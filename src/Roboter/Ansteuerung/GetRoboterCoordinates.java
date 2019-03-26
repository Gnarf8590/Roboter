package Roboter.Ansteuerung;

import Roboter.Coordinates;
import Roboter.Main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class GetRoboterCoordinates implements Control
{
	private FrameGrabber fgrabber;
	private RobotControl rcontrol;
	private List<Position> positions;

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

        positions = new ArrayList<>();
		
		for(Coordinates akt_koordinate : coordinates)
		{
			double target_x = startwert_x - (akt_koordinate.x * 0.36);
			double target_y = startwert_y + (akt_koordinate.y * 0.36);
			
			Position bsp_Position = new Position(target_x, target_y, 310, 180, 0, 50);
			if(Main.DEBUG) {
                System.out.println(akt_koordinate);
                System.out.println(bsp_Position);
                System.out.println();
            }

			//if(target_x < -220 || target_x > 244)
				//throw new IllegalArgumentException();
			//if(target_y < -590 || target_y > -329)
				//throw new IllegalArgumentException();

            positions.add(bsp_Position);
		}
	}

	@Override
	public void close() {
		try {
			rcontrol.close();
		} catch (IOException e) {
			;
		}
	}


	public void moveRobot()
    {

        try {
			sendPositions(positions);
        	rcontrol.moveZ(100);
        	try {
				Thread.sleep(500);
			}catch (InterruptedException intE)
			{}
            rcontrol.moveForCamera();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void sendPositions(List<Position> armcoordinates) throws IOException
	{

		for (Position armcoordinate : armcoordinates) {
			rcontrol.moveToPosition(armcoordinate);
		}

	}
}
