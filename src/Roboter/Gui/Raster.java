package Roboter.Gui;

import java.awt.*;

public class Raster
{
    private int x;
    private int y;
    private Color[][] colors;

    public Raster(int x, int y,Color[][] colors)
    {
        this.x = x;
        this.y = y;
        this.colors = colors;
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
        for(int i = 0; i < colors.length; i++)
        {
            for(int j = 0; j < colors[i].length; j++)
            {
                Color color = colors[i][j];
                red     += color.getRed();
                green   += color.getGreen();
                blue    += color.getBlue();
            }
        }

        red   = red   / (colors.length * colors.length);
        green = green / (colors.length * colors.length);
        blue  = blue  / (colors.length * colors.length);

        return new Color(red, green, blue);
    }
}
