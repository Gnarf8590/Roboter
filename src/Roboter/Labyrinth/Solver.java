package Roboter.Labyrinth;

import Roboter.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.*;

public class Solver
{
    private Node[][] nodes;
    private TreeSet<Node> openList = new TreeSet<>();
    private Set<Node> closedList = new HashSet<>();
    private BufferedImage image;


    private static final int COST = 10;
    public Solver(Node[][] nodes, BufferedImage image)
    {
        this.nodes = nodes;
        this.image = image;
    }


    public List<Node> solve()
    {
        Node start = getStart();
        Node end = getEnd();

        if (start == null || end == null)
            throw new RuntimeException("No Start|End");

        openList.add(start);

        Node current = null;
        while (!openList.isEmpty())
        {
            current = openList.pollFirst();

            if (current == end)
                break;

            closedList.add(current);

            expandNode(current);
        }
        ImageUtil.writeImage(image, new File("sadsa.png"));
        List<Node> path = new ArrayList<>();
        //if(current == end)
        {
            Node node = current;
            while (node != null)
            {
                path.add(node);
                node = node.getPrev();
            }
        }
        Collections.reverse(path);



        List<Node> toRemove = new ArrayList<>();

        Node last = path.get(0);
        for(int i = 1; i < path.size(); i++)
        {
            Node node = path.get(i);

            if(node.getX() == last.getX())
            {
                if (i + 1 < path.size() && path.get(i + 1).getX() == node.getX())
                {
                    toRemove.add(node);
                }
                else
                    last = node;
            }
            else last = node;
        }


        last = path.get(0);
        for(int i = 1; i < path.size(); i++)
        {
            Node node = path.get(i);

            if(node.getY() == last.getY())
            {
                if (i + 1 < path.size() && path.get(i + 1).getY() == node.getY())
                {
                    toRemove.add(node);
                }
                else
                    last = node;
            }
            else last = node;
        }


        path.removeAll(toRemove);

        return path;
    }

    private void expandNode(Node current)
    {
        List<Node> next = new ArrayList<>();
        next.add(current.getUp());
        next.add(current.getDown());
        next.add(current.getLeft());
        next.add(current.getRight());

        for (Node node : next)
        {
            if(node == null || !node.isPath())
                continue;

            if(closedList.contains(node))
                continue;

            int cost = current.getCost() + COST;

            if(cost >= node.getCost() && openList.contains(node))
                continue;

            node.setPrev(current);
            node.setCost(cost);


            node.setPrio(cost + node.getPrio());

            //showCurrent(node);
            openList.add(node);
        }

        //Resort because of possible Prio update
        openList = new TreeSet<>(openList);
    }

    private void showCurrent(Node node)
    {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.RED);
        BasicStroke bs = new BasicStroke(6);
        g2d.setStroke(bs);
        g2d.fillRect(node.getX(), node.getY(), 2, 2);
//
//        try
//        {
//            Thread.sleep(500);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
    }

    private Node getStart()
    {
        int startY = -1;
        for(int y = 0; y < nodes.length; y++)
        {
            if(nodes[y][0].isPath() && startY == -1)
            {
                startY = y;
            }

            if(!nodes[y][0].isPath() && startY != -1)
            {
                int diff = (y-1) - startY;
                diff /=2;
                return nodes[startY+diff][0];
            }
        }
        return null;
    }

    private Node getEnd()
    {
        int startY = -1;
        for(int y = 20; y < nodes.length; y++)
        {
            if(nodes[y][nodes[y].length-1].isPath() && startY == -1)
            {
                startY = y;
            }

            if(!nodes[y][nodes[y].length-1].isPath() && startY != -1)
            {
                int diff = (y-1) - startY;
                diff /=2;
                return nodes[startY+diff][nodes[y].length-1];
            }
        }
        return null;
    }
}
