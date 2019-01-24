package Roboter.Ansteuerung;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageServer
{
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private VideoCapture camera;

    public ImageServer()
    {
         camera = new VideoCapture("rtsp://127.0.0.1:8554/mystream");
    }

    public BufferedImage convertToImage(Mat src)
    {
        Mat destFrame = new Mat();
        Imgproc.cvtColor(src, destFrame, Imgproc.COLOR_RGB2GRAY, 0);

        // Create an empty image in matching format
        BufferedImage gray = new BufferedImage(destFrame.width(), destFrame.height(), BufferedImage.TYPE_BYTE_GRAY);

        //Copy data in BufferedImage
        byte[] data = ((DataBufferByte) gray.getRaster().getDataBuffer()).getData();
        destFrame.get(0, 0, data);
        return gray;
    }

    public Mat readImage()
    {
        if(camera.isOpened())
        {
            Mat srcFrame = new Mat();

            camera.read(srcFrame);
           return srcFrame;
        }else
            System.err.println("Kamera konnte nicht geöffnet werden");
        return null;
    }

    public void writeToFile(BufferedImage image)
    {
        File outputFile = new File("image.jpg");
        try {
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showVideo()
    {
        while (true)
        {
            Mat image = readImage();
            showResult(image);
        }
    }

    private void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
