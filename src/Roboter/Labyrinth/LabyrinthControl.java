package Roboter.Labyrinth;

import Roboter.Coordinates;

import java.awt.image.BufferedImage;
import java.util.List;

public interface LabyrinthControl
{
    public List<Coordinates> solve(BufferedImage image);
}
