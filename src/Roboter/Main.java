package Roboter;

import Roboter.Ansteuerung.Control;
import Roboter.Ansteuerung.GetRoboterCoordinates;
import Roboter.DEBUG_KLASSEN.Dummy_Control;
import Roboter.DEBUG_KLASSEN.Dummy_Labyrinth;
import Roboter.Gui.Gui;
import Roboter.Labyrinth.Labyrinth;
import Roboter.Labyrinth.LabyrinthControl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    //TRUE = Debug Messages
    public static final boolean DEBUG = false;
    public static void main(String[] args)
    {
        Control control = new GetRoboterCoordinates();
        LabyrinthControl labyrinthControl = new Labyrinth();

        Gui gui = new Gui(control, labyrinthControl);
    }
}
