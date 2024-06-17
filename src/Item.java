import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Item class represents an item in the game. Items can be furniture, food, or cats.
 * Each item has a type, color, position, dimensions, image, and price. The class handles rendering and interaction with the items.
 */
public class Item implements Comparable<Item> {
    private String type;
    private Color color;
    private int x, y;
    private int width, height;
    private Image image;
    private int price;

    /**
     * Constructor
     * Initializes a new Item with given type, color, position, and price.
     * @param type Type of the item
     * @param color Color of the item
     * @param x Initial x position of the item
     * @param y Initial y position of the item
     * @param price Price of the item
     */
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

    /**
     * Loads the image for the item based on its type.
     */
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
                case "Cat Food":
                    image = ImageIO.read(new File("Cat Food.png"));
                    break;
                case "Cat Can":
                    image = ImageIO.read(new File("Cat Can.png"));
                    break;
                case "Cat Toy 1":
                    image = ImageIO.read(new File("Cat Toy 1.png"));
                    break;
                case "Cat Toy 2":
                    image = ImageIO.read(new File("Cat Toy 2.png"));
                    break;
                case "Cat Comb":
                    image = ImageIO.read(new File("Cat Comb.png"));
                    break;
                case "Coffee Machine":
                    image = ImageIO.read(new File("Coffee Machine.png"));
                    break;
                case "Ice Cream Machine":
                    image = ImageIO.read(new File("Ice Cream Machine.png"));
                    break;
                case "Cake":
                    image = ImageIO.read(new File("Cake.png"));
                    break;
                case "Bombay Cat":
                    image = ImageIO.read(new File("Bombay Cat.png"));
                    break;
                case "Orange Cat":
                    image  = ImageIO.read(new File("Orange Cat.png"));
                    break;
                case "Tabby Cat":
                    image = ImageIO.read(new File("Tabby Cat.png"));
                    break;
                case "White Cat":
                    image = ImageIO.read(new File("White Cat.png"));
                    break;
                case "British Shorthair Cat":
                    image = ImageIO.read(new File("British Shorthair Cat.png"));
                    break;
                case "Maine Coon Cat":
                    image = ImageIO.read(new File("Maine Coon Cat.png"));
                    break;
                case "Ragdoll Cat":
                    image = ImageIO.read(new File("Ragdoll Cat.png"));
                    break;
                case "American Shorthair Cat":
                    image = ImageIO.read(new File("American Shorthair Cat.png"));
                    break;
                case "Siamese Cat":
                    image = ImageIO.read(new File("Siamese Cat.png"));
                    break;
                case "Calico Cat":
                    image = ImageIO.read(new File("Calico Cat.png"));
                    break;
                case "Li Hua Cat":
                    image = ImageIO.read(new File("Li Hua Cat.png"));
                    break;
                case "Russian Blue Cat":
                    image = ImageIO.read(new File("Russian Blue Cat.png"));
                    break;
                case "Balinese Cat":
                    image = ImageIO.read(new File("Balinese Cat.png"));
                    break;
                case "Persian Cat":
                    image = ImageIO.read(new File("Persian Cat.png"));
                    break;
                case "RagaMuffin Cat":
                    image = ImageIO.read(new File("RagaMuffin Cat.png"));
                    break;
                default:
                    image = null; // If no matching type, set image to null
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders the item on the screen.
     * If the image is not available, it draws a colored rectangle as a placeholder.
     * @param g Graphics object used for drawing
     */
    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(color);
            g.fillRect(x, y, width, height); // Placeholder
        }
    }

    /**
     * Get the type of the item.
     * @return The type of the item
     */
    public String getType() {
        return type;
    }

    /**
     * Get the color of the item.
     * @return The color of the item
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the color of the item.
     * @param color The new color of the item
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the x position of the item.
     * @return The x position of the item
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x position of the item.
     * @param x The new x position of the item
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y position of the item.
     * @return The y position of the item
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y position of the item.
     * @param y The new y position of the item
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the width of the item.
     * @return The width of the item
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the item.
     * @return The height of the item
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the price of the item.
     * @return The price of the item
     */
    public int getPrice() {
        return price;
    }

    /**
     * Check if the item contains the given point (mouse click).
     * @param mouseX The x coordinate of the point
     * @param mouseY The y coordinate of the point
     * @return True if the item contains the point, false otherwise
     */
    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    /**
     * Compare items based on their price.
     * @param other The other item to compare with
     * @return Negative if this item is cheaper, positive if more expensive, zero if equal
     */
    @Override
    public int compareTo(Item other) {
        return Integer.compare(this.price, other.price);
    }
}
