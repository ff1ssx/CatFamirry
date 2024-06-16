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
    private boolean headingToCashier;

    public Customer(int x, int y, int satisfaction, int imageIndex, int tileSize, int screenWidth, int screenHeight) {
        try {
            sherryFont = Font.createFont(Font.TRUETYPE_FONT, new File("Neucha-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(sherryFont);
        } catch (IOException | FontFormatException e) {
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
        this.headingToCashier = true;
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
            if (headingToCashier) {
                moveToCashierTable(items);
            } else {
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
                    int newX = x;
                    int newY = y;

                    if (x < targetX) newX += 1;
                    if (x > targetX) newX -= 1;
                    if (y < targetY) newY += 1;
                    if (y > targetY) newY -= 1;

                    if (!collidesWithItems(newX, newY, items) && !isCollidingWithCashier(newX, newY)) {
                        x = newX;
                        y = newY;
                    } else {
                        stepsRemaining = 0; // Stop moving if colliding
                    }

                    if (x == targetX && y == targetY) {
                        stepsRemaining = 0;
                    }

                    if (random.nextInt(10) == 0) {
                        satisfaction -= 1;
                    }
                }
            }
        }
    }

    public void moveToCashierTable(ArrayList<Item> items) {
        int cashierX1 = (screenWidth / tileSize - 2) * tileSize;
        int cashierX2 = (screenWidth / tileSize - 1) * tileSize;
        int cashierY = tileSize;

        int newX = x;
        int newY = y;

        if (y < cashierY && !collidesWithItems(x, y + 1, items)) {
            newY += 1;
        } else if (x < cashierX1 && !collidesWithItems(x + 1, y, items)) {
            newX += 1;
        } else if (x > cashierX2 && !collidesWithItems(x - 1, y, items)) {
            newX -= 1;
        } else {
            headingToCashier = false;
        }

        if (!collidesWithItems(newX, newY, items)) {
            x = newX;
            y = newY;
        }
    }

    public void moveToEntrance(ArrayList<Item> items) {
        int entranceX = 375 / tileSize * tileSize;
        int entranceY = tileSize;

        int newX = x;
        int newY = y;

        if (x != entranceX) {
            if (x < entranceX && !collidesWithItems(x + 1, y, items)) {
                newX += 1;
            } else if (x > entranceX && !collidesWithItems(x - 1, y, items)) {
                newX -= 1;
            }
        } else if (y != entranceY) {
            if (y > entranceY && !collidesWithItems(x, y - 1, items)) {
                newY -= 1;
            } else if (y < entranceY && !collidesWithItems(x, y + 1, items)) {
                newY += 1;
            }
        }

        if (!collidesWithItems(newX, newY, items)) {
            x = newX;
            y = newY;
        }
    }

    public boolean hasReachedEntrance() {
        int entranceX = 375 / tileSize * tileSize;
        int entranceY = tileSize;
        return x == entranceX && y == entranceY;
    }

    public boolean isHeadingToCashier() {
        return headingToCashier;
    }

    public boolean hasLeft() {
        return x == 375 / tileSize * tileSize && y == tileSize && satisfaction <= 0;
    }

    private boolean collidesWithItems(int x, int y, ArrayList<Item> items) {
        for (Item item : items) {
            if (x < item.getX() + item.getWidth() &&
                    x + tileSize > item.getX() &&
                    y < item.getY() + item.getHeight() &&
                    y + tileSize > item.getY()) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollidingWithCashier(int x, int y) {
        int cashierX = 375 / tileSize * tileSize;
        int cashierY = 11 * tileSize;
        return (x < cashierX + tileSize && x + tileSize > cashierX && y < cashierY + tileSize && y + tileSize > cashierY);
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
