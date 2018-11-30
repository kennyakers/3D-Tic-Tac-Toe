
import java.util.ArrayList;

public class AI {

    public Coordinate nextMove(Board board) {
        ArrayList<Coordinate> openSpots = board.getOpenSpots();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        UtilMove bestMove = new UtilMove(Integer.MIN_VALUE, null);
        for (Coordinate point : openSpots) {
            UtilMove currentMove = this.minValue(board, alpha, beta, board.turn()); // We want to maximize the mins for this player. 
            if (currentMove.getUtility() > bestMove.getUtility()) { // If this move is better
                bestMove = currentMove;
            }
        }
        return bestMove.getMove();
    }

    private UtilMove maxValue(Board boardState, int alpha, int beta, int currentPlayer) {
        System.out.println("---------------\n\nmaxValue: " + boardState);
        boardState.print();
        if (boardState.isGoalState(currentPlayer) || boardState.getOpenSpots().isEmpty()) { // If this is a goal state.
            System.out.println("\n\nRETURNING\n\n");
            return new UtilMove(boardState.evaluationFunction(currentPlayer), null);
        }
        Coordinate action = null;
        ArrayList<Coordinate> openSpots = boardState.getOpenSpots();
        for (Coordinate move : openSpots) {
            Board tempBoard = boardState.copyBoard();
            tempBoard = tempBoard.move(move, tempBoard.turn());
            boardState.getOpenSpots().remove(move);
            System.out.println("Move applied: " + move);
            System.out.println("tempBoard: ");
            tempBoard.print();
            int minVal = minValue(tempBoard, alpha, Integer.MAX_VALUE, tempBoard.turn()).getUtility();
            if (minVal > beta) {
                return new UtilMove(minVal, move);
            }
            if (minVal > alpha) {
                action = move;
                alpha = minVal;
            }
        }
        return new UtilMove(alpha, action);
    }

    private UtilMove minValue(Board boardState, int alpha, int beta, int currentPlayer) {
        System.out.println("----------------\n\nminValue: " + boardState);
        boardState.print();
        if (boardState.isGoalState(currentPlayer) || boardState.getOpenSpots().isEmpty()) {
            System.out.println("\n\nRETURNING\n\n");
            return new UtilMove(boardState.evaluationFunction(currentPlayer), null);
        }

        Coordinate action = null;
        ArrayList<Coordinate> openSpots = boardState.getOpenSpots();
        for (Coordinate move : openSpots) {
            Board tempBoard = boardState.copyBoard();
            tempBoard = tempBoard.move(move, tempBoard.turn());
            boardState.getOpenSpots().remove(move);
            System.out.println("Move applied: " + move);
            System.out.println("tempBoard: ");
            tempBoard.print();
            int maxVal = maxValue(tempBoard, Integer.MIN_VALUE, beta, tempBoard.turn()).getUtility();
            if (maxVal < alpha) {
                return new UtilMove(maxVal, move);
            }
            if (maxVal < beta) {
                beta = maxVal;
                action = move;
            }
        }

        return new UtilMove(beta, action);
    }

    private class UtilMove {

        private int utility;
        private Coordinate move;

        public UtilMove(int utilVal, Coordinate move) {
            this.utility = utilVal;
            this.move = move;
        }

        public int getUtility() {
            return this.utility;
        }

        public Coordinate getMove() {
            return this.move;
        }
    }

}
