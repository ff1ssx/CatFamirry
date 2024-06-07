import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Waste {
    private int x, y;
    private int size;
    private Image image;

    public Waste(int x, int y) {
        this.x = x;
        this.y = y;
        this.size = 30;
        loadImage();
    }

    private void loadImage() {
        try {
            image = ImageIO.read(new File("waste.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, size, size, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, size, size);
        }
    }

    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
}
