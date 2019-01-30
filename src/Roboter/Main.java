package Roboter;

import Roboter.Ansteuerung.Control;
import Roboter.DEBUG_KLASSEN.Dummy_Control;
import Roboter.DEBUG_KLASSEN.Dummy_Labyrinth;
import Roboter.Gui.Gui;
import Roboter.Labyrinth.LabyrinthControl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main
{
    //TRUE = Debug Messages
    public static final boolean DEBUG = false;
    public static void main(String[] args)
    {
        Control control = new Dummy_Control();
        LabyrinthControl labyrinthControl = new Dummy_Labyrinth();

        Gui gui = new Gui(control, labyrinthControl);
    }
}
