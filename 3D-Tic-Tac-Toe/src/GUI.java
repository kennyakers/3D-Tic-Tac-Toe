
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

/**
 * Kenny Akers Mr. Paige Homework #
 *
 */
public class GUI extends JPanel {

    private JButton[][] buttons;
    private JPanel[] levels;
    private JFrame frame;
    private JPanel panel;

    public GUI(int dimension, boolean debug) {
        this.frame = new JFrame("3D-Tic-Tac-Toe");
        this.buttons = new JButton[4][16]; // 4 levels, 16 buttons each.
        this.levels = new JPanel[4]; // 4 levels.
        this.panel = new JPanel(new GridLayout(1, 4)); // The background panel. Each panel in @levels will be in one of the 4 slots in this panel.

        for (int i = 1; i <= this.levels.length; i++) {
            TitledBorder border = new TitledBorder("Level " + i);
            border.setTitleJustification(TitledBorder.CENTER);
            border.setTitlePosition(TitledBorder.TOP);
            this.levels[i - 1] = new JPanel(new GridLayout(dimension, dimension));
            this.levels[i - 1].setBorder(border);
        }

        for (int i = 0; i < this.levels.length; i++) {
            this.buttons[i] = new JButton[16];
            for (int j = 0; j < this.buttons[i].length; j++) {
                this.buttons[i][j] = new JButton("");
                this.buttons[i][j].addActionListener(new ButtonListener(debug));
                this.buttons[i][j].putClientProperty("column", j % dimension);
                this.buttons[i][j].putClientProperty("row", j / dimension);
                this.buttons[i][j].putClientProperty("level", i);
                this.levels[i].add(this.buttons[i][j]);
            }
            panel.add(this.levels[i]);
        }

        frame.add(panel);
        frame.setSize(1000, 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class ButtonListener implements ActionListener {

        private final boolean debug;

        public ButtonListener(boolean debug) {
            this.debug = debug;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            JButton buttonClicked = (JButton) e.getSource(); // Get the particular button that was clicked

            int column = (Integer) buttonClicked.getClientProperty("column");
            int row = (Integer) buttonClicked.getClientProperty("row");
            int level = (Integer) buttonClicked.getClientProperty("level");

            if (debug) {
                System.out.println("Column: " + column);
                System.out.println("Row: " + row);
                System.out.println("Level: " + level);
                System.out.println("Evaluation Function Player 1: " + Main.board.evaluationFunction(1));
                System.out.println("Evaluation Function Player 2: " + Main.board.evaluationFunction(2));
            }

            int currentPlayer = Main.board.turn();
            buttonClicked.setText("" + currentPlayer);
            Main.move(column, row, level);
            if (Main.board.isGoalState(currentPlayer)) {
                JOptionPane.showMessageDialog(null, "Player " + currentPlayer + " Wins");
                System.exit(1);
            } else {
                System.out.println("Player to move: " + Main.board.turn());
            }
        }

    }
}
