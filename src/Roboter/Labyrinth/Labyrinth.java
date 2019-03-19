package Roboter.Labyrinth;

import Roboter.Coordinates;
import Roboter.ImageIterator;

import java.awt.image.BufferedImage;
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
	public List<Coordinates> solve(BufferedImage image)
	{
		Transformer transformer = new Transformer(image, 2);
		Solver solver = new Solver(transformer.transform(), image);

		List<Node> solution = solver.solve();
		return transformer.transform(solution);
	}
}
