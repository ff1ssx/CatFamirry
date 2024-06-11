import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Customer
{
    private int x;
    private int y;
    private int width;
    private int height;
    private double satisfaction;
    private double moneySpent;
    private double patience;
    private Image customerImage;

    public Customer(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 150;
        satisfaction = 0.0;
        patience = 10.0;
        moneySpent = 0.0;
    }

    public void render(Graphics g)
    {
        int randomNum = (int)(Math.random() * (2 - 1 + 1)) + 1;
        if(randomNum == 1)
        {
            try
            {
                customerImage = ImageIO.read(new File("female_customer.png"));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            try
            {
                customerImage = ImageIO.read(new File("male_customer.png"));
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        g.drawImage(customerImage, x, y, width, height, null);
    }

    public void decreasePatience()
    {
        patience -= 0.5;
    }

    public void leave()
    {
        while(x <= 600 && y <= 800)
        {
            x+=5;
            y+=5;
        }
    }

    public void interact(Item item)
    {
        satisfaction += 0.5;
    }
}