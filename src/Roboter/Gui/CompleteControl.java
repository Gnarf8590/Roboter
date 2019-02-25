package Roboter.Gui;

import Roboter.Ansteuerung.Control;
import Roboter.Coordinates;
import Roboter.Labyrinth.LabyrinthControl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class CompleteControl implements Control, LabyrinthControl
{
    private final Control control;
    private final LabyrinthControl labyrinthControl;

    public CompleteControl(Control control, LabyrinthControl labyrinthControl)
    {
        this.control = control;
        this.labyrinthControl = labyrinthControl;
    }

    @Override
    public BufferedImage getImage()
    {
        return control.getImage();
    }


    @Override
    public void moveTo(Coordinates coordinates)
    {
        control.moveTo(coordinates);
    }

    @Override
    public void setSolution(List<Coordinates> coordinates)
    {
        control.setSolution(coordinates);
    }

    @Override
    public void close() {
        control.close();
    }

    @Override
    public List<Coordinates> solve(BufferedImage image)
    {
        return labyrinthControl.solve(image);
    }
}
