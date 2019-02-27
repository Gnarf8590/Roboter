package Roboter.Labyrinth;

import Roboter.Coordinates;
import Roboter.ImageIterator;
import Roboter.ImageIterator.Type;
import Roboter.ImageUtil;
import Roboter.Raster;

import java.awt.image.BufferedImage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Labyrinth implements LabyrinthControl{

	public BufferedImage img;
	int rastersize = 5;
	private Coordinates start;
	private Coordinates end;
	
	private ImageIterator i_iterator;
	
	
	
	public Labyrinth() {

	}

	
	@Override
	public List<Coordinates> solve(BufferedImage image) {
		
		this.img = image;
		start = findStart();
		end = findEnd();
 		i_iterator = ImageIterator.getIterator(Type.Lab, img, rastersize);
		
		System.out.println(start.toString());
		System.out.println(end.toString());
		
		return new ArrayList<Coordinates>();
	}
	

	public boolean isLeftPath()
	{
		Raster t_raster = i_iterator.getNext();
		
		if( ImageUtil.greaterEqual(t_raster.getColor(), Color.WHITE))
			return true;
		else
			return false;
	}

	public boolean isRightPath()
	{
		Raster t_raster = i_iterator.getPrev();
		
		if( ImageUtil.greaterEqual(t_raster.getColor(),Color.WHITE))
			return true;
		else
			return false;
	}
	
	public boolean isUpPath()
	{
		Raster t_raster = i_iterator.getPrev();
		
		if( ImageUtil.greaterEqual(t_raster.getColor(),Color.WHITE))
			return true;
		else
			return false;
	}
	
	public boolean isDownPath()
	{
		Raster t_raster = i_iterator.getPrev();
		
		if( ImageUtil.greaterEqual(t_raster.getColor(),Color.WHITE))
			return true;
		else
			return false;
	}
	

	public Coordinates findStart() {
		ImageIterator start_iterator = ImageIterator.getIterator(Type.Lab, img, rastersize);
		
		 start = null;

		while(start_iterator.hasNext() && start == null) {
			Raster t_raster = start_iterator.next();
			if(	t_raster.getX() == 0 && ImageUtil.greaterEqual(t_raster.getColor(), Color.WHITE)
					|| t_raster.getY() == 0 && ImageUtil.greaterEqual(t_raster.getColor(), Color.WHITE))
			{
				start = new Coordinates(t_raster.getX(),t_raster.getY());			
			}
		}

		return start;
	}
	
	public Coordinates findEnd() {
		ImageIterator end_iterator = ImageIterator.getIterator(Type.Lab, img, rastersize);

		end = null;
		
		while(end_iterator.hasNext() && end == null && !end.equals(start)) {
			Raster t_raster = end_iterator.next();
			if(t_raster.getX() > (img.getWidth()-5) && ImageUtil.greaterEqual(t_raster.getColor(), Color.WHITE)
					|| t_raster.getY() == (img.getHeight()-5) && ImageUtil.greaterEqual(t_raster.getColor(), Color.WHITE)) {

				//Mit t_raster.getMiddle() würdest du den Mittelpunkt bekommen falls du willst
				//So bekommst den Anfangswert links oben
			
				end = new Coordinates(t_raster.getX(), t_raster.getY());
			}
		}
		
		return start;
	}



}
