package Roboter.Gui;

import Roboter.Ansteuerung.Control;
import Roboter.Coordinates;
import Roboter.ImageUtil;
import Roboter.Labyrinth.LabyrinthControl;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.Collectors;

public class Frame extends JFrame
{
    private final CompleteControl completeControl;

    private BufferedImage original;
    private BufferedImage image;

    private JLabel picLabel;
    private JPanel all;
    private JPanel imagePanel;
    private JButton grabFrame;
    private JButton start;
    private JButton stop;
    public Frame(CompleteControl completeControl)
    {
        this.completeControl = completeControl;

        picLabel = new JLabel();
        add(picLabel);

        imagePanel = new JPanel();
        imagePanel.add(picLabel);
        all = new JPanel();
        start = new JButton("Start");
        stop = new JButton("Stop");
        grabFrame = new JButton("Hole Bild");

        start.setEnabled(false);
        stop.setEnabled(false);

        all.add(grabFrame);
        all.add(start);
        all.add(stop);


        BorderLayout layout = new BorderLayout();

        setLayout(layout);
        setResizable(false);
        setSize(800, 800);
        add(imagePanel, BorderLayout.CENTER);
        add(all, BorderLayout.PAGE_END);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initListener();
    }

    private void paintSolution(List<Coordinates> solution)
    {
        BufferedImage solutionImage = ImageUtil.deepCopy(image);
        Coordinates last = solution.get(0);

        Graphics2D g2d = solutionImage.createGraphics();
        g2d.setColor(Color.RED);
        BasicStroke bs = new BasicStroke(4);
        g2d.setStroke(bs);

        for (Coordinates coordinates : solution)
        {
            g2d.drawLine(last.x, last.y, coordinates.x, coordinates.y);
            last = coordinates;
        }
        setMazeImage(solutionImage);

    }

    private void setMazeImage(BufferedImage image)
    {
        picLabel.setIcon(new ImageIcon(image));
    }

    private List<Coordinates> mapCoordinates(List<Coordinates> old)
    {
        int mapX = original.getWidth() - image.getWidth();
        int mapY = original.getHeight() - image.getHeight();

        return old.stream().map(e -> new Coordinates(e.x+mapX, e.y +mapY)).collect(Collectors.toList());
    }

    private void initListener()
    {
        grabFrame.addActionListener((e) ->
                                    {
                                        grabFrame.setEnabled(false);
                                        original = completeControl.getImage();
                                        image = ImageUtil.reColor(original, true, 1);
                                        image = ImageUtil.cutToSize(image, 1);
                                        setMazeImage(image);
                                        start.setEnabled(true);
                                });

        start.addActionListener(e ->
                                {
                                    start.setEnabled(false);
                                    stop.setEnabled(true);
                                    List<Coordinates> solution = completeControl.solve(image);
                                    paintSolution(solution);
                                    completeControl.setSolution(mapCoordinates(solution));
                                });

        stop.addActionListener(e ->
                               {
                                   start.setEnabled(true);
                                   stop.setEnabled(false);
                               });
    }
}
