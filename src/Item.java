import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Item implements Comparable<Item> {
    private String type;
    private Color color;
    private int x, y;
    private int width, height;
    private Image image;
    private int price;

    public Item(String type, Color color, int x, int y, int price) {
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.price = price;
        loadImage();
    }

    private void loadImage() {
        try {
            switch (type) {
                case "Table":
                    image = ImageIO.read(new File("Table.png"));
                    break;
                case "Left Chair":
                    image = ImageIO.read(new File("Left Chair.png"));
                    break;
                case "Right Chair":
                    image = ImageIO.read(new File("Right Chair.png"));
                    break;
                case "Sofa":
                    image = ImageIO.read(new File("Sofa.png"));
                    break;
                case "Cat Tree":
                    image = ImageIO.read(new File("Cat Tree.png"));
                    break;
                case "Cat Litter Box":
                    image = ImageIO.read(new File("Cat Litter Box.png"));
                    break;
                case "Coffee Machine":
                    image = ImageIO.read(new File("Coffee Machine.png"));
                    break;
                default:
                    image = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, width, height); // Placeholder
        }
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
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

    public int getPrice() {
        return price;
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    @Override
    public int compareTo(Item other) {
        return Integer.compare(this.price, other.price);
    }
}
