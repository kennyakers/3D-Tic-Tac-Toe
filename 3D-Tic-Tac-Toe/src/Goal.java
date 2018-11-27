
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
        // If this goal contains this point.
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
