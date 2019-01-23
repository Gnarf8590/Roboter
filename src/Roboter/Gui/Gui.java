package Roboter.Gui;

import Roboter.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Gui implements Runnable
{
    private BufferedImage original;
    private Path imageFile;

    public Gui(String file) throws IOException {
        imageFile = Paths.get(file);
        this.original = ImageIO.read(imageFile.toFile());
    }

    public Gui(BufferedImage original)
    {
        imageFile = Paths.get("image_Papier.jpg");
        this.original = original;
    }

    public void run()
    {
        BufferedImage image = ImageUtil.cutToSize(original);

        if(Main.DEBUG)
            writeImage(image, new File("original_cut.png"));
        
        //image = ImageUtil.reColor(image, false);

        if (Main.DEBUG)
            writeImage(image, new File("median_cut.png"));

        image = ImageUtil.reColor(image, true);

        if (Main.DEBUG)
            writeImage(image, new File("black_white_cut.png"));

        Frame frame = new Frame(image);
    }

    private BufferedImage loadImage(Path imageFile) throws IOException
    {
        try(InputStream in = Files.newInputStream(imageFile))
        {
            return ImageIO.read(in);
        }
    }

    private void writeImage(BufferedImage image, File file)
    {
        try
        {
            ImageIO.write(image,"png", file);
        }catch (IOException ioE)
        {

        }

    }
}
