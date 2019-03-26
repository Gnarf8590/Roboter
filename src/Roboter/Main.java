package Roboter;

import Roboter.Ansteuerung.Control;
import Roboter.Ansteuerung.GetRoboterCoordinates;
import Roboter.Ansteuerung.RobotControl;
import Roboter.DEBUG_KLASSEN.Dummy_Control;
import Roboter.Gui.Gui;
import Roboter.Labyrinth.Labyrinth;
import Roboter.Labyrinth.LabyrinthControl;

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
