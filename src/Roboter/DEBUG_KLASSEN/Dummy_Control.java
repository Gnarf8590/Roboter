package Roboter.DEBUG_KLASSEN;

import Roboter.Ansteuerung.Control;
import Roboter.Coordinates;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Dummy_Control implements Control
{
    private Path IMAGE_PATH = Paths.get("image_1.png");
    @Override
    public BufferedImage getImage()
    {
        try
        {
            return loadImage(IMAGE_PATH);
        } catch (IOException e)
        {
            return null;
        }
    }

    //DEBUG METHODS
    private BufferedImage loadImage(Path imageFile) throws IOException
    {
        try(InputStream in = Files.newInputStream(imageFile))
        {
            return ImageIO.read(in);
        }
    }

    @Override
    public void moveTo(Coordinates coordinates)
    {

    }

    @Override
    public void setSolution(List<Coordinates> coordinates)
    {

    }

    @Override
    public void close() {

    }
}
