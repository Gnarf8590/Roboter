package Roboter.Gui;

import Roboter.Ansteuerung.Control;

import Roboter.Labyrinth.LabyrinthControl;

public class Gui
{
    private Frame frame;

    public Gui(Control control, LabyrinthControl labyrinthControl)
    {
        frame = new Frame(new CompleteControl(control, labyrinthControl));
        frame.setVisible(true);
    }
}
