package code.trevooga.demo;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class Point {
    @Getter
    @Setter
    private int x;

    @Getter
    @Setter
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
