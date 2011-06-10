package alit.sudoku;

public class Point {
    public int x = 0;
    public int y = 0;

    public Point() {
    }

    @Override
    public int hashCode() {
        return x + y;
    }

    @Override
    public boolean equals(Object obj) {
        Point p = (Point)obj;
        if(p == null)
            return false;
        return this.x == p.x && this.y == p.y;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
