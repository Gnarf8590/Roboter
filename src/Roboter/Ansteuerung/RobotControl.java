package Roboter.Ansteuerung;

import Roboter.Main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

public class RobotControl implements AutoCloseable
{
    private static final Position START = new Position(0, -500, 350, 180, 0, 50);
    private static final Position KAMERA = new Position(410.28, -35.86, 528.72, 180, 0, 50);
    private static final int WAIT_TIME = 500;

    private Socket socket;
    private OutputStream writer;
    private InputStream reader;

    public RobotControl()
    {
        try
        {
            System.out.println("Try opening Socket");
            socket = new Socket("192.168.1.223",10001);
            writer = socket.getOutputStream();
            reader = socket.getInputStream();
            enableCommunication();
            enableOperation();
            enableServo();
            override(50);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            //socket = new Socket("192.168.0.3",8888);

        } catch (IOException e)
        {
            System.err.println("Failed");
            e.printStackTrace();
            //ignore
        }
    }

    public void send(String cmd) throws IOException
    {
        if(Main.DEBUG)
            System.out.println("SEND: "+cmd);
        writer.write(cmd.getBytes(StandardCharsets.US_ASCII));
        //writer.newLine();
        //writer.flush();
    }

    public String receive() throws IOException
    {
        byte[] bytes = new byte[1024];
        reader.read(bytes);
        String string = new String(bytes);

        if(Main.DEBUG)
            System.out.println("RECEIVE: "+string);
        return string;
    }

    public String enableCommunication() throws IOException
    {
        send("1;1;OPEN=NARCUSR");
        return receive();
    }

    public String override(int speed) throws IOException
    {
        if(speed < 0)
             speed = 0;
        if (speed > 100)
            speed = 100;


        send("1;1;OVRD="+speed);
        return receive();
    }

    public String getState() throws IOException
    {
        send("1;1;STATE");
        return receive();
    }

    public String disableCommunication() throws IOException
    {
        send("1;1;CLOSE");
        return receive();
    }

    public String enableOperation() throws IOException
    {
        send("1;1;CNTLON");
        return receive();
    }

    public String disableOperation() throws IOException
    {
        send("1;1;CNTLOFF");
        return receive();
    }

    public String enableServo() throws IOException
    {
        send("1;1;SRVON");

        return receive();
    }

    public String startPump() throws IOException
    {
        send("1;1;OUT=5;1");

        return receive();
    }

    public String stopPump() throws IOException
    {
        send("1;1;OUT=5;0");

        return receive();
    }

    public String disableServo() throws IOException
    {
        send("1;1;SRVOFF");
        return receive();
    }

    public void moveZ(int count) throws IOException
    {
        Position pos = getCurrentPosition();
        pos.z += count;
        moveToPosition(pos);
    }

    public void moveX(int count) throws IOException
    {
        Position pos = getCurrentPosition();
        pos.x += count;
        moveToPosition(pos);
    }

    public void moveY(int count) throws IOException
    {
        Position pos = getCurrentPosition();
        pos.y += count;
        moveToPosition(pos);
    }

    public void moveForCamera() throws IOException
    {
        moveToPosition(KAMERA);
    }

    public void moveToPosition(Position position) throws IOException
    {
        Position positionToMove = position.clone();
        Position currentPosition = getCurrentPosition();

        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        String x = decimalFormat.format(positionToMove.x - currentPosition.x).replace(",",".");
        String y = decimalFormat.format(positionToMove.y - currentPosition.y).replace(",",".");
        String z = decimalFormat.format(positionToMove.z - currentPosition.z).replace(",",".");
        String a = decimalFormat.format(positionToMove.a - currentPosition.a).replace(",",".");
        String b = decimalFormat.format(positionToMove.b - currentPosition.b).replace(",",".");
        String c = decimalFormat.format(positionToMove.c - currentPosition.c).replace(",",".");
/*
        String x = String.valueOf(positionToMove.x - currentPosition.x);
        String y = String.valueOf(positionToMove.y - currentPosition.y);
        String z = String.valueOf(positionToMove.z - currentPosition.z);
        String a = String.valueOf(positionToMove.a - currentPosition.a);
        String b = String.valueOf(positionToMove.b - currentPosition.b);
        String c = String.valueOf(positionToMove.c - currentPosition.c);
*/

        send(String.format("1;1;EXECMOV P_CURR+(%s,%s,%s,%s,%s,%s)", x, y, z, a, b, c));
        receive();
        waitBetweenMoves();
    }

    public void moveToStartPosition() throws IOException
    {
        moveToPosition(START);
    }

    //XYZ position read
    public Position getCurrentPosition() throws IOException
    {
        send("1;1;PPOSF");

        String result = receive();

        result = result.substring(3);
        String[] split = result.split(";");


        double x = Double.parseDouble(split[1]);
        double y = Double.parseDouble(split[3]);
        double z = Double.parseDouble(split[5]);
        double a = Double.parseDouble(split[7]);
        double b = Double.parseDouble(split[9]);
        double c = Double.parseDouble(split[11]);

        return new Position(x, y, z, a, b, c);
    }

    public void waitBetweenMoves()
    {
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {

        }
    }

    public String enablePump() throws IOException
    {
        send("1;1;OUT=5;1");
        return receive();
    }

    public String disablePump() throws IOException
    {
        send("1;1;OUT=5;0");
        return receive();
    }


    @Override
    public void close() throws IOException
    {
        try {
            Thread.sleep(WAIT_TIME*2);
        }catch (InterruptedException intE)
        {}
        disableServo();
        disableCommunication();
        disableOperation();

        socket.close();
    }
}
