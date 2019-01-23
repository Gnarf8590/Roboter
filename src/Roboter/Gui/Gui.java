package Roboter.Gui;

import Roboter.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class Gui implements Runnable
{
    private BufferedImage original;
    private BufferedImage cut;
    private Path imageFile;
    private Labyrinth labyrinth;
    public Gui()
    {
        try
        {
            original = ImageIO.read(new File("image_Papier"));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void run()
    {
        cut = ImageUtil.cutToSize(original);


        if(Main.DEBUG)
            writeImage(cut, new File("original_cut.png"));

        labyrinth = new Labyrinth(cut);
        List<Coordinates> path = labyrinth.run();
        path = mapCoordinates(path);



        writeImage(ImageUtil.reColor(cut, false), new File("cut.png"));
        writeImage(ImageUtil.reColor(cut, true), new File("black_white_cut.png"));

        Frame frame = new Frame(cut);
    }

    private List<Coordinates> mapCoordinates(List<Coordinates> old)
    {
        int mapX = original.getWidth() - cut.getWidth();
        int mapY = original.getHeight() - cut.getHeight();

        return old.stream().map(e -> new Coordinates(e.x+mapX, e.y +mapY)).collect(Collectors.toList());
    }

    public BufferedImage loadImage(Path imageFile) throws IOException
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
