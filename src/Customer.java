import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Customer {
    private int x, y;
    private int targetX, targetY;
    private int satisfaction;
    private int imageIndex;
    private boolean hasPaid;
    private boolean isPaused;
    private int tileSize;
    private int screenWidth, screenHeight;
    private Random random;
    private BufferedImage image;
    Font sherryFont;
    private static final int MOVE_STEPS = 5;
    private int stepsRemaining;

    public Customer(int x, int y, int satisfaction, int imageIndex, int tileSize, int screenWidth, int screenHeight) {
        try
        {
            sherryFont = Font.createFont(Font.TRUETYPE_FONT, new File("Neucha-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(sherryFont);
        }
        catch (IOException | FontFormatException e)
        {
            e.printStackTrace();
        }

        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.satisfaction = satisfaction;
        this.imageIndex = imageIndex;
        this.tileSize = tileSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.hasPaid = false;
        this.isPaused = false;
        this.random = new Random();
        this.stepsRemaining = 0;
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

    public void render(Graphics g)
    {
        g.setColor(new Color(250, 250, 250, 150));
        g.fillRect(x, y-10, 50, 10);

        if (image != null)
        {
            g.setColor(Color.BLACK);
            g.setFont(sherryFont);
            g.drawString("" + satisfaction, x + 20, y);
            g.drawImage(image, x, y, tileSize, tileSize, null);
        }
        else
        {
            g.setColor(Color.RED);
            g.fillRect(x, y, tileSize, tileSize); // Placeholder
        }
    }

    public void move(ArrayList<Item> items) {
        if (!isPaused && satisfaction > 0) {
            if (stepsRemaining <= 0) {
                stepsRemaining = random.nextInt(6) + MOVE_STEPS;
                int direction = random.nextInt(4);

                switch (direction) {
                    case 0: targetX = x + tileSize * stepsRemaining; targetY = y; break;
                    case 1: targetX = x - tileSize * stepsRemaining; targetY = y; break;
                    case 2: targetX = x; targetY = y + tileSize * stepsRemaining; break;
                    case 3: targetX = x; targetY = y - tileSize * stepsRemaining; break;
                }

                if (targetY < tileSize || targetY >= screenHeight || targetX < 0 || targetX >= screenWidth || collidesWithItems(targetX, targetY, items)) {
                    targetX = x;
                    targetY = y;
                    stepsRemaining = 0;
                }
            } else {
                if (x < targetX) x += 1;
                if (x > targetX) x -= 1;
                if (y < targetY) y += 1;
                if (y > targetY) y -= 1;

                if (x == targetX && y == targetY) {
                    stepsRemaining = 0;
                }

                if (random.nextInt(10) == 0) {
                    satisfaction -= 1;
                }
            }
        }
    }

    public void moveToEntrance() {
        if (x < 375 / tileSize * tileSize) {
            x += 1;
        } else if (x > 375 / tileSize * tileSize) {
            x -= 1;
        } else if (y > tileSize) {
            y -= 1;
        }
    }

    public void moveToCashierTable() {
        if (x < 700 / tileSize * tileSize) {
            x += tileSize;
        } else if (x > 700 / tileSize * tileSize) {
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
