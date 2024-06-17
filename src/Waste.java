import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * The Waste class represents a piece of waste in the game. It handles loading its image,
 * rendering itself on the screen, and checking if it contains a given point.
 */
public class Waste {
    private int x, y;
    private int size;
    private Image image;

    /**
     * Constructor.
     * Initializes a waste object at the specified coordinates.
     * @param x The x-coordinate of the waste
     * @param y The y-coordinate of the waste
     */
    public Waste(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 30; // Set default size for waste
        loadImage(); // Load the image for the waste
    }

    /**
     * Loads the image for the waste from a file.
     */
    private void loadImage() {
        try {
            image = ImageIO.read(new File("waste.png"));
        } catch (IOException e) {
            e.printStackTrace(); // Print the stack trace if there is an error loading the image
        }
    }

    /**
     * Renders the waste on the screen.
     * @param g The Graphics object used for drawing
     */
    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, size, size, null); // Draw the waste image
        } else {
            g.setColor(Color.GRAY); // Fallback color if image is not loaded
            g.fillRect(x, y, size, size); // Draw a gray rectangle as a placeholder
        }
    }

    /**
     * Checks if the waste contains the specified point.
     * @param mouseX The x-coordinate of the point
     * @param mouseY The y-coordinate of the point
     * @return True if the waste contains the point, otherwise false
     */
    public boolean contains(int mouseX, int mouseY) {
        // Check if the point (mouseX, mouseY) is within the bounds of the waste
        return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
}
