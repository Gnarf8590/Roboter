package Roboter.Gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.function.Consumer;

public class ImageIterator implements Iterator<Raster> {

    private final BufferedImage image;
    private final int rasterSize;
    private int x;
    private int y;
    public ImageIterator(BufferedImage image, int rasterSize)
    {
        this.image = image;
        this.rasterSize = rasterSize;

        x = 0;
        y = 0;
    }

    @Override
    public boolean hasNext() {
        return (x + rasterSize) < image.getWidth() && (y+rasterSize) < image.getHeight();
    }

    @Override
    public Raster next() {

        if(!hasNext())
            return null;

        Color[][] colors = new Color[rasterSize][rasterSize];

        for(int i = 0; i < rasterSize; i++)
        {
            for(int j = 0; j < rasterSize; j++)
            {

                colors[i][j] = new Color(image.getRGB(i+rasterSize, j +rasterSize));
            }
        }
        x += rasterSize;
        y += rasterSize;
        return new Raster(x, y, colors);
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
}
