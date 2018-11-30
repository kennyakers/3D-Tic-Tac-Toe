
import java.io.Console;

public class Main {

    public static Board board;

    public final static boolean DEBUG = true;
    public final static boolean ENABLE_AI = false;

    public static void main(String[] args) {

        //GUI gui = new GUI(4);
        board = new Board(DEBUG);

        AI ai = new AI();
        board = move(ai.nextMove(board), 2);
        System.out.println("\n\n\n\n\n\nMOVE\n\n\n\n\n");
        board.print();


        /*
        Board board = new Board();
        Console console = System.console();
        String line = console.readLine("Command: ").trim();
        while (!board.isGoalState(1) && !board.isGoalState(2)) {

        String command = getCommand(line);

            switch (command) {
                case "move":
                    int x = Integer.parseInt(getArgument(line, 1));
                    int y = Integer.parseInt(getArgument(line, 2));
                    int z = Integer.parseInt(getArgument(line, 3));

                    board.move(x, y, z);

                    if (board.DEBUG) {
                        System.out.println("Evaluation Function Player 1: " + board.evaluationFunction(1));
                        System.out.println("Evaluation Function Player 2: " + board.evaluationFunction(2));
                    }

                    System.out.println("Player to move: " + board.turn());

                    break;

                case "print":
                    System.out.println("\nCurrent board");
                    board.print();
                    break;

                case "end":
                case "exit":
                case "quit":
                    return;

                default:
                    System.out.println("Invalid command: " + command);
                    break;
            }

            line = console.readLine("Command: ").trim();
             
        }
         */
    }

    public static Board move(Coordinate point, int player) {
        return move(point.column, point.row, point.level, player);
    }

    public static Board move(int column, int row, int level, int player) {
        return board.move(column, row, level, player);
    }

    private static String getArgument(String line, int index) {
        String[] words = line.split("\\s");
        return words.length > index ? words[index] : "";
    }

    private static String getCommand(String line) {
        return getArgument(line, 0);
    }

}
