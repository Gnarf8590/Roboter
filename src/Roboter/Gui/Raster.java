package Roboter.Gui;

import Roboter.Coordinates;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

public class Raster
{
    private int x;
    private int y;

    private final List<Color[]> colors;
    private final int rasterSizeX;
    private final int rasterSizeY;

    public Raster(int x, int y, List<Color[]> colors)
    {
        this.x = x;
        this.y = y;
        this.colors = colors;
        this.rasterSizeY = colors.size();
        this.rasterSizeX = colors.get(0).length;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRasterSizeX()
    {
        return rasterSizeX;
    }

    public int getRasterSizeY()
    {
        return rasterSizeY;
    }

    public Coordinates getMiddle() {
        return new Coordinates(x + (rasterSizeX / 2), y + (rasterSizeY / 2));
    }


    public Color getColor()
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        for(Color[] elem : colors)
        {
            for(int j = 0; j < elem.length; j++)
            {
                Color color = elem[j];
                red     += color.getRed();
                green   += color.getGreen();
                blue    += color.getBlue();
            }
        }

        red   = red   / (rasterSizeX * rasterSizeY);
        green = green / (rasterSizeX * rasterSizeY);
        blue  = blue  / (rasterSizeX * rasterSizeY);

        return new Color(red, green, blue);
    }

    @Override
    public String toString() {
        return "Raster{" +
                "x=" + x +
                ", y=" + y +
                ", rasterSizeX=" + rasterSizeX +
                ", rasterSizeY=" + rasterSizeY +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Raster raster = (Raster) o;
        return x == raster.x &&
                y == raster.y &&
                rasterSizeX == raster.rasterSizeX &&
                rasterSizeY == raster.rasterSizeY &&
                Objects.equals(colors, raster.colors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, colors, rasterSizeX, rasterSizeY);
    }
}
