
import java.util.ArrayList;

public class Board {

    private int[][][] board;
    private int playerNext = 1;
    private int turnCount = 0;

    public boolean DEBUG;

    public static ArrayList<Goal> goalStates;
    private static ArrayList<Coordinate> player1Pieces;
    private static ArrayList<Coordinate> player2Pieces;

    // Parameters
    private final double POWER_FACTOR_SCALAR = 10.0;
    private final double BLOCKING_FACTOR_SCALAR = 1.0;
    private final double OPPORTUNITY_FACTOR_SCALAR = 1.0;

    public Board(boolean debug) {
        this(new int[4][4][4], debug);
    }

    public Board(int[][][] inputBoard, boolean debug) {
        this.board = inputBoard;
        this.DEBUG = debug;
        goalStates = this.generateGoalStates();
        player1Pieces = new ArrayList<>();
        player2Pieces = new ArrayList<>();
    }

    private ArrayList<Goal> generateGoalStates() {
        // Single plane: horizontal
        ArrayList<Goal> list = new ArrayList<>();
        for (int level = 0; level < this.board.length; level++) {
            for (int row = 0; row < this.board[0].length; row++) {
                Goal goal = new Goal();
                for (int column = 0; column < this.board[0][0].length; column++) {
                    Coordinate point = new Coordinate(column, row, level);
                    goal.set(column, point);
                }
                list.add(goal);
            }
        }

        // Single plane: vertical
        for (int level = 0; level < this.board.length; level++) {
            for (int row = 0; row < this.board[0].length; row++) {
                Goal goal = new Goal();
                for (int column = 0; column < this.board[0][0].length; column++) {
                    Coordinate point = new Coordinate(row, column, level);
                    goal.set(column, point);
                }
                list.add(goal);
            }
        }

        // Single plane: diagonals
        for (int level = 0; level < this.board.length; level++) {
            Goal goal1 = new Goal();
            Goal goal2 = new Goal();
            for (int i = 0; i < this.board[0].length; i++) {
                Coordinate point1 = new Coordinate(i, i, level);
                Coordinate point2 = new Coordinate(this.board[0][0].length - 1 - i, i, level);
                goal1.set(i, point1);
                goal2.set(i, point2);
            }
            list.add(goal1);
            list.add(goal2);
        }

        // Multi-plane: vertical columns
        for (int row = 0; row < this.board[0].length; row++) {
            for (int column = 0; column < this.board[0][0].length; column++) {
                Goal goal = new Goal();
                for (int level = 0; level < this.board.length; level++) {
                    Coordinate point = new Coordinate(column, row, level);
                    goal.set(level, point);
                }
                list.add(goal);
            }
        }

        // Multi-plane: side to side diagonal
        for (int row = 0; row < this.board[0].length; row++) {
            Goal goal1 = new Goal();
            Goal goal2 = new Goal();
            for (int i = 0; i < this.board.length; i++) {
                Coordinate point1 = new Coordinate(i, row, i);
                Coordinate point2 = new Coordinate(this.board[0][0].length - 1 - i, row, i);
                goal1.set(i, point1);
                goal2.set(i, point2);
            }
            list.add(goal1);
            list.add(goal2);
        }

        // Multi-plane: front to back diagonal
        for (int column = 0; column < this.board[0].length; column++) {
            Goal goal1 = new Goal();
            Goal goal2 = new Goal();
            for (int i = 0; i < this.board.length; i++) {
                Coordinate point1 = new Coordinate(column, i, i);
                Coordinate point2 = new Coordinate(column, this.board[0][0].length - 1 - i, i);
                goal1.set(i, point1);
                goal2.set(i, point2);
            }
            list.add(goal1);
            list.add(goal2);
        }

        // Multi-plane: corner to corner diagonal 
        Goal goal1 = new Goal();
        Goal goal2 = new Goal();
        Goal goal3 = new Goal();
        Goal goal4 = new Goal();
        for (int i = 0; i < this.board.length; i++) {
            goal1.set(i, new Coordinate(i, i, i));
            goal2.set(i, new Coordinate(this.board[0][0].length - 1 - i, i, i));
            goal3.set(i, new Coordinate(this.board[0][0].length - 1 - i, this.board[0][0].length - 1 - i, i));
            goal4.set(i, new Coordinate(i, this.board[0][0].length - 1 - i, i));
        }
        list.add(goal1);
        list.add(goal2);
        list.add(goal3);
        list.add(goal4);

        return list;
    }

    public int turn() {
        return this.playerNext;
    }

    public boolean move(int x, int y, int z) {
        int opponent = this.playerNext == 1 ? 2 : 1;

        if (DEBUG) {
            System.out.println("\tMove request #" + (this.turnCount + 1) + " by player " + this.playerNext + " for coordinates " + x + "," + y + "," + z);
        }

        if (this.board[x][y][z] == opponent) { // Spot already filled
            if (DEBUG) {
                System.out.println("\tMove request rejected, opponent occupies request");
            }
            return false;
        }
        if (DEBUG) {
            System.out.println("\tMove request allowed\n");
        }
        Coordinate move = new Coordinate(x, y, z);

        if (this.playerNext == 1) {
            player1Pieces.add(move);
        } else {
            player2Pieces.add(move);
        }

        this.board[x][y][z] = this.playerNext;
        this.turnCount++;
        this.playerNext = opponent;

        return true;
    }

    public boolean isGoalState(int playerID) {
        for (Goal g : goalStates) {
            boolean match = true;
            for (Coordinate c : g.points) {
                int x = c.column;
                int y = c.row;
                int z = c.level;

                //If this goalState doesn't allign, break out of the loop
                if (this.board[x][y][z] != playerID) {
                    match = false;
                }
            }
            if (match == true) {
                return true;
            }
        }
        return false;
    }

    // TO ADD:
    // Opportunity and blocking counts
    //      - Helper method to return all the possible lines from a point.
    public int evaluationFunction(int playerID) {
        int moveCount = this.turnCount / 2;
        if (this.turnCount == 1) {
            moveCount = 1;
        }
        int opponent = playerID == 1 ? 2 : 1;

        ArrayList<Coordinate> coordinates;
        if (playerID == 1) {
            coordinates = player1Pieces;
        } else {
            coordinates = player2Pieces;
        }

        //Power positions
        int powerCount = 0;
        for (Coordinate point : coordinates) {
            if (point.level == 0 || point.level == this.board.length - 1) { //Top or bottom
                if (point.row == 0 || point.row == this.board.length - 1) { //Left or right
                    if (point.column == 0 || point.column == this.board.length - 1) { //Front or back
                        powerCount++;
                    }
                }
            }

            if (point.level == 1 || point.level == 2) { //Two middle
                if (point.row == 1 || point.row == 2) { //Two middle
                    if (point.column == 1 || point.column == 2) { //Two middle
                        powerCount++;
                    }
                }
            }
        }

        double powerFactor = ((double) this.POWER_FACTOR_SCALAR) / ((double) moveCount);
        double powerPosition = powerCount * powerFactor;

        if (this.DEBUG) {
            System.out.println("PLAYER " + playerID + " STATS:");
            System.out.println("\tmoveCount: " + moveCount);
            System.out.println("\tpowerCount: " + powerCount);
            System.out.println("\tpowerFactor: " + powerFactor);
            System.out.println("\tpowerPosition: " + powerPosition);
        }

        // Distance to goalStates, blockingFactor, and opportunityFactor
        int totalFilled = 0;
        double blockingFactor = 0.0;
        double opportunityFactor = 0.0;
        for (Goal goalState : goalStates) {
            int sum = 0;
            boolean opponentOccupies = false;
            int numOpponentsPieces = this.numPlayersPiecesInLine(goalState, opponent);
            int numCurrentPlayersPieces = this.numPlayersPiecesInLine(goalState, playerID);

            // More positive means more blocking potential in this line.
            blockingFactor += (this.BLOCKING_FACTOR_SCALAR * (numOpponentsPieces - numCurrentPlayersPieces));

            // If this goal line has an opponent piece in it, it's useless to us now (opportunityFactor = 0).
            // Otherwise, the more of our pieces in this line, the higher the opportunityFactor.
            opportunityFactor += numOpponentsPieces > 0 ? 0 : (this.OPPORTUNITY_FACTOR_SCALAR * numCurrentPlayersPieces);

            for (Coordinate point : goalState.points) {
                int x = point.column;
                int y = point.row;
                int z = point.level;

                if (this.board[x][y][z] == playerID) { // If the current player occupies this spot.
                    sum++;
                } else if (this.board[x][y][z] == opponent) { // If the opponent occupies this spot.
                    opponentOccupies = true;
                }
            }
            if (!opponentOccupies) {
                totalFilled += sum;
            }
        }

        double goalFactor = moveCount;
        double distance = totalFilled * goalFactor;
        if (DEBUG) {
            System.out.println("\tgoalFactor: " + goalFactor);
            System.out.println("\topponent: " + opponent);
            System.out.println("\ttotalFilled: " + totalFilled);
            System.out.println("\tdistance: " + distance);
            System.out.println("\tblockingFactor: " + blockingFactor);
            System.out.println("\topportunityFactor: " + opportunityFactor);
        }

        return (int) (powerPosition + distance + blockingFactor + opportunityFactor);

    }

    private ArrayList<Goal> goalsFromPoint(Coordinate point) {
        ArrayList<Goal> subsetOfGoals = new ArrayList<>();
        for (Goal goal : goalStates) {
            if (goal.contains(point)) {
                subsetOfGoals.add(goal);
            }
        }
        return subsetOfGoals;
    }

    private int numPlayersPiecesInLine(Goal line, int playerID) {
        int count = 0;
        for (Coordinate point : line.points) {
            if (this.getPlayerAt(point) == playerID) {
                count++;
            }
        }
        return count;
    }

    private int getPlayerAt(Coordinate point) {
        return this.board[point.level][point.row][point.column];
    }

    public void print() {
        for (int level = 0; level < this.board.length; level++) {
            System.out.println("Level " + level);
            for (int row = 0; row < this.board.length; row++) {
                for (int column = 0; column < this.board[0].length; column++) {
                    System.out.print("\t" + this.board[level][row][column]);
                }
                System.out.println("");
            }
        }
        System.out.println("");
    }

    public class Goal {

        public Coordinate[] points;

        public Goal() {
            this.points = new Coordinate[4];
        }

        public void set(int index, Coordinate point) {
            this.points[index] = point;
        }

        public void print() {
            for (Coordinate point : points) {
                point.print();
            }
        }

        public boolean contains(Coordinate point) {
            return this.contains(point.column, point.row, point.level);
        }

        public boolean contains(int x, int y, int z) {
            for (Coordinate point : this.points) {
                if (point.column == x && point.row == y && point.level == z) {
                    return true;
                }
            }
            return false;
        }
    }

    private class Coordinate {

        public int level;
        public int row;
        public int column;

        public Coordinate(int column, int row, int level) {
            this.level = level;
            this.row = row;
            this.column = column;
        }

        public void print() {
            System.out.println("(" + this.column + ", " + this.row + ", " + this.level + ")");
        }
    }
}
