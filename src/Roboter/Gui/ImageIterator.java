package Roboter.Gui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public abstract class ImageIterator implements Iterator<Raster> {

    private final BufferedImage image;
    private final int rasterSize;
    private int x;
    private int y;

    public enum Type{X,Y};
    public static ImageIterator getIterator(BufferedImage image, int rasterSize)
    {
        return new XIterator(image,rasterSize);
    }

    public static ImageIterator getIterator(Type type, BufferedImage image, int rasterSize)
    {
        switch (type)
        {
            case X:
                return new XIterator(image,rasterSize);
            case Y:
                return new YIterator(image,rasterSize);
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

    public float[] get_count_float() {
        float xcount = 0;
        float ycount = 0;

        if(image.getWidth()%rasterSize == 0)
            xcount = image.getWidth()/rasterSize;
        else
        {
            xcount = image.getWidth()/rasterSize;
            xcount += (image.getWidth()%rasterSize / 10);
        }
        if(image.getHeight()%rasterSize == 0)
            ycount = image.getHeight()/rasterSize;
        else
        {
            ycount = image.getHeight()/rasterSize;
            ycount += (image.getHeight()%rasterSize / 10);

        }

        float[] r = new float[1];
        r[0] = xcount;
        r[1] = ycount;

        return r;
    }

    public int get_count_int() {
        int xcount = 0;
        int ycount = 0;

        xcount = image.getWidth()/rasterSize;

        ycount = image.getHeight()/rasterSize;

        return xcount + ycount;

    }

    @Override
    public void remove()
    {
        //does nothing
    }

    @Override
    public void forEachRemaining(Consumer action) {
        //does nothing
    }

    private static class XIterator extends ImageIterator
    {
        private XIterator(BufferedImage image, int rasterSize)
        {
            super(image, rasterSize);
        }

        @Override
        public boolean hasNext() {
            return (super.y + super.rasterSize) < super.image.getHeight();
        }

        @Override
        public Raster next() {

            if(!hasNext())
                return null;

            List<Color[]> colorList = new ArrayList<>(super.rasterSize);

            for(int y = 0; y < super.rasterSize; y++)
            {
                Color[] colors = new Color[super.rasterSize];
                for(int x = 0; x < super.rasterSize; x++)
                {
                    int xCord = Math.min(super.x + x, super.image.getWidth()-1);
                    int yCord = Math.min(super.y + y, super.image.getWidth()-1);
                    colors[x] = new Color(super.image.getRGB(xCord, yCord));
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x, super.y, colorList, super.rasterSize);

            super.x += super.rasterSize;
            if(super.x > super.image.getWidth()) {
                super.x = 0;
                super.y += super.rasterSize;
            }

            return raster;
        }
    }

    private static class YIterator extends ImageIterator
    {
        private YIterator(BufferedImage image, int rasterSize)
        {
            super(image, rasterSize);
        }

        @Override
        public boolean hasNext() {
            return (super.x + super.rasterSize) < super.image.getWidth();
        }

        @Override
        public Raster next() {

            if(!hasNext())
                return null;

            List<Color[]> colorList = new ArrayList<>(super.rasterSize);

            for(int x = 0; x < super.rasterSize; x++)
            {
                Color[] colors = new Color[super.rasterSize];
                for(int y = 0; y < super.rasterSize; y++)
                {
                    int xCord = Math.min(super.x + x, super.image.getWidth()-1);
                    int yCord = Math.min(super.y + y, super.image.getHeight()-1);
                    colors[x] = new Color(super.image.getRGB(xCord, yCord));
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x, super.y, colorList, super.rasterSize);

            super.y += super.rasterSize;
            if(super.y > super.image.getHeight()) {
                super.y = 0;
                super.x += super.rasterSize;
            }

            return raster;
        }
    }
}
