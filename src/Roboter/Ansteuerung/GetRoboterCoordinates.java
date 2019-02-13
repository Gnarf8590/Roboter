package Roboter.Ansteuerung;

import Roboter.Coordinates;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetRoboterCoordinates implements Control
{
	private FrameGrabber fgrabber;
	private RobotControl rcontrol;

	public GetRoboterCoordinates() {
		fgrabber = new FrameGrabber();
		rcontrol = new RobotControl();
	}

    @Override
    public BufferedImage getImage()
	{
		try
		{
			rcontrol.moveForCamera();
			return fgrabber.readImage();
		} catch (IOException e)
		{
			return null;
		}
	}

	@Override
	public void moveTo(Coordinates coordinates) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSolution(List<Coordinates> coordinates) throws IllegalArgumentException
	{
		double startwert_x = 242;
		double startwert_y = -644;
				
		List<Position> al_ArmKoordinaten = new ArrayList<>();
		
		for(Coordinates akt_koordinate : coordinates)
		{
			double target_x = startwert_x - (akt_koordinate.x * 0.36);
			double target_y = startwert_y + (akt_koordinate.y * 0.36);
			
			Position bsp_Position = new Position(target_x, target_y, 350, 180, 0, 50);
			
			if(target_x < -220 || target_x > 244)
				throw new IllegalArgumentException();
			if(target_y < -590 || target_y > -329)
				throw new IllegalArgumentException();
			
			al_ArmKoordinaten.add(bsp_Position);
		}
		sendPositions(al_ArmKoordinaten);
	}
	
	private void sendPositions(List<Position> armcoordinates)
	{
    	try
		{
			for(Position akt_pos : armcoordinates) {
				rcontrol.moveToPosition(akt_pos);
			}
		}catch (IOException ioE)
		{

		}
	}
}
