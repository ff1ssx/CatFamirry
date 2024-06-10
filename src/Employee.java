import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Employee {
    private String name;
    private String role;
    private int x, y;
    private int width, height;
    private Image image;

    public Employee(String name, String role, int x, int y) {
        this.name = name;
        this.role = role;
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        loadImage();
    }

    private void loadImage() {
        try {
            Image rawImage;
            switch (role) {
                case "Waiter":
                    rawImage = ImageIO.read(new File("waiter.png"));
                    break;
                case "Chef":
                    rawImage = ImageIO.read(new File("chef.png"));
                    break;
                case "Cleaner":
                    rawImage = ImageIO.read(new File("cleaner.png"));
                    break;
                default:
                    rawImage = null;
            }
            if (rawImage != null) {
                image = rawImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, width, height); // Placeholder
        }
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
