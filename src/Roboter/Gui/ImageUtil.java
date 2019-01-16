package Roboter.Gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.opencv.highgui.HighGui.waitKey;

public class ImageUtil
{
    private static final Color BLACK_START = new Color(180,180,180);
    private static final int RASTER_SIZE = 5;
    private static final int RASTER_RECOLOR = 5;
    private static final int MAX_GAP = 50 / RASTER_SIZE;

    public static BufferedImage cutToSize(BufferedImage image)
    {
        Coordinates start = getStart(image, BLACK_START);
        if(start == null)
            throw new IllegalStateException("Image no black!!!!!!!!!!");
        BufferedImage cut = image.getSubimage(start.x, start.y, image.getWidth() - start.x, image.getHeight() - start.y);

        System.out.println(start);
        Coordinates end = getEnd(cut, BLACK_START);

        if(end == null)
            throw new IllegalStateException("Image no white!!!!!!!!!!");


        end = new Coordinates(start.x + end.x, start.y + end.y);

        System.out.println(end);
        return image.getSubimage(start.x, start.y, end.x-start.x, end.y-start.y);
    }

    private static Coordinates getStart(BufferedImage image, Color colorToFind)
    {
        int rasterSize = 5;

        for(int y = 0; y < image.getHeight(); y += rasterSize)
        {
            for(int x = 0; x < image.getWidth(); x+=rasterSize)
            {
                int red = 0;
                int green = 0;
                int blue = 0;

                for(int i = 0; i < rasterSize; i++)
                {
                    for(int j = 0; j < rasterSize; j++)
                    {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        red     += color.getRed();
                        green   += color.getGreen();
                        blue    += color.getBlue();
                    }
                }

                red   = red   / (RASTER_SIZE * RASTER_SIZE);
                green = green / (RASTER_SIZE * RASTER_SIZE);
                blue  = blue  / (RASTER_SIZE * RASTER_SIZE);

                Color median = new Color(red, green, blue);
                if(smaller(median, colorToFind))
                {
                    return new Coordinates(x+(rasterSize/2), y+(rasterSize/2));
                }
            }
        }
        return null;
    }

    private static Coordinates getEnd(BufferedImage image, Color colorToFind)
    {
        int counterY = 0;
        int END_Y = -1;
        int END_X = -1;

        for(int y = 0; y < image.getHeight() && END_Y == -1; y+= RASTER_SIZE)
        {
            int red = 0;
            int green = 0;
            int blue = 0;

            for(int i = 0; i < RASTER_SIZE; i++)
            {
                for(int j = 0; j < RASTER_SIZE; j++)
                {
                    Color color = new Color(image.getRGB(i, y + j));
                    red     += color.getRed();
                    green   += color.getGreen();
                    blue    += color.getBlue();
                }
            }

            red   = red   / (RASTER_SIZE * RASTER_SIZE);
            green = green / (RASTER_SIZE * RASTER_SIZE);
            blue  = blue  / (RASTER_SIZE * RASTER_SIZE);

            Color median = new Color(red, green, blue);

            if(greater(median, colorToFind))
            {
                if(++counterY >= MAX_GAP)
                    END_Y = y;
            }
        }

        int counterX = 0;
        for(int x = 0; x < image.getWidth() && END_X == -1; x += RASTER_SIZE)
        {
            int red = 0;
            int green = 0;
            int blue = 0;

            for(int i = 0; i < RASTER_SIZE; i++)
            {
                for(int j = 0; j < RASTER_SIZE; j++)
                {
                    Color color = new Color(image.getRGB(x + i, j));
                    red     += color.getRed();
                    green   += color.getGreen();
                    blue    += color.getBlue();
                }
            }

            red   = red   / (RASTER_SIZE * RASTER_SIZE);
            green = green / (RASTER_SIZE * RASTER_SIZE);
            blue  = blue  / (RASTER_SIZE * RASTER_SIZE);

            Color median = new Color(red, green, blue);
            if(greater(median, colorToFind))
            {
                END_X = x;
            }
        }
        return new Coordinates(END_X, END_Y);
    }

    //ONLY FOR GREY COLORS
    private static boolean smaller(Color RGB, Color toMatch)
    {
        return RGB.getBlue() <= toMatch.getBlue() && RGB.getGreen() <= toMatch.getGreen() && RGB.getRed() <= toMatch.getRed();
    }

    //ONLY FOR GREY COLORS
    private static boolean greater(Color RGB, Color toMatch)
    {
        return RGB.getBlue() >= toMatch.getBlue() && RGB.getGreen() >= toMatch.getGreen() && RGB.getRed() >= toMatch.getRed();
    }


    public static BufferedImage reColor(BufferedImage image, boolean set)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        Color black = new Color(200,200,200);
        for(int y = 0; y < image.getHeight(); y += RASTER_RECOLOR)
        {
            for (int x = 0; x < image.getHeight(); x += RASTER_RECOLOR)
            {
                int red = 0;
                int green = 0;
                int blue = 0;

                for(int i = 0; i < RASTER_RECOLOR; i++)
                {
                    for(int j = 0; j < RASTER_RECOLOR; j++)
                    {
                        Color color = new Color(image.getRGB(x + i, y + j));
                        red     += color.getRed();
                        green   += color.getGreen();
                        blue    += color.getBlue();
                    }
                }

                red   = red   / (RASTER_RECOLOR * RASTER_RECOLOR);
                green = green / (RASTER_RECOLOR * RASTER_RECOLOR);
                blue  = blue  / (RASTER_RECOLOR * RASTER_RECOLOR);

                Color color = new Color(red,green,blue);

                if(set)
                {
                    if(smaller(color, black))
                        color = Color.BLACK;
                    else
                        color = Color.WHITE;
                }
                for(int i = 0; i < RASTER_RECOLOR; i++)
                {
                    for(int j = 0; j < RASTER_RECOLOR; j++)
                    {
                        newImage.setRGB(x+i,y+j, color.getRGB());
                    }
                }
            }
        }
        return newImage;
    }
}
