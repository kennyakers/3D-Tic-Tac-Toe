
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

    public String toString() {
        return ("(" + this.column + ", " + this.row + ", " + this.level + ")");
    }

    @Override
    public boolean equals(Object other) {
        Coordinate otherCord = (Coordinate) other;
        return this.hashCode() == otherCord.hashCode();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.level;
        hash = 97 * hash + this.row;
        hash = 97 * hash + this.column;
        return hash;
    }

}
