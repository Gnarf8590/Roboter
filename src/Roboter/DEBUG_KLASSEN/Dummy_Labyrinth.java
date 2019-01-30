package Roboter.DEBUG_KLASSEN;

import Roboter.Coordinates;
import Roboter.Labyrinth.LabyrinthControl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Dummy_Labyrinth implements LabyrinthControl
{
    @Override
    public List<Coordinates> solve(BufferedImage image)
    {
        List<Coordinates> solution = new ArrayList<>();
        solution.add(new Coordinates(200,200));
        solution.add(new Coordinates(250,200));
        solution.add(new Coordinates(250,250));
        solution.add(new Coordinates(300,250));
        solution.add(new Coordinates(350,250));

        return solution;
    }
}
