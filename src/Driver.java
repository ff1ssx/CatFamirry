import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

@SuppressWarnings("serial")
public class Driver extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int TILE_SIZE = 50;

    private ArrayList<Waste> wasteList;
    private ArrayList<Decoration> decorations;
    private double money = 100.0;
    private JButton shopButton;
    private boolean shopOpen = false;
    private Decoration selectedDecoration;
    private Random random = new Random();
    private JFrame shopFrame;
    private JPanel shopPanel;
    private Point dragOffset;
    private BufferedImage backgroundImage;
    private Font sherryFont;
    private MoneyPanel moneyPanel;

    public Driver() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.WHITE);
        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);

        wasteList = new ArrayList<>();
        decorations = new ArrayList<>();

        shopButton = new JButton("Shop");
        shopButton.setBounds(10, 10, 80, 30);
        shopButton.addActionListener(this);
        add(shopButton);

        moneyPanel = new MoneyPanel(money);
        moneyPanel.setBounds(SCREEN_WIDTH - 260, 10, 200, 60);
        add(moneyPanel);

        Timer wasteTimer = new Timer(13000, this);
        wasteTimer.setActionCommand("spawnWaste");
        wasteTimer.start();

        shopFrame = new JFrame("Shop");
        shopFrame.setSize(600, 200);
        shopButton.setFont(sherryFont);
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setResizable(false);

        shopPanel = new JPanel();
        shopPanel.setLayout(new BoxLayout(shopPanel, BoxLayout.X_AXIS));
        shopPanel.setBackground(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(shopPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        shopFrame.add(scrollPane);

        shopFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        shopButton.addActionListener(e -> shopFrame.setVisible(true));
        shopButton.setBackground(Color.CYAN);

        String[] items = {"Table", "Chair", "Cat Tree", "Litter Box", "Food Bowl", "Water Bowl", "Toy", "Sofa", "Coffee Machine", "Bookshelf"};
        int[] prices = {50, 30, 80, 20, 10, 10, 15, 100, 60, 70};
        for (int i = 0; i < items.length; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.LIGHT_GRAY);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JButton button = new JButton(items[i]);
            button.setActionCommand(items[i]);
            button.addActionListener(this);
            button.setFont(sherryFont);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel priceLabel = new JLabel("$" + prices[i]);
            priceLabel.setFont(sherryFont);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel imageLabel = new JLabel(new ImageIcon(items[i].toLowerCase() + ".png"));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            itemPanel.add(imageLabel);
            itemPanel.add(Box.createVerticalStrut(5));
            itemPanel.add(button);
            itemPanel.add(Box.createVerticalStrut(5));
            itemPanel.add(priceLabel);

            shopPanel.add(itemPanel);
        }

        try {
            backgroundImage = ImageIO.read(new File("temp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
        }

        if (selectedDecoration != null) {
            drawGrid(g);
        }

        for (Waste waste : wasteList) {
            waste.render(g);
        }

        for (Decoration decoration : decorations) {
            decoration.render(g);
        }

        g.setFont(sherryFont);
        g.setColor(Color.ORANGE);
        g.drawString("Money: $" + money, SCREEN_WIDTH - 250, 50);

        if (selectedDecoration != null) {
            selectedDecoration.render(g);
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(new Color(200, 200, 200, 150));
        for (int x = 0; x < SCREEN_WIDTH; x += TILE_SIZE) {
            for (int y = 0; y < SCREEN_HEIGHT; y += TILE_SIZE) {
                g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("spawnWaste")) {
            spawnWaste();
        } else if (command.equals("Shop")) {
            shopFrame.setVisible(true);
        } else {
            selectedDecoration = new Decoration(command, Color.YELLOW, -50, -50);
            shopFrame.setVisible(false);
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (selectedDecoration != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
            int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
            decorations.add(new Decoration(selectedDecoration.getType(), selectedDecoration.getColor(), snappedX, snappedY));
            selectedDecoration = null;
            repaint();
        } else {
            int mouseX = e.getX();
            int mouseY = e.getY();
            for (int i = 0; i < wasteList.size(); i++) {
                Waste waste = wasteList.get(i);
                if (waste.contains(mouseX, mouseY)) {
                    wasteList.remove(i);
                    money += 5.0;
                    moneyPanel.setMoney(money);
                    break;
                }
            }
            repaint();
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        for (Decoration decoration : decorations) {
            if (decoration.contains(e.getX(), e.getY())) {
                dragOffset = new Point(e.getX() - decoration.getX(), e.getY() - decoration.getY());
                selectedDecoration = decoration;
                decorations.remove(decoration);
                repaint();
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (selectedDecoration != null) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
            int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
            selectedDecoration.setX(snappedX);
            selectedDecoration.setY(snappedY);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void spawnWaste() {
        int x = random.nextInt(SCREEN_WIDTH - 50);
        int y = random.nextInt(SCREEN_HEIGHT - 50);
        wasteList.add(new Waste(x, y));
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Cat Famirry");
        Driver gamePanel = new Driver();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
