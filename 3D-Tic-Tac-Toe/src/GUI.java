
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;

public class GUI extends JPanel {

    public JButton[][] buttons;
    private JPanel[] levels;
    private JFrame frame;
    private JPanel panel;

    public GUI(int dimension) {
        this.frame = new JFrame("3D-Tic-Tac-Toe");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            for (int j = 0; j < this.buttons[0].length; j++) {
                this.buttons[i][j] = new JButton("");
                this.buttons[i][j].addActionListener(new ButtonListener(Game.DEBUG));
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

    public void setButtonsEnabled(boolean enabled) {
        for (int i = 0; i < this.levels.length; i++) {
            for (int j = 0; j < this.buttons[0].length; j++) {
                this.buttons[i][j].setEnabled(enabled);
            }
        }
    }

    public void move(Coordinate move) {
        this.buttons[move.x][(4 * move.y) + move.z].setText("2");
    }

    public void showWinMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
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

            buttonClicked.setText("1");

            if (debug) {
                System.out.println("\tColumn: " + column);
                System.out.println("\tRow: " + row);
                System.out.println("\tLevel: " + level);
            }

            Coordinate move;
            boolean success = false;
            do {
                move = new Coordinate(level, row, column);

                success = Game.board.isSquareBlank(move);
                if (!success) {
                    System.out.println("Invalid move. Try again.");
                }
            } while (!success);
            Game.board = Game.board.move(move, 1);
            Game.board.turnCount++;
            if (Game.DEBUG) {
                Game.board.print();
                System.out.println("Evaluation of that move: " + Game.board.evaluationFunction(1));
                if (Game.CONSIDER_BLOCKING) {
                    System.out.println("Blocking factor: " + Game.board.blockingFactor(1, 2));
                }
                System.out.println("Total filled: " + Game.board.DEBUG_totalFilled);
                if (Game.COUNT_TURNS) {
                    System.out.println("Turn count: " + Game.board.turnCount);
                }
            }

            if (Game.board.isGoalState()) {
                Game.isAITurn = false;
            } else {

                Game.isAITurn = true;
            }
        }

    }
}
