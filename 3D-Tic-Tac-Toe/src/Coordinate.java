
public class Coordinate {

    public final int level;
    public final int row;
    public final int column;

    public Coordinate(int column, int row, int level) {
        this.level = level;
        this.row = row;
        this.column = column;
    }

    public void print() {
        System.out.println("(" + this.column + ", " + this.row + ", " + this.level + ")");
    }

}
