
import java.util.Scanner;
import java.util.Stack;

public class Game {

    /**
     * Behavioral Notes
     *
     * 1. Is good at blocking and winning early on. As game goes on, it won't
     * choose the winning move even though we assign the winning move the
     * largest int value possible. (Alpha-Beta problem or heuristic problem? Or
     * both?) 
     * 2. Increasing ply depth from 2 --> 3 drastically increases
     * computation time, and even makes it make worse decisions (doesn't block
     * 100% of the time now) Is this normal? 
     * 3. Is there something wrong with our Alpha-Beta pruning such that it's not 
     * pruning as much as it could? How to implement move ordering? 
     * 4. Enabling pruning SIGNIFICANTLY speeds up search time (5x + better for ply depth 3) 
     * 5. No difference between pruning enabled and pruning disabled in terms of move responses 
     *  --> Above observations are almost certainly down to evaluation function 
     * 6. There IS a difference between AB pruning implementations, though. 
     * 7. Last year's program ran into the same problem of getting 2 three in a rows and then
     * not choosing either of the solutions.
     * 8. Overall, we could turn this in right now, but I want it to be better than last year's.
     */
    
    private static final int MAX_PLY_DEPTH = 2;
    public static final boolean DEBUG = false;
    private static final boolean ENABLE_GUI = true;
    public static final boolean PRIORITIZE_CORNER_MOVES = false;
    public static final boolean PRIORITIZE_MULTILEVEL_MOVES = false;
    public static final boolean CONSIDER_BLOCKING = false;
    public static final boolean COUNT_TURNS = false;
    public static final boolean ENABLE_AB_PRUNING = true;
    public static final boolean ENABLE_MOVE_ORDERING = false;
    public static final boolean CONSIDER_POWER_POSITIONS = false;

    private static GUI gui;
    public static Board board;
    public static boolean isAITurn = false;

    public static void main(String[] args) {
        // TESTER FOR SETTING UP BOARD CONFIGS. HAS UNDO CAPABILITY
//        Scanner scanner = new Scanner(System.in);
//        Board board = new Board();
//        Coordinate move = null;
//        Stack<Coordinate> lastMoves = new Stack<>();
//        while (true) {
//            System.out.print("\nPlayer | ");
//            move = getMove(board, scanner, lastMoves);
//            board = board.move(move, 1);
//            lastMoves.add(move);
//            board.print();
//            System.out.println("Eval for Player: " + board.evaluationFunction(1));
//            System.out.println("Eval for AI: " + board.evaluationFunction(2));
//            System.out.print("\nAI | ");
//            move = getMove(board, scanner, lastMoves);
//            board = board.move(move, 2);
//            lastMoves.add(move);
//            board.print();
//            System.out.println("Eval for Player: " + board.evaluationFunction(1));
//            System.out.println("Eval for AI: " + board.evaluationFunction(2));
//        }

        Scanner scanner = new Scanner(System.in);
        if (ENABLE_GUI) {
            gui = new GUI(4);
        } else {
            System.out.println("Welcome to 3D Tic Tac Toe.");
            System.out.println("Player: input coordinates as `level <space> row <space> column` where (0,0,0) is top left corner of bottom level");
        }
        board = new Board();

        while (board.getOpenSpots().size() > 0 && !board.isGoalState()) {
            if (!ENABLE_GUI) {
                board.turnCount++;
                board = playerMove(board, scanner, 1);
                if (DEBUG) {
                    System.out.println("Evaluation of that move: " + board.evaluationFunction(1));
                    if (CONSIDER_BLOCKING) {
                        System.out.println("Blocking factor: " + board.blockingFactor(1, 2));
                    }
                    System.out.println("Total filled: " + board.DEBUG_totalFilled);
                    if (COUNT_TURNS) {
                        System.out.println("Turn count: " + board.turnCount);
                    }
                }

                if (board.isGoalState()) {
                    System.out.println("Player won!");
                    break;
                }

                board = aiMove(board, 2);
                board.turnCount++;
                if (DEBUG) {
                    System.out.println("Evaluation of that move: " + board.evaluationFunction(2));
                    if (CONSIDER_BLOCKING) {
                        System.out.println("Blocking factor: " + board.blockingFactor(2, 1));
                    }
                    System.out.println("Total filled: " + board.DEBUG_totalFilled);
                    if (COUNT_TURNS) {
                        System.out.println("Turn count: " + board.turnCount);
                    }
                }

                if (board.isGoalState()) {
                    System.out.println("AI won!");
                    break;
                }
            } else { // Using the GUI
                if (isAITurn) {
                    board = aiMove(board, 2);
                    isAITurn = false;
                    board.turnCount++;
                    if (DEBUG) {
                        board.print();
                        System.out.println("Evaluation of that move: " + board.evaluationFunction(2));
                        if (CONSIDER_BLOCKING) {
                            System.out.println("Blocking factor: " + board.blockingFactor(2, 1));
                        }
                        System.out.println("Total filled: " + board.DEBUG_totalFilled);
                        if (COUNT_TURNS) {
                            System.out.println("Turn count: " + board.turnCount);
                        }
                    }

                    if (board.isGoalState()) {
                        gui.showWinMessage("AI won!");
                        break;
                    }
                }
            }
            if (board.getOpenSpots().isEmpty()) {
                System.out.println("Tie: No spots left");
                break;
            }
            if (board.isGoalState()) {
                gui.showWinMessage("Player won!");
                break;
            }
        }
        System.exit(1);
    }

    public static Board aiMove(Board board, int aiTile) {
        AI ai = new AI();
        Coordinate move = ai.nextMove(board, aiTile, MAX_PLY_DEPTH).getMove();
        if (!board.isSquareBlank(move)) {
            System.out.println("Square not blank");
            return aiMove(board, aiTile);
        }
        board = board.move(move, aiTile);
        if (ENABLE_GUI) {
            gui.move(move);
        } else {
            board.print();
            System.out.println("AI moved " + move);
        }
        return board;

    }

    // Used only for the board configuration tester
    private static Coordinate getMove(Board board, Scanner scanner, Stack<Coordinate> moves) {
        System.out.print("Move: ");
        String input = "";
        int x = 0, y = 0, z = 0;

        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
            if (input.equals("undo")) {
                Coordinate lastMove = moves.pop();
                board.board[lastMove.x][lastMove.y][lastMove.z] = 0;
                board.print();
                System.out.println("Eval for Player: " + board.evaluationFunction(1));
                System.out.println("Eval for AI: " + board.evaluationFunction(2));
                getMove(board, scanner, moves);
            }
            String[] parsed = input.split(" ");
            try {
                x = Integer.parseInt(parsed[0]);
                y = Integer.parseInt(parsed[1]);
                z = Integer.parseInt(parsed[2]);
                if ((x > 3 || x < 0) || (y > 3 || y < 0) || (z > 3 || z < 0)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Invalid move.");
            }

        } else {
            System.err.print("Invalid input");
        }
        return new Coordinate(x, y, z);
    }

    public static Board playerMove(Board board, Scanner scanner, int playerTile) {
        System.out.print("Move: ");
        String input = "";
        int x = 0, y = 0, z = 0;

        if (scanner.hasNextLine()) {
            input = scanner.nextLine();
            String[] parsed = input.split(" ");
            try {
                x = Integer.parseInt(parsed[0]);
                y = Integer.parseInt(parsed[1]);
                z = Integer.parseInt(parsed[2]);
                if ((x > 3 || x < 0) || (y > 3 || y < 0) || (z > 3 || z < 0)) {
                    throw new Exception();
                }
            } catch (Exception e) {
                System.out.println("Invalid move.");
                return playerMove(board, scanner, playerTile);
            }

        } else {
            System.err.print("Invalid input");
            return playerMove(board, scanner, playerTile);
        }
        Coordinate move;
        boolean success = false;
        do {
            move = new Coordinate(x, y, z);

            success = board.isSquareBlank(move);
            if (!success) {
                System.out.println("Invalid move. Try again.");
                return playerMove(board, scanner, playerTile);
            }
        } while (!success);
        board = board.move(move, playerTile);
        board.print();
        System.out.println(playerTile + " moved " + move);
        return board;
    }

    public static int getPlayerTile(Scanner scanner) {
        System.out.print("Player tile is (X / O): ");
        String tile = "";
        if (scanner.hasNextLine()) {
            tile = scanner.nextLine();
            if (tile.toUpperCase().equals("X")) {
                return 1;
            } else if (tile.toUpperCase().equals("O")) {
                return 2;
            } else {
                System.out.println("Invalid Tile. Must be X or O.");
                getPlayerTile(scanner);
            }
        }
        return 0;
    }
}
