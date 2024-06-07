import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

public class MoneyPanel extends JPanel {
    private double money;
    private Font sherryFont;
    private Image moneyIcon;

    public MoneyPanel(double initialMoney) {
        this.money = initialMoney;
        try {
            sherryFont = Font.createFont(Font.TRUETYPE_FONT, new File("Neucha-Regular.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(sherryFont);
            moneyIcon = ImageIO.read(new File("money_icon.png"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(200, 60));
        setOpaque(false);
    }

    public void setMoney(double money) {
        this.money = money;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new Color(255, 223, 186));
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        if (moneyIcon != null) {
            g2.drawImage(moneyIcon, 10, 10, 40, 40, this);
        }

        g2.setFont(sherryFont);
        g2.setColor(Color.BLACK);
        g2.drawString("Money: $" + money, 60, 40);
    }
}
