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
import java.util.Iterator;
import java.util.List;

import static Roboter.ImageIterator.Type.X;
import static Roboter.ImageIterator.Type.Y;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;


public class ImageUtil
{
    private static final int RASTER_SIZE = 5;
    private static final int MAX_GAP = 50;
    private static boolean DEBUG = false;

    public static BufferedImage cutToSize(BufferedImage image)
    {
        return cutToSize(image, RASTER_SIZE);
    }
    public static BufferedImage cutToSize(BufferedImage image, int rastersize)
    {
        if(DEBUG)
            writeImage(image, new File("before_cut.png"));

        Coordinates start = getStart(image, rastersize);

        BufferedImage cut = image.getSubimage(start.x, start.y, image.getWidth() - start.x, image.getHeight() - start.y);

        if(DEBUG)
            writeImage(cut, new File("cut.png"));

        Coordinates end = getEnd(cut, rastersize);

        end = new Coordinates(start.x + end.x, start.y + end.y);


        System.out.println(start);
        System.out.println(end);


        BufferedImage img = image.getSubimage(start.x, start.y, end.x-start.x, end.y-start.y); //fill in the corners of the desired crop location here
        BufferedImage copyOfImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = copyOfImage.createGraphics();
        g.drawImage(img, 0, 0, null);

        if(DEBUG)
            writeImage(copyOfImage, new File("complete_cut.png"));

        return copyOfImage;
    }

    public static Coordinates getStart(BufferedImage image, int rastersize)
    {
        ImageIterator iter = ImageIterator.getIterator(image, rastersize);
        while (iter.hasNext())
        {
            Raster raster = iter.next();
            if(BLACK.equals(raster.getColor()))
            {
                Coordinates start = raster.getMiddle();

                return new Coordinates(start.x +rastersize, start.y +rastersize);
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
        int lastBlackY = -1;
        while (imageIteratorY.hasNext())
        {
            Raster raster = imageIteratorY.next();

            if(WHITE.equals(raster.getColor()))
            {
                if(++counterY >= MAX_GAP / rastersize)
                {
                    END_Y = lastBlackY -rastersize;
                    break;
                }
            }else
                lastBlackY = raster.getY();
        }

        //Find Right Bottom Corner
        ImageIterator imageIteratorX = ImageIterator.getIterator(X, image, rastersize, END_Y);

        while (imageIteratorX.hasNext())
        {
            Raster raster = imageIteratorX.next();

            if(WHITE.equals(raster.getColor()))
            {
                END_X = raster.getX();
                break;
            }
        }

        return new Coordinates(END_X, END_Y);
    }

    public static BufferedImage rotate(BufferedImage image)
    {
        Coordinates start = null;
        Coordinates end = null;


        for(int y = 0; y < image.getHeight() && start == null; y++)
        {
            for(int x = 0; x < image.getWidth() && start == null; x++)
            {
                Color color = new Color(image.getRGB(x,y));
                if(color.equals(BLACK))
                    start = new Coordinates(x,y);
            }
        }

        List<Coordinates> coordinatesLeft = new ArrayList<>();
        //try left from start
        for(int x = start.x-1; x > 0; x--)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                Color color = new Color(image.getRGB(x,y));
                if(color.equals(BLACK))
                {
                    coordinatesLeft.add(new Coordinates(x, y));
                    break;
                }
            }
        }

        List<Coordinates> coordinatesRight = new ArrayList<>();
        //try right from start
        for(int x = start.x+1; x < image.getWidth(); x++)
        {
            for(int y = 0; y < image.getHeight(); y++)
            {
                Color color = new Color(image.getRGB(x,y));
                if(color.equals(BLACK))
                {
                    coordinatesRight.add(new Coordinates(x, y));
                    break;
                }
            }
        }

        List<Coordinates> coordinates;
        if(coordinatesLeft.size() > coordinatesRight.size())
            coordinates = coordinatesLeft;
        else
            coordinates = coordinatesRight;

        Coordinates last = coordinates.get(0);
        Iterator<Coordinates> iter = coordinates.iterator();
        while(iter.hasNext())
        {
            Coordinates curr = iter.next();
            if(last.distance(curr)> image.getHeight()/2) {
                iter.remove();
            }else
                last = curr;
        }

        end = coordinates.get(coordinates.size()-1);

        int height = end.y - start.y;
        int width = end.x - start.x;

        // Ankathete/Gegenkathete
//        double faktor = (double)height/width;
        double faktor = (double)height/width;

        double radian = Math.atan(faktor);

        radian *= -1;

        return rotate(image,radian);
    }

    private static BufferedImage rotate(BufferedImage image, double radian)
    {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage result = new BufferedImage(w, h, image.getType());
        Graphics2D g2 = result.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        AffineTransform transform = new AffineTransform();
        transform.rotate(radian, image.getWidth()/2, image.getHeight()/2);
        AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        result = op.filter(image, result);
        return result;
    }

    //ONLY FOR GREY COLORS
    private static boolean smallerEqual(Color RGB, Color toMatch)
    {
        return RGB.getBlue() <= toMatch.getBlue() && RGB.getGreen() <= toMatch.getGreen() && RGB.getRed() <= toMatch.getRed();
    }

    //ONLY FOR GREY COLORS
    private static boolean greaterEqual(Color RGB, Color toMatch)
    {
        return RGB.getBlue() >= toMatch.getBlue() && RGB.getGreen() >= toMatch.getGreen() && RGB.getRed() >= toMatch.getRed();
    }


    public static BufferedImage reColor(BufferedImage image)
    {
        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        Color black = new Color(220,220,220);

        ImageIterator iter = ImageIterator.getIterator(image, 1);
        while (iter.hasNext())
        {
            Raster raster = iter.next();

            Color color = raster.getColor();

            if(smallerEqual(color, black))
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

        if(DEBUG)
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
