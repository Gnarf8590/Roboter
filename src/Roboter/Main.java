package Roboter;

import Roboter.Ansteuerung.Control;
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
        Control control = new Dummy_Control();
        LabyrinthControl labyrinthControl = new Labyrinth();


        Gui gui = new Gui(control, labyrinthControl);
    }
}
