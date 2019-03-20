package Roboter.Gui;

import Roboter.Coordinates;
import Roboter.ImageUtil;
import Roboter.Main;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.runAsync;

public class Frame extends JFrame
{
    private final CompleteControl completeControl;
    public static final int RASTERSIZE = 4;
    private BufferedImage original;
    private BufferedImage cut;
    private Coordinates startCoord;

    private List<Coordinates> userSolution;
    private boolean isUserPainting;

    private JLabel picLabel;
    private JPanel all;
    private JPanel imagePanel;
    private JButton grabFrame;
    private JButton solution;
    private JButton start;
    private JButton stop;
    private JButton reset;
    public Frame(CompleteControl completeControl)
    {
        this.completeControl = completeControl;
        userSolution = new ArrayList<>();

        picLabel = new JLabel();
        add(picLabel);

        imagePanel = new JPanel();
        imagePanel.add(picLabel);
        all = new JPanel();
        solution = new JButton("Finde Lösung");
        start = new JButton("Starte Roboter");
        stop = new JButton("Stoppe Roboter");
        reset = new JButton("Zurücksetzen");
        grabFrame = new JButton("Fotografiere Labyrinth");

        start.setEnabled(false);
        stop.setEnabled(false);
        reset.setEnabled(false);

        all.add(grabFrame);
        all.add(solution);
        all.add(start);
        all.add(reset);
        all.add(stop);


        BorderLayout layout = new BorderLayout();

        setLayout(layout);
        pack();
        setResizable(false);
        setSize(new Dimension(800, 650));
        add(imagePanel, BorderLayout.CENTER);
        add(all, BorderLayout.PAGE_END);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dimension.width/2-(getWidth()/2), dimension.height/2-(getHeight()/2));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initListener();
    }

    private void paintSolution(List<Coordinates> solution)
    {
        if(solution.size() == 0)
            return;

        BufferedImage solutionImage = ImageUtil.deepCopy(cut);
        Coordinates last = solution.get(0);

        Graphics2D g2d = solutionImage.createGraphics();
        Graphics2D g2dString = solutionImage.createGraphics();
        g2d.setColor(Color.RED);
        g2dString.setColor(Color.BLACK);
        BasicStroke bs = new BasicStroke(6);
        g2d.setStroke(bs);
        int radius = 5;

        for (Coordinates coordinates : solution)
        {
            g2d.drawLine(last.x, last.y, coordinates.x, coordinates.y);
            g2d.drawOval(coordinates.x -(radius/2) ,coordinates.y - (radius/2), radius, radius);
            last = coordinates;
        }


        if(Main.DEBUG)
        {
            int count = 1;
            for (Coordinates coordinates : solution)
            {
                g2dString.drawString(String.valueOf(count++), coordinates.x, coordinates.y);
            }
        }


        setMazeImage(solutionImage);

        if(Main.DEBUG)
            ImageUtil.writeImage(solutionImage,new File("solution.png"));
    }

    public void setMazeImage(BufferedImage image)
    {
        picLabel.setIcon(new ImageIcon(image));
    }

    private List<Coordinates> mapCoordinates(List<Coordinates> old)
    {
        if(old.size() == 0)
            return old;

        int mapX = startCoord.x;
        int mapY = startCoord.y;

        return old.stream().map(e -> new Coordinates(e.x+mapX, e.y +mapY)).collect(Collectors.toList());
    }

    private List<Coordinates> removeShort(List<Coordinates> old)
    {
        if(old.size() == 0)
            return  old;

        List<Coordinates> newCord = new ArrayList<>();
        newCord.add(old.get(0));
        Coordinates last = old.get(0);
        int skipped = 0;
        for (Coordinates coordinates : old)
        {
            if(last.distance(coordinates) > 50 || ++skipped % 10 == 0)
                newCord.add(coordinates);

            last = coordinates;
        }
        return newCord;
    }

    private void initListener()
    {
        imagePanel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e) {
                userSolution.clear();
                isUserPainting = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                isUserPainting = false;
                userSolution = removeShort(userSolution);
                paintSolution(userSolution);
                start.setEnabled(true);
            }
        });

        imagePanel.addMouseMotionListener(new MouseMotionAdapter() {
                                        @Override
                                        public void mouseDragged(MouseEvent e) {
                                            if(isUserPainting)
                                            {
                                                Point point = picLabel.getLocation();
                                                Coordinates newCord = new Coordinates(e.getX() - point.x, e.getY() -  point.y);
                                                userSolution.add(newCord);
                                            }
                                        }
                                    });
                grabFrame.addActionListener((e) ->
                    runAsync(() -> {
                        original = completeControl.getImage();

                        original = ImageUtil.reColor(original);
                        original = ImageUtil.rotate(original);
                        original = ImageUtil.reColor(original);

                        cut = ImageUtil.cutToSize(original, RASTERSIZE);
                        startCoord = ImageUtil.getStart(original, RASTERSIZE);

                        setMazeImage(cut);
                        start.setEnabled(true);
                    }));

        start.addActionListener(e -> runAsync(() ->
                                                                {
                                                                    start.setEnabled(false);
                                                                    stop.setEnabled(true);

                                                                    if (userSolution.size() > 0)
                                                                        completeControl.setSolution(mapCoordinates(userSolution));

                                                                    completeControl.moveRobot();
                                                                }
                                ));
        reset.addActionListener(e -> {
            userSolution.clear();
            setMazeImage(cut);
        });

        solution.addActionListener(e ->
            runAsync(() -> {
                reset.setEnabled(false);
                List<Coordinates> cords = completeControl.solve(cut);
                paintSolution(cords);
                completeControl.setSolution(mapCoordinates(cords));
            }
        ));

        stop.addActionListener(e ->
                               {
                                   completeControl.close();
                                   start.setEnabled(true);
                                   stop.setEnabled(false);
                               });

        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                completeControl.close();
            }
        });
    }
}
