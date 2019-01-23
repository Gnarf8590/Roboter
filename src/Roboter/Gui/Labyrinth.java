package Roboter.Gui;

import java.awt.image.BufferedImage;

import java.awt.*;

public class Labyrinth {

	public BufferedImage img;
	int rastersize = 5;
	ImageIterator i_iterator = ImageIterator.getIterator(img,rastersize);
	Color white = new Color(200,200,200);
	Color black = new Color(50,50,50);
	private Coordinates start;
	private Coordinates end;
		
	public Labyrinth(BufferedImage bufImg) {
		this.img = bufImg;
		start = findStart();
		end = findEnd();
		
		
		System.out.println(start.toString());
		System.out.println(end.toString());
	}
	
	
	public Coordinates findStart() {
		Coordinates start = null;
		
		while(i_iterator.hasNext() && start == null) {
			Raster t_raster = i_iterator.next();
			if(	t_raster.getX() == 0 && ImageUtil.greater(t_raster.getColor(), white)
					|| t_raster.getY() == 0 && ImageUtil.greater(t_raster.getColor(), white))
			{
				start = new Coordinates(t_raster.getX(),t_raster.getY());			
			}
		}

		return start;
	}
	
	public Coordinates findEnd() {
		Coordinates start = null;
		
		while(i_iterator.hasNext() && start == null) {
			Raster t_raster = i_iterator.next();
			if(t_raster.getX() > (img.getWidth()-5) && ImageUtil.greater(t_raster.getColor(), white) 
					|| t_raster.getY() == (img.getHeight()-5) && ImageUtil.greater(t_raster.getColor(), white)) {

				//Mit t_raster.getMiddle() würdest du den Mittelpunkt bekommen falls du willst
				//So bekommst den Anfangswert links oben
				start = new Coordinates(t_raster.getX(), t_raster.getY());
			}
		}
		
		return start;
	}

}
