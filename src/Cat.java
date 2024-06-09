import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Cat
{
    private boolean appear;
    private int x;
    private int y;
    private int width;
    private int height;
    private String imageName;
    private Color color;
    private String mood;
    private Image catImage;

    // Getters & Setters
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Color getColor()
    {
        return color;
    }

    public String getMood()
    {
        return mood;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Cat(int x, int y, Color color, String mood)
    {
        this.x = x;
        this.y = y;
        this.color = color;
        this.mood = mood;
        width = 30;
        height = 10;

        int randomNum = (int)(Math.random() * (10 - 1 + 1)) + 1;
        this.imageName = "cat" + randomNum;
    }

    public void move(Graphics g)
    {
        int randomNum = (int)(Math.random() * (5 - 1 + 1)) + 1;

        try
        {
            if(randomNum == 1)
            {
                imageName = "cuddling_"+ imageName;
                ImageIO.read(new File(imageName));
                g.drawImage(catImage, x, y, width, height, null);
            }
            else if(randomNum == 2)
            {
                imageName = "sleeping_"+ imageName;
                ImageIO.read(new File(imageName));
                g.drawImage(catImage, x, y, width, height, null);
            }
            else if(randomNum == 3)
            {
                imageName = "cleaning_"+ imageName;
                ImageIO.read(new File(imageName));
                g.drawImage(catImage, x, y, width, height, null);
            }
            else if(randomNum == 4)
            {
                imageName = "chasing_"+ imageName;
                ImageIO.read(new File(imageName));
                g.drawImage(catImage, x, y, width, height, null);
            }
            else if(randomNum == 5)
            {
                imageName = "stretching_"+ imageName;
                ImageIO.read(new File(imageName));
                g.drawImage(catImage, x, y, width, height, null);
            }
            else
            {
                x+=5;
                y+=5;
                g.drawImage(catImage, x, y, width, height, null);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void render(Graphics g)
    {
        try
        {
            catImage = ImageIO.read(new File(imageName));
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        g.drawImage(catImage, x, y, width, height, null);
    }

    public boolean contains(int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
