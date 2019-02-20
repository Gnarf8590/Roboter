package Roboter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import static Roboter.ImageIterator.Type.X;
import static Roboter.ImageIterator.Type.Y;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public class ImageUtil
{
    private static final Color BLACK_START = new Color(180,180,180);
    private static final int RASTER_SIZE = 5;
    private static final int RASTER_RECOLOR = 5;
    private static final int MAX_GAP = 40;

    public static BufferedImage cutToSize(BufferedImage image)
    {
        return cutToSize(image, RASTER_SIZE);
    }
    public static BufferedImage cutToSize(BufferedImage image, int rastersize)
    {
        Coordinates start = getStart(image, rastersize);
        if(start == null)
            throw new IllegalStateException("Image no black!!!!!!!!!!");
        BufferedImage cut = image.getSubimage(start.x, start.y, image.getWidth() - start.x, image.getHeight() - start.y);

        Coordinates end = getEnd(cut, BLACK_START, rastersize);

        end = new Coordinates(start.x + end.x, start.y + end.y);

        System.out.println(start);
        System.out.println(end);

        BufferedImage img = image.getSubimage(start.x, start.y,end.x-start.x, end.y-start.y); //fill in the corners of the desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        return copyOfImage; //or use it however you want
        //return image.getSubimage(start.x, start.y, end.x-start.x, end.y-start.y);
    }

    public static Coordinates getStart(BufferedImage image, int rastersize)
    {
        ImageIterator iter = ImageIterator.getIterator(image,rastersize);
        while (iter.hasNext())
        {
            Raster raster = iter.next();
            if(smaller(raster.getColor(), BLACK_START))
            {
                return raster.getMiddle();
            }
        }

        return null;
    }

    private static Coordinates getEnd(BufferedImage image, Color colorToFind, int rastersize)
    {
        int counterY = 0;
        int END_Y = -1;
        int END_X = -1;

        ImageIterator imageIteratorY = ImageIterator.getIterator(Y, image, rastersize);

        while (imageIteratorY.hasNext())
        {
            Raster raster = imageIteratorY.next();

            if(greater(raster.getColor(), colorToFind))
            {
                if(++counterY >= MAX_GAP / rastersize)
                {
                    END_Y = raster.getY();
                    break;
                }
            }
        }

        ImageIterator imageIteratorX = ImageIterator.getIterator(X, image, rastersize);

        while (imageIteratorX.hasNext())
        {
            Raster raster = imageIteratorX.next();

            if(greater(raster.getColor(), colorToFind))
            {
                END_X = raster.getX();
                break;
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
        return reColor(image,set,RASTER_SIZE);
    }

    public static BufferedImage reColor(BufferedImage image, boolean set, int rasterSize)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        Color black = new Color(200,200,200);

        ImageIterator iter = ImageIterator.getIterator(image, rasterSize);
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
            for(int i = 0; i < raster.getRasterSizeY(); i++)
            {
                for(int j = 0; j < raster.getRasterSizeX(); j++)
                {
                    //System.out.println("X:"+(raster.getX() + j)+" Y:"+(raster.getY() + i));
                    newImage.setRGB(raster.getX() + j,raster.getY() + i, color.getRGB());
                }
            }
        }

        return newImage;
    }

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public static void writeImage(BufferedImage image, File file)
    {
        try
        {
            ImageIO.write(image,"png", file);
        }catch (IOException ioE)
        {

        }
    }
}
