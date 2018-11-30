
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
    public int hashCode() {
        return this.column*100+this.row*10+this.level;
    }
    @Override
    public boolean equals(Object other){
        Coordinate otherCord = (Coordinate)other;
        return this.hashCode() == otherCord.hashCode();
    }

}
