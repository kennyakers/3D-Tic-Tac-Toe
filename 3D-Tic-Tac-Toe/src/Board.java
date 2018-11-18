
import java.util.ArrayList;

public class Board {

    private int[][][] board;

    public ArrayList<Board> goalStates;

    public Board() {
        this.board = new int[4][4][4];
        this.goalStates = this.generateGoalStates();
    }

    public Board(int[][][] inputBoard) {
        this.board = inputBoard;
    }

//    private boolean isGoalState(int player) {
//        
//    }
    private ArrayList<Board> generateGoalStates() {
        // Single plane: horizontal
        ArrayList<Board> list = new ArrayList<>();
        for (int level = 0; level < this.board.length; level++) {
            for (int row = 0; row < this.board[0].length; row++) {
                int[][][] newBoard = new int[4][4][4];
                for (int column = 0; column < this.board[0][0].length; column++) {
                    newBoard[level][row][column] = 3; // 2 = win for either player
                }
                list.add(new Board(newBoard));
            }
        }

        // Single plane: vertical
        for (int level = 0; level < this.board.length; level++) {
            for (int row = 0; row < this.board[0].length; row++) {
                int[][][] newBoard = new int[4][4][4];
                for (int column = 0; column < this.board[0][0].length; column++) {
                    newBoard[level][column][row] = 3; // 2 = win for either player
                }
                list.add(new Board(newBoard));
            }
        }

        // Multi-plane: vertical columns
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board[0].length; column++) {
                int[][][] newBoard = new int[4][4][4];
                for (int level = 0; level < this.board[0][0].length; level++) {
                    newBoard[level][row][column] = 3; // 2 = win for either player
                }
                list.add(new Board(newBoard));
            }
        }
        return list;
    }

    public void print() {
        for (int level = 0; level < this.board.length; level++) {
            System.out.println("Level " + level);
            for (int row = 0; row < this.board.length; row++) {
                for (int column = 0; column < this.board[0].length; column++) {
                    System.out.print(this.board[level][row][column]);
                }
                System.out.println();
            }
            
        }
    }
}
