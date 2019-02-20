package Roboter.Ansteuerung;

import java.util.Objects;

public class Position implements Cloneable
{
    public double x;
    public double y;
    public double z;
    public double a;
    public double b;
    public double c;

    public Position(double x, double y, double z, double a, double b, double c)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    protected Position clone()
    {
        return new Position(x, y, z, a, b ,c);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return Double.compare(position.x, x) == 0 &&
                Double.compare(position.y, y) == 0 &&
                Double.compare(position.z, z) == 0 &&
                Double.compare(position.a, a) == 0 &&
                Double.compare(position.b, b) == 0 &&
                Double.compare(position.c, c) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, z, a, b, c);
    }

    @Override
    public String toString()
    {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
