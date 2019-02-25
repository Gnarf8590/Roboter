package Roboter.Labyrinth;

import Roboter.Coordinates;
import Roboter.ImageIterator;
import Roboter.Raster;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class Transformer
{
    private final BufferedImage image;
    private final int rasterSize;
    public Transformer(BufferedImage image, int rasterSize)
    {
        this.image = image;
        this.rasterSize = rasterSize;
    }

    public Node[][] transform()
    {
        ImageIterator imageIterator = ImageIterator.getIterator(image,rasterSize);
        int countX = image.getWidth()/rasterSize    + (image.getWidth()  % rasterSize == 0 ? 0 : 1);
        int countY = image.getHeight()/rasterSize   + (image.getHeight() % rasterSize == 0 ? 0 : 1);

        Node[][] nodes = new TransformableNode[countY][countX];

        for(int y = 0; y < nodes.length; y++)
        {
            for(int x = 0; x < nodes[y].length; x++)
            {
                nodes[y][x] = new TransformableNode(x*rasterSize,y*rasterSize);
            }
        }

        for(int y = 0; y < countY; y++)
        {
            for (int x = 0; x < countX; x++)
            {
                Node node = nodes[y][x];
                Raster raster = imageIterator.next();
                node.setPath(raster.getColor().equals(Color.WHITE));

                if(x > 0)
                    node.setLeft(nodes[y][x-1]);
                if(x < countX-1)
                    node.setRight(nodes[y][x+1]);

                if(y > 0)
                    node.setUp(nodes[y-1][x]);
                if(y < countY-1)
                    node.setDown(nodes[y+1][x]);

            }
        }
        try(BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("sdsa.txt"),CREATE,WRITE))
        {
            for (Node[] nodeY : nodes)
            {
                for (Node nodeX : nodeY)
                {
                    if (nodeX.isPath())
                        bufferedWriter.append(" ");
                    else
                        bufferedWriter.append("#");
                }
                bufferedWriter.newLine();
            }
        }catch (IOException ioE)
        {
            ioE.printStackTrace();
        }

        return nodes;
    }

    public List<Coordinates> transform(List<Node> nodeList)
    {
        return nodeList.stream().map(e -> (TransformableNode)e).map(e-> new Coordinates(e.getX(), e.getY())).collect(Collectors.toList());
    }

    private static class TransformableNode extends Node
    {
        private final int x;
        private final int y;
        private TransformableNode(int x, int y)
        {
            super();
            this.x = x;
            this.y = y;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            if (!super.equals(o))
                return false;

            TransformableNode that = (TransformableNode) o;
            return x == that.x &&
                    y == that.y;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(super.hashCode(), x, y);
        }

        @Override
        public String toString()
        {
            return "TransformableNode{" +
                    "x=" + x +
                    ", y=" + y +
                    ", "+super.toString()+
                    '}';
        }
    }
}
