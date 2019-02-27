package Roboter.Labyrinth;

import java.util.Objects;
import java.util.function.Function;

public class Node
{
    private int x;
    private int y;
    private Node up;
    private Node down;
    private Node left;
    private Node right;

    private boolean isPath;
    private boolean visited;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;
        up = null;
        down = null;
        left = null;
        right = null;
        isPath = false;
        visited = false;
    }

    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void visited()
    {
        visited = true;
    }

    public boolean isVisited()
    {
        return visited;
    }

    public boolean isSame(Node other)
    {
        return !isBelow(other) && !isAbove(other);
    }

    public boolean isAbove(Node other)
    {
        return is((node) -> node.getUp(), other);
    }

    public boolean isBelow(Node other)
    {
        return is((node) -> node.getDown(), other);
    }

    private boolean is(Function<Node , Node> function, Node other)
    {
        int count;
        Node up = function.apply(this);
        for(count = 0; up != null; count++)
        {
            up = function.apply(up);
        }

        int countOther;
        up = function.apply(other);
        for(countOther = 0; up != null; countOther++)
        {
            up =  function.apply(up);
        }

        return count < countOther;
    }


    public boolean isPath()
    {
        return isPath;
    }

    public void setPath(boolean path)
    {
        isPath = path;
    }

    public boolean isBorder()
    {
        if(isCorner())
            return false;


        return (up == null || down == null || left == null || right == null) && !isPath;
    }

    public boolean isCorner()
    {
        boolean isCorner = false;
        //upper left Corner
        isCorner |= up == null && left == null;

        //upper rigth Corner
        isCorner |= up == null && right == null;

        //down left Corner
        isCorner |= down == null && left == null;

        //down rigth Corner
        isCorner |= down == null && right == null;

        return isCorner;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Node node = (Node) o;
        return isPath == node.isPath &&
                visited == node.visited &&
                up == node.up &&
                down == node.down &&
                left == node.left &&
                right == node.right;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(up, down, left, right, isPath, visited);
    }

    @Override
    public String toString()
    {
        return "Node{" +
                "isPath=" + isPath +
                ",isCorner=" +isCorner()+
                ",isBorder=" +isBorder()+
                '}';
    }
}
