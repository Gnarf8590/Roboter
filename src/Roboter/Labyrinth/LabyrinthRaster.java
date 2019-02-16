package Roboter.Labyrinth;

import java.awt.Color;
import java.util.List;

import Roboter.Raster;

public class LabyrinthRaster extends Raster {

	private Raster up;
	private Raster down;
	private Raster left;
	private Raster right;
	
	
	public LabyrinthRaster(int x, int y, List<Color[]> colors) {
		super(x, y, colors);
		// TODO Auto-generated constructor stub
	}

	public Raster getUp() {
		return up;
	}

	public void setUp(Raster up) {
		this.up = up;
	}

	public Raster getDown() {
		return down;
	}

	public void setDown(Raster down) {
		this.down = down;
	}

	public Raster getLeft() {
		return left;
	}

	public void setLeft(Raster left) {
		this.left = left;
	}

	public Raster getRight() {
		return right;
	}

	public void setRight(Raster right) {
		this.right = right;
	}

}
