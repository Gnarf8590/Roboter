package Roboter.Gui;

import javax.swing.*;
import java.awt.image.BufferedImage;

public class Frame extends JFrame
{
    private BufferedImage image;
    private JPanel imagePanel;
    public Frame(BufferedImage image)
    {
        imagePanel = new JPanel();

        getContentPane().add(new JLabel(new ImageIcon(image)));

        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
