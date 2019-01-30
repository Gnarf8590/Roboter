package Roboter;

import Roboter.Gui.Raster;

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

    public int[] getRasterCount()
    {
        int xcount = (image.getWidth()/rasterSize);
        int ycount = (image.getHeight()/rasterSize);

        if(image.getWidth() % rasterSize != 0)
            xcount++;

        if(image.getHeight() % rasterSize != 0)
            ycount++;


        int[] r = new int[2];
        r[0] = xcount;
        r[1] = ycount;

        return r;
    }

    public int getRasterCountTotal()
    {
        int xcount = image.getWidth()/rasterSize;
        int ycount = image.getHeight()/rasterSize;

        return xcount * ycount;
    }
    
    public Raster getNext() {return null;}
    
    public Raster getPrev() {return null;}
    
    public Raster getUp() {return null;}
    
    public Raster getDown() {return null;}
    
    public boolean hasAbove() {return false;}

    public boolean hasPrev() {return false;}

    public boolean hasBeneath() {return false;}

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
        public boolean hasNext()
        {
            return super.y < super.image.getHeight();
        }
        
        public boolean hasPrev()
        {
            return (super.x - super.rasterSize) > 0;
        }
        
        public boolean hasAbove()
        {
            return (super.y - super.rasterSize) > 0;
        }
        
        public boolean hasBeneath()
        {
            return (super.y + super.rasterSize) < super.image.getHeight();
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

                    //System.out.println("X:"+xCord+" Y:"+yCord);
                    colors[x] = new Color(super.image.getRGB(xCord, yCord));
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
        
        public Raster getNext() {

            if(!hasNext())
                return null;

            List<Color[]> colorList = new ArrayList<>(super.rasterSize);

            for(int y = 0; y < super.rasterSize; y++)
            {
                Color[] colors = new Color[super.rasterSize];
                for(int x = 0; x < super.rasterSize; x++)
                {
                    int xCord = Math.min(super.x + x, super.image.getWidth()-1);
                    int yCord = Math.min(super.y + y, super.image.getHeight()-1); 
                    
                    colors[x] = new Color(super.image.getRGB(xCord, yCord));
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x + super.rasterSize, super.y + super.rasterSize, colorList);
            
            return raster;
        }
        
        public Raster getPrev() {

            if(!hasPrev())
                return null;

            List<Color[]> colorList = new ArrayList<>(super.rasterSize);

            for(int y = 0; y < super.rasterSize; y++)
            {
                Color[] colors = new Color[super.rasterSize];
                for(int x = 0; x < super.rasterSize; x++)
                {
                    int xCord = Math.max(super.x - x, 0);
                    int yCord = Math.max(super.y - y, 0);
                    colors[x] = new Color(super.image.getRGB(xCord, yCord));
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x - super.rasterSize, super.y - super.rasterSize, colorList);
            
            return raster;
        }
        
        public Raster getUp() {

            if(!hasAbove())
                return null;

            List<Color[]> colorList = new ArrayList<>(super.rasterSize);

            for(int y = 0; y < super.rasterSize; y++)
            {
                Color[] colors = new Color[super.rasterSize];
                for(int x = 0; x < super.rasterSize; x++)
                {
                    int xCord = Math.max(super.x - x, 0);
                    int yCord = Math.max(super.y - y, 0);
                    colors[x] = new Color(super.image.getRGB(xCord, yCord));
                }
                colorList.add(colors);
            }

            Raster raster = new Raster(super.x - super.rasterSize, super.y - super.rasterSize, colorList);
            
            return raster;
        }
        
        public Raster getDown() {

            if(!hasBeneath())
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

            Raster raster = new Raster(super.x - super.rasterSize, super.y - super.rasterSize, colorList);
            
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
                    colors[y] = new Color(super.image.getRGB(xCord, yCord));
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
