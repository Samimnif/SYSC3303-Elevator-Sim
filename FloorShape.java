import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;

public class FloorShape {
    private int width;
    private int height;
    private int xPosition;
    private int yPosition;
    private String color;
    private boolean isVisible;
    private JFrame frame;
    private String Name;
    //private CanvasPane canvas;


    /**
     * Create a new square at default position with default color.
     */
    public FloorShape()
    {
        width = 500;
        height = 70;
        xPosition = 0;
        yPosition = 20;
        color = "blue";
        isVisible = false;
    }

    public FloorShape(String name)
    {
        width = 500;
        height = 70;
        xPosition = 0;
        yPosition = 20;
        color = "blue";
        isVisible = false;
        this.Name = name;
    }

    /**
     * Make this square visible. If it was already visible, do nothing.
     */
    public void makeVisible()
    {
        isVisible = true;
        draw();
    }

    /**
     * Make this square invisible. If it was already invisible, do nothing.
     */
    public void makeInvisible()
    {
        //erase();
        isVisible = false;
    }

    /**
     * Move the square a few pixels to the right.
     */
    public void moveRight()
    {
        moveHorizontal(20);
    }

    /**
     * Move the square a few pixels to the left.
     */
    public void moveLeft()
    {
        moveHorizontal(-20);
    }

    /**
     * Move the square a few pixels up.
     */
    public void moveUp()
    {
        moveVertical(-20);
    }

    /**
     * Move the square a few pixels down.
     */
    public void moveDown()
    {
        moveVertical(20);
    }

    /**
     * Move the square horizontally by 'distance' pixels.
     */
    public void moveHorizontal(int distance)
    {
        //erase();
        xPosition += distance;
        draw();
    }

    /**
     * Move the square vertically by 'distance' pixels.
     */
    public void moveVertical(int distance)
    {
        //erase();
        yPosition += distance;
        draw();
    }

    /**
     * Slowly move the square horizontally by 'distance' pixels.
     */
    public void slowMoveHorizontal(int distance)
    {
        int delta;

        if(distance < 0)
        {
            delta = -1;
            distance = -distance;
        }
        else
        {
            delta = 1;
        }

        for(int i = 0; i < distance; i++)
        {
            xPosition += delta;
            draw();
        }
    }

    /**
     * Slowly move the square vertically by 'distance' pixels.
     */
    public void slowMoveVertical(int distance)
    {
        int delta;

        if(distance < 0)
        {
            delta = -1;
            distance = -distance;
        }
        else
        {
            delta = 1;
        }

        for(int i = 0; i < distance; i++)
        {
            yPosition += delta;
            //draw();
        }
    }


    private void draw()
    {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                    new java.awt.Rectangle(xPosition, yPosition, width, height),this.Name);
            //canvas.wait(10);
        }
    }










    /**
     * Change the size to the new size (in pixels). Size must be >= 0.

     public void changeSize(int newWidth, int newHeight)
     {
     erase();
     width = newWidth;
     height = newHeight;
     draw();
     }
     */

    /**
     * Change the color. Valid colors are "red", "yellow", "blue", "green",
     * "magenta" and "black".

     public void changeColor(String newColor)
     {
     color = newColor;
     //draw();
     }
     */

    /**
     * Draw the square with current specifications on screen.

     private void draw()
     {
     if(isVisible) {
     Canvas canvas = Canvas.getCanvas();
     canvas.draw(this, color,
     new java.awt.Rectangle(xPosition, yPosition, width, height));
     canvas.wait(10);
     }
     }
     */

    /**
     * Erase the square on screen.

     private void erase()
     {
     if(isVisible) {
     Canvas canvas = Canvas.getCanvas();
     canvas.erase(this);
     }
     }
     */
}