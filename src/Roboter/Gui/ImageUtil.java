package Roboter.Gui;

import java.awt.*;
import java.awt.image.BufferedImage;

import static Roboter.Gui.ImageIterator.Type.X;
import static Roboter.Gui.ImageIterator.Type.Y;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;


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


        end = new Coordinates(start.x + end.x, start.y + end.y);

        System.out.println(end);
        return image.getSubimage(start.x, start.y, end.x-start.x, end.y-start.y);
    }

    private static Coordinates getStart(BufferedImage image, Color colorToFind)
    {
        ImageIterator iter = ImageIterator.getIterator(image,RASTER_SIZE);
        while (iter.hasNext())
        {
            Raster raster = iter.next();
            if(smaller(raster.getColor(), colorToFind))
            {
                return raster.getMiddle();
            }
        }

        return null;
    }

    private static Coordinates getEnd(BufferedImage image, Color colorToFind)
    {
        int counterY = 0;
        int END_Y = -1;
        int END_X = -1;

        ImageIterator imageIteratorY = ImageIterator.getIterator(Y, image, RASTER_SIZE);

        while (imageIteratorY.hasNext())
        {
            Raster raster = imageIteratorY.next();

            if(greater(raster.getColor(), colorToFind))
            {
                if(++counterY >= MAX_GAP)
                    END_Y = raster.getY();
            }
        }

        ImageIterator imageIteratorX = ImageIterator.getIterator(X, image, RASTER_SIZE);

        while (imageIteratorX.hasNext())
        {
            Raster raster = imageIteratorX.next();

            if(greater(raster.getColor(), colorToFind))
            {
                END_X = raster.getX();
            }
        }

        return new Coordinates(END_X, END_Y);
    }

    //ONLY FOR GREY COLORS
    public static boolean smaller(Color RGB, Color toMatch)
    {
        return RGB.getBlue() <= toMatch.getBlue() && RGB.getGreen() <= toMatch.getGreen() && RGB.getRed() <= toMatch.getRed();
    }

    //ONLY FOR GREY COLORS
    public static boolean greater(Color RGB, Color toMatch)
    {
        return RGB.getBlue() >= toMatch.getBlue() && RGB.getGreen() >= toMatch.getGreen() && RGB.getRed() >= toMatch.getRed();
    }


    public static BufferedImage reColor(BufferedImage image, boolean set)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        Color black = new Color(200,200,200);

        ImageIterator iter =ImageIterator.getIterator(image,RASTER_SIZE);
        while (iter.hasNext())
        {
            Raster raster = iter.next();
            Color color = raster.getColor();
            if(set)
            {
                if(smaller(raster.getColor(), black))
                    color = Color.BLACK;
                else
                    color = Color.WHITE;
            }
            for(int i = 0; i < RASTER_RECOLOR; i++)
            {
                for(int j = 0; j < RASTER_RECOLOR; j++)
                {
                    newImage.setRGB(raster.getX()+i,raster.getY()+j, color.getRGB());
                }
            }
        }

        return newImage;
    }
}
