package Roboter.Ansteuerung;

import Roboter.Coordinates;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Control
{
    public BufferedImage getImage();

    //lässt mich manuell ein Punkt auf Bild anfahren
    public void moveTo(Coordinates coordinates);

    public void setSolution(List<Coordinates> coordinates);
}
