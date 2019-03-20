package Roboter.Labyrinth;

import Roboter.Coordinates;
import Roboter.Gui.Frame;


import java.awt.image.BufferedImage;
import java.util.List;

public class Labyrinth implements LabyrinthControl
{

	public Labyrinth() {

	}

	
	@Override
	public List<Coordinates> solve(BufferedImage image)
	{
		Transformer transformer = new Transformer(image, Frame.RASTERSIZE);
		Solver solver = new Solver(transformer.transform(), image);

		List<Node> solution = solver.solve();
		return transformer.transform(solution);
	}
}
