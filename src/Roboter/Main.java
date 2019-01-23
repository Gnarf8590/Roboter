package Roboter;

import Roboter.Gui.Gui;
import Roboter.Ansteuerung.ImageServer;
import Roboter.Ansteuerung.RoboterServer;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main
{
    //TRUE = Debug Messages
    public static final boolean DEBUG = true;
    private static final ExecutorService service = Executors.newCachedThreadPool();
    public static void main(String[] args) throws IOException {
        /*
        ImageServer imageServer = new ImageServer();
        Mat image = imageServer.readImage();

        BufferedImage bufferedImage = imageServer.convertToImage(image);

        if(DEBUG)
            imageServer.writeToFile(bufferedImage);

    */
        Gui gui = new Gui("image_Papier.jpg");
        gui.run();
        /*
        service.submit(gui);
        try {
            service.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {

        }
        */

/*
        try (RoboterServer roboterServer = new RoboterServer())
        {
            roboterServer.moveToStartPosition();
            roboterServer.waitBetweenMoves();
            roboterServer.moveForCamera();


        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }
}
