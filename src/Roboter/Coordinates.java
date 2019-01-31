package Roboter;

import java.util.Objects;

public class Coordinates
{
    final public int x;
    final public int y;

    public Coordinates(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public double distance(Coordinates other)
    {
        int X = (int)Math.pow(Math.abs(x-other.x),2);
        int Y = (int)Math.pow(Math.abs(y-other.y),2);

        return Math.sqrt(X+Y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
