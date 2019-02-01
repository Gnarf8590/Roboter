package Roboter.Gui;

import Roboter.Ansteuerung.Control;
import Roboter.Coordinates;
import Roboter.Labyrinth.LabyrinthControl;

import java.awt.image.BufferedImage;
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
    public List<Coordinates> solve(BufferedImage image)
    {
        return labyrinthControl.solve(image);
    }
}
