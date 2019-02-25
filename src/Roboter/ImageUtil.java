package Roboter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Roboter.ImageIterator.Type.X;
import static Roboter.ImageIterator.Type.Y;
import static java.awt.Color.BLACK;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public class ImageUtil
{
    private static final int RASTER_SIZE = 5;
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

        writeImage(cut, new File("cut.png"));
        Coordinates end = getEnd(cut, rastersize);

        end = new Coordinates(start.x + end.x, start.y + end.y);

        System.out.println(start);
        System.out.println(end);

        BufferedImage img = image.getSubimage(start.x, start.y, end.x-start.x, end.y-start.y); //fill in the corners of the desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        return copyOfImage; //or use it however you want
        //return image.getSubimage(start.x, start.y, end.x-start.x, end.y-start.y);
    }

    public static Coordinates getStart(BufferedImage image, int rastersize)
    {
        ImageIterator iter = ImageIterator.getIterator(image, rastersize);
        while (iter.hasNext())
        {
            Raster raster = iter.next();
            if(smaller(raster.getColor(), BLACK))
            {
                return raster.getMiddle();
            }
        }

        return null;
    }

    private static Coordinates getEnd(BufferedImage image, int rastersize)
    {
        int counterY = 0;
        int END_Y = -1;
        int END_X = -1;

        //Find left Bottom Corner
        ImageIterator imageIteratorY = ImageIterator.getIterator(Y, image, rastersize);

        while (imageIteratorY.hasNext())
        {
            Raster raster = imageIteratorY.next();

            if(greater(raster.getColor(), BLACK))
            {
                if(++counterY >= MAX_GAP / rastersize)
                {
                    END_Y = raster.getY();
                    break;
                }
            }
        }

        //Find Right Bottom Corner
        ImageIterator imageIteratorX = ImageIterator.getIterator(X, image, rastersize, END_Y-rastersize*2);

        while (imageIteratorX.hasNext())
        {
            Raster raster = imageIteratorX.next();

            if(greater(raster.getColor(), BLACK))
            {
                END_X = raster.getX();
                break;
            }
        }

        return new Coordinates(END_X, END_Y);
    }

    public static BufferedImage rotate(BufferedImage image)
    {
        double radian = 0;

        ImageIterator imageIterator = ImageIterator.getIterator(image,1);

        List<Integer> distance = new ArrayList<>();
        int counter = 0;
        while (imageIterator.hasNext())
        {
            Raster raster = imageIterator.next();

            if(BLACK.equals(raster.getColor()))
            {
                distance.add(counter);
                counter = 0;
                while (imageIterator.hasNext() && imageIterator.next().getX() != 0)
                {
                    ;
                }
            }
            counter++;

        }


        return rotate(image,radian);
    }

    private static BufferedImage rotate(BufferedImage image, double radian)
    {
        AffineTransform transform = new AffineTransform();
        transform.rotate(radian, image.getWidth()/2, image.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);
        return image;
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

    public static BufferedImage reColor(BufferedImage image)
    {
        return reColor(image,RASTER_SIZE);
    }

    public static BufferedImage reColor(BufferedImage image, int rasterSize)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        Color black = new Color(175,175,175);

        ImageIterator iter = ImageIterator.getIterator(image, rasterSize);
        while (iter.hasNext())
        {
            Raster raster = iter.next();

            Color color = raster.getColor();

            if(smaller(color, black))
                color = BLACK;
            else
                color = Color.WHITE;

            for(int i = 0; i < raster.getRasterSizeY(); i++)
            {
                for(int j = 0; j < raster.getRasterSizeX(); j++)
                {
                    //System.out.println("X:"+(raster.getX() + j)+" Y:"+(raster.getY() + i));
                    newImage.setRGB(raster.getX() + j,raster.getY() + i, color.getRGB());
                }
            }
        }

        writeImage(newImage, new File("BLACK_WHITE.png"));
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
