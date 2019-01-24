package Roboter.Gui;

import java.awt.Color;
import java.util.List;

public class Raster
{
    private int x;
    private int y;

    private final List<Color[]> colors;
    private final int rasterSize;

    public Raster(int x, int y, List<Color[]> colors, int rasterSize)
    {
        this.x = x;
        this.y = y;
        this.colors = colors;
        this.rasterSize = rasterSize;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates getMiddle() {
        return new Coordinates(x/2, y/2);
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

        red   = red   / (rasterSize * rasterSize);
        green = green / (rasterSize * rasterSize);
        blue  = blue  / (rasterSize * rasterSize);

        return new Color(red, green, blue);
    }
}
