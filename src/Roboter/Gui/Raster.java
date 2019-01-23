package Roboter.Gui;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Raster
{
    private int x;
    private int y;
    private List<Color[]> colorList;
    private final int rasterSize;

    public Raster(int x, int y, List<Color[]> colorList, int rasterSize)
    {
        this.x = x;
        this.y = y;
        this.colorList = colorList;
        this.rasterSize = rasterSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates getMiddle() {
        return new Coordinates(x + (rasterSize / 2), y + (rasterSize/2));
    }

    public Color getColor()
    {
        int red = 0;
        int green = 0;
        int blue = 0;
        for(Color[] elem : colorList)
        {
            for(int j = 0; j < elem.length; j++)
            {
                Color color = elem[j];
                red     += color.getRed();
                green   += color.getGreen();
                blue    += color.getBlue();
            }
        }

        red   = red   / (rasterSize * rasterSize);
        green = green / (rasterSize * rasterSize);
        blue  = blue  / (rasterSize * rasterSize);

        return new Color(red, green, blue);
    }


    @Override
    public String toString() {
        return "Raster{" +
                "x=" + x +
                ", y=" + y +
                ", colorList=" + colorList +
                ", rasterSize=" + rasterSize +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Raster raster = (Raster) o;
        return x == raster.x &&
                y == raster.y &&
                rasterSize == raster.rasterSize &&
                Objects.equals(colorList, raster.colorList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, colorList, rasterSize);
    }
}
