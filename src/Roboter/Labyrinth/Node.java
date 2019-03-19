package Roboter.Labyrinth;

import java.util.Objects;

public class Node implements Comparable<Node>
{
    private int x;
    private int y;

    private Node up;
    private Node down;
    private Node left;
    private Node right;


    private Node prev;
    private int cost;
    private int prio;

    private boolean isPath;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.prev = null;
        this.up = null;
        this.down = null;
        this.left = null;
        this.right = null;
        isPath = false;
        cost = 0;
        prio = 0;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public int getCost()
    {
        return cost;
    }

    public void setCost(int cost)
    {
        this.cost = cost;
    }

    public boolean isPath()
    {
        return isPath;
    }

    public void setPath(boolean path)
    {
        isPath = path;
    }


    public void setPrev(Node prev)
    {
        this.prev = prev;
    }

    public Node getPrev()
    {
        return prev;
    }

    public Node getUp()
    {
        return up;
    }

    public void setUp(Node up)
    {
        this.up = up;
    }

    public Node getDown()
    {
        return down;
    }

    public void setDown(Node down)
    {
        this.down = down;
    }

    public Node getLeft()
    {
        return left;
    }

    public void setLeft(Node left)
    {
        this.left = left;
    }

    public Node getRight()
    {
        return right;
    }

    public void setRight(Node right)
    {
        this.right = right;
    }

    public void setPrio(int prio)
    {
        this.prio = prio;
    }

    public int getPrio()
    {
        return prio;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    @Override
    public String toString()
    {
        return "Node{" + "x=" + x + ", y=" + y + ", cost=" + cost + ", prio=" + prio + ", isPath=" + isPath + '}';
    }

    @Override
    public int compareTo(Node o)
    {
        int comp = Integer.compare(prio, o.prio);
        if(comp == 0)
            comp = Integer.compare(x, o.x);
        if(comp == 0)
            comp = Integer.compare(y, o.y);

        return comp;
    }
}

