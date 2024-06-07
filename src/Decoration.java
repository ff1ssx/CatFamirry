import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Decoration {

    private String type;
    private Color color;
    private int x, y;
    private int width, height;
    private Image image;

    public Decoration(String type, Color color, int x, int y) {
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        loadImage();
    }

    private void loadImage() {
        try {
            switch (type) {
                case "Table":
                    image = ImageIO.read(new File("table-removebg-preview.png"));
                    break;
                case "Chair":
                    image = ImageIO.read(new File("chair-removebg-preview.png"));
                    break;
                case "Cat Tree":
                    image = ImageIO.read(new File("cat_tree.png"));
                    break;
                case "Litter Box":
                    image = ImageIO.read(new File("litter_box.png"));
                    break;
                case "Food Bowl":
                    image = ImageIO.read(new File("food_bowl.png"));
                    break;
                case "Water Bowl":
                    image = ImageIO.read(new File("water_bowl.png"));
                    break;
                case "Toy":
                    image = ImageIO.read(new File("toy.png"));
                    break;
                case "Sofa":
                    image = ImageIO.read(new File("sofa.png"));
                    break;
                case "Coffee Machine":
                    image = ImageIO.read(new File("coffee_machine.png"));
                    break;
                case "Bookshelf":
                    image = ImageIO.read(new File("bookshelf.png"));
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

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }
}
