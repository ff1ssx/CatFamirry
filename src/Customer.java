import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Customer {
    private int x, y;
    private int satisfaction;
    private int imageIndex;
    private boolean hasPaid;
    private boolean isPaused;
    private int tileSize;
    private int screenWidth, screenHeight;
    private Random random;
    private BufferedImage image;

    public Customer(int x, int y, int satisfaction, int imageIndex, int tileSize, int screenWidth, int screenHeight) {
        this.x = x;
        this.y = y;
        this.satisfaction = satisfaction;
        this.imageIndex = imageIndex;
        this.tileSize = tileSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.hasPaid = false;
        this.isPaused = false;
        this.random = new Random();
        loadImage();
    }

    private void loadImage() {
        try {
            BufferedImage rawImage = ImageIO.read(new File("customer" + imageIndex + ".png"));
            image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(rawImage, 0, 0, tileSize, tileSize, null);
            g2d.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, tileSize, tileSize); // Placeholder
        }

        g.setColor(Color.BLACK);
        g.drawString(String.valueOf(satisfaction), x + 20, y + 30);
    }

    public void move(ArrayList<Item> items) {
        if (!isPaused && satisfaction > 0) {
            int dx = random.nextInt(3) - 1;
            int dy = random.nextInt(3) - 1;

            int newX = x + dx * tileSize;
            int newY = y + dy * tileSize;

            if (newY >= tileSize && !collidesWithItems(newX, newY, items)) {
                x = newX;
                y = newY;
            }

            if(newX >= 700)
            {
                if(newY < 550)
                {
                    x = newX;
                }
                else
                {
                    int lineNum = (int)(Math.random() * (2 - 1 + 1)) + 1;

                    if(lineNum == 1)
                    {
                        while(y > 100)
                        {
                            y-=50;
                        }
                    }
                    else if(lineNum == 2)
                    {
                        x = 750;

                        while(y > 100)
                        {
                            y -= 50;
                        }
                    }
                }
            }

            if (random.nextInt(10) == 0) {
                satisfaction -= 1;
            }
        }
    }

    public void moveToEntrance() {
        if (x < 375 / tileSize * tileSize) {
            x += tileSize;
        } else if (x > 375 / tileSize * tileSize) {
            x -= tileSize;
        } else if (y > tileSize) {
            y -= tileSize;
        }
    }

    public boolean hasLeft() {
        return x == 375 / tileSize * tileSize && y == tileSize && satisfaction <= 0;
    }

    public void leaveCafe() {
        x = -1;
        y = -1;
    }

    private boolean collidesWithItems(int x, int y, ArrayList<Item> items) {
        for (Item item : items) {
            if (item.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public void interactWithItem() {
        satisfaction += 5;
    }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getSatisfaction() { return satisfaction; }
    public void setSatisfaction(int satisfaction) { this.satisfaction = satisfaction; }
    public boolean hasPaid() { return hasPaid; }
    public void setHasPaid(boolean hasPaid) { this.hasPaid = hasPaid; }
    public boolean isPaused() { return isPaused; }
    public void setPaused(boolean isPaused) { this.isPaused = isPaused; }
}
