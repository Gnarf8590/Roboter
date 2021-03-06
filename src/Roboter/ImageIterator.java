package Roboter;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class ImageIterator implements Iterator<Raster> {

    private final BufferedImage image;
    private final int rasterSize;
    private int x;
    private int y;

    public enum Type{X,Y};
    public static ImageIterator getIterator(BufferedImage image, int rasterSize)
    {
        return new XIterator(image,rasterSize,0);
    }
    public static ImageIterator getIterator(Type type, BufferedImage image, int rasterSize)
    {
        return getIterator(type,image,rasterSize,0);
    }

    public static ImageIterator getIterator(Type type, BufferedImage image, int rasterSize, int start)
    {
        switch (type)
        {
            case X:
                return new XIterator(image, rasterSize, start);
            case Y:
                return new YIterator(image, rasterSize, start);
            default:
                    throw new IllegalStateException("Wrong Type");
        }
    }

    protected ImageIterator(BufferedImage image, int rasterSize)
    {
        this.image = image;
        this.rasterSize = rasterSize;

        x = 0;
        y = 0;
    }

    protected Color getRGB(int x, int y)
    {
        Color color = new Color(image.getRGB(x,y));
        return color;
    }


    private static class XIterator extends ImageIterator
    {
        private XIterator(BufferedImage image, int rasterSize, int start)
        {
            super(image, rasterSize);
            super.y = start;
        }

        @Override
        public boolean hasNext()
        {
            return super.y < super.image.getHeight();
        }

        @Override
        public Raster next() {

            if(!hasNext())
                return null;

            int currRasterSizeX = Math.min(super.rasterSize, super.image.getWidth() - super.x);
            int currRasterSizeY = Math.min(super.rasterSize, super.image.getHeight() - super.y);

            List<Color[]> colorList = new ArrayList<>(currRasterSizeY);

            for(int y = 0; y < currRasterSizeY; y++)
            {
                Color[] colors = new Color[currRasterSizeX];
                for(int x = 0; x < colors.length; x++)
                {
                    int xCord = Math.min(super.x + x, super.image.getWidth() - 1);
                    int yCord = Math.min(super.y + y, super.image.getHeight() - 1);

                    colors[x] = getRGB(xCord,yCord);
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x, super.y, colorList);

            super.x += currRasterSizeX;
            if(super.x == super.image.getWidth()) {
                super.x = 0;
                super.y += currRasterSizeY;
            }

            return raster;
        }
    }

    private static class YIterator extends ImageIterator
    {
        private YIterator(BufferedImage image, int rasterSize, int start)
        {
            super(image, rasterSize);
            super.x = start;
        }

        @Override
        public boolean hasNext() {
            return super.x < super.image.getWidth();
        }

        @Override
        public Raster next() {

            if(!hasNext())
                return null;


            int currRasterSizeX = Math.min(super.rasterSize, super.image.getWidth() - super.x);
            int currRasterSizeY = Math.min(super.rasterSize, super.image.getHeight() - super.y);

            List<Color[]> colorList = new ArrayList<>(currRasterSizeY);

            for(int x = 0; x < currRasterSizeY; x++)
            {
                Color[] colors = new Color[currRasterSizeX];
                for(int y = 0; y < currRasterSizeX; y++)
                {
                    int xCord = Math.min(super.x + x, super.image.getWidth()-1);
                    int yCord = Math.min(super.y + y, super.image.getHeight()-1);
                    colors[y] = getRGB(xCord, yCord);
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x, super.y, colorList);

            super.y += currRasterSizeY;
            if(super.y == super.image.getHeight()) {
                super.y = 0;
                super.x += currRasterSizeX;
            }

            return raster;
        }
    }
}
