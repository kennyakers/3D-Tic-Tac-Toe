
import java.util.ArrayList;

public class Board {

    private int[][][] board;

    public ArrayList<Goal> goalStates;

    public Board() {
        this.board = new int[4][4][4];
        this.goalStates = this.generateGoalStates();
    }

    public Board(int[][][] inputBoard) {
        this.board = inputBoard;
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

        // Single plane: diagonal bottom left to top right
        for (int level = 0; level < this.board.length; level++) {
            Goal goal = new Goal();
            for (int row = 0; row < this.board[0].length; row++) {
                Coordinate point = new Coordinate(row, row, level);
                goal.set(row, point);
            }
            list.add(goal);
        }

        // Single plane: diagonal bottom right to top left
        for (int level = 0; level < this.board.length; level++) {
            Goal goal = new Goal();
            for (int row = 0; row < this.board[0].length; row++) {
                Coordinate point = new Coordinate(3 - row, row, level);
                goal.set(row, point);
            }
            list.add(goal);
        }

        // Multi-plane: vertical columns
        for (int row = 0; row < this.board.length; row++) {
            for (int column = 0; column < this.board[0].length; column++) {
                Goal goal = new Goal();
                for (int level = 0; level < this.board[0][0].length; level++) {
                    Coordinate point = new Coordinate(column, row, level);
                    goal.set(level, point);
                }
                list.add(goal);
            }
        }

        // Multi-plane: side diagonal
        

        // Multi-plane: center diagonal 
        return list;
    }

    private int playerNext = 1;
    private int turnCount = 1;

    private boolean debug = true;

    public int turn() {
        return playerNext;
    }

    public boolean move(int x, int y, int z) {
        int opponent;
        if (playerNext == 1) {
            opponent = 2;
        } else {
            opponent = 1;
        }

        if (debug) {
            System.out.println("Move request #" + turnCount + " for coordinates" + x + "," + y + "," + z + " ,playerNext being " + playerNext + " opponent being " + opponent);
        }

        if (board[x][y][z] == opponent) { //Spot already filled
            if (debug) {
                System.out.println("    Move request rejected, opponent occupies request");
            }
            return false;
        } else {
            if (debug) {
                System.out.println("    Move request allowed, nextPlayer is now opponent");
            }

        }
        turnCount++;
        playerNext = opponent;
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
                    continue;
                }
            }
            if (match == true) {
                return true;
            }
        }
        return false;
    }

    public int evaluationFunction(int playerID) {
        ArrayList<Coordinate> coordinates = new ArrayList();
        //Generates all occupied positions by playerID
        for (int column = 0; column < board.length; column++) {
            for (int row = 0; row < board[column].length; row++) {
                for (int level = 0; level < board[column][row].length; level++) {
                    if (board[column][row][level] == playerID) {
                        coordinates.add(new Coordinate(column, row, level));
                    }
                }
            }
        }

        //Power positions
        int powerCount = 0;
        for (Coordinate c : coordinates) {
            if (c.level == 0 || c.level == 3) { //Top or bottom
                if (c.row == 0 || c.row == 3) { //Left or right
                    if (c.column == 0 || c.column == 3) { //Front or back
                        powerCount++;
                    }
                }
            }

            if (c.level == 1 || c.level == 2) { //Two middle
                if (c.row == 1 || c.row == 2) { //Two middle
                    if (c.column == 1 || c.column == 2) { //Two middle
                        powerCount++;
                    }
                }
            }
        }
        float powerFactor = 1 / turnCount;
        float powerPosition = powerCount * powerFactor;

        //Distance to goalStates
        int netDistance = 0;
        for (Goal g : goalStates) {
            for (Coordinate c : g.points) {
                int x = c.column;
                int y = c.row;
                int z = c.level;

                if (board[x][y][z] != playerID) {
                    netDistance++;
                }
            }
        }
        float goalFactor = turnCount;
        float distance = netDistance * goalFactor;

        return (int) (powerPosition + distance);

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
