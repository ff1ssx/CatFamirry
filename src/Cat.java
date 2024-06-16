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
    private String type;
    private int price;

    // Getters & Setters
    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public String getMood()
    {
        return mood;
    }

    public int getPrice() {
        return price;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Cat(String type, Color color, String mood, int x, int y, int price)
    {
        this.type = type;
        this.mood = mood;
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.price = price;
        loadImage();
    }

    private void loadImage()
    {
        try
        {
            switch (type) {
                case "Bombay":
                    catImage = ImageIO.read(new File("Bombay.png"));
                    break;
                case "Orange":
                    catImage = ImageIO.read(new File("Orange.png"));
                    break;
                case "Tabby":
                    catImage = ImageIO.read(new File("Tabby.png"));
                    break;
                case "White":
                    catImage = ImageIO.read(new File("White.png"));
                    break;
                case "British Shorthair":
                    catImage = ImageIO.read(new File("British Shorthair.png"));
                    break;
                case "Maine Coon":
                    catImage = ImageIO.read(new File("Maine Coon.png"));
                    break;
                case "Ragdoll":
                    catImage = ImageIO.read(new File("Ragdoll.png"));
                    break;
                case "American Shorthair":
                    catImage = ImageIO.read(new File("American Shorthair.png"));
                    break;
                case "Siamese":
                    catImage = ImageIO.read(new File("Siamese.png"));
                    break;
                case "Calico":
                    catImage = ImageIO.read(new File("Calico.png"));
                    break;
                case "Li Hua":
                    catImage = ImageIO.read(new File("Li Hua.png"));
                    break;
                case "Russian Blue":
                    catImage = ImageIO.read(new File("Russian Blue.png"));
                    break;
                case "Balinese":
                    catImage = ImageIO.read(new File("Balinese.png"));
                    break;
                case "Persian":
                    catImage = ImageIO.read(new File("Persian.png"));
                    break;
                case "RagaMuffin":
                    catImage = ImageIO.read(new File("RagaMuffin.png"));
                    break;
                default:
                    catImage = null;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void render(Graphics g)
    {
        if (catImage != null)
        {
            g.drawImage(catImage, x, y, width, height, null);
        }
        else
        {
            g.setColor(color);
            g.fillRect(x, y, width, height); // Placeholder
        }
    }

    public boolean contains(int mouseX, int mouseY)
    {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
