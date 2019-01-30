package Roboter.Ansteuerung;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class FrameGrabber
{
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private VideoCapture camera;

    public FrameGrabber()
    {
         camera = new VideoCapture("rtsp://127.0.0.1:8554/mystream");
    }

    private BufferedImage convertToImage(Mat src)
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

    public BufferedImage readImage()
    {
        if(camera.isOpened())
        {
            Mat srcFrame = new Mat();

            camera.read(srcFrame);
           return convertToImage(srcFrame);
        }else
            System.err.println("Kamera konnte nicht geöffnet werden");
        return null;
    }
}
