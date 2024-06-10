import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;

@SuppressWarnings("serial")
public class Driver extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int TILE_SIZE = 50;

    private ArrayList<Waste> wasteList;
    private ArrayList<Item> items;
    private ArrayList<Cat> cats;
    private ArrayList<Employee> employees;
    private double money = 100.0;
    private JButton shopButton;
    private boolean shopOpen = false;
    private Item selectedItem;
    private Cat selectedCat;
    private Employee selectedEmployee;
    private Random random = new Random();
    private Shop shop;
    private Point dragOffset;
    private BufferedImage backgroundImage;
    private Font sherryFont;
    private MoneyPanel moneyPanel;
    private static final int STATE_MENU = 0;
    private static final int STATE_GAME = 1;
    private static final int STATE_INSTRUCTIONS = 2;

    private int gameState = STATE_MENU;
    private BufferedImage menuImage;
    private JButton startButton;
    private JButton instructionButton;
    private JButton menuButton;

    public Driver() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.WHITE);
        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);

        wasteList = new ArrayList<>();
        items = new ArrayList<>();
        cats = new ArrayList<>();
        employees = new ArrayList<>();

        try {
            sherryFont = Font.createFont(Font.TRUETYPE_FONT, new File("Neucha-Regular.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(sherryFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        try {
            menuImage = ImageIO.read(new File("menuImage.jpg"));
            backgroundImage = ImageIO.read(new File("temp.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gameState == STATE_MENU) {
            setupMenuComponents();
        }
    }

    private void setupMenuComponents() {
        removeAll();
        startButton = new JButton("Start");
        startButton.setBounds(350, 200, 100, 50);
        startButton.addActionListener(this);
        startButton.setFont(sherryFont);
        add(startButton);

        instructionButton = new JButton("Instructions");
        instructionButton.setBounds(350, 300, 150, 50);
        instructionButton.addActionListener(this);
        instructionButton.setFont(sherryFont);
        add(instructionButton);

        revalidate();
        repaint();
    }

    private void setupGameComponents() {
        removeAll();
        shopButton = new JButton("Shop");
        shopButton.setBounds(10, 10, 80, 30);
        shopButton.addActionListener(this);
        shopButton.setFont(sherryFont);
        add(shopButton);

        menuButton = new JButton("Menu");
        menuButton.setBounds(100, 10, 100, 30);
        menuButton.addActionListener(this);
        menuButton.setFont(sherryFont);
        add(menuButton);

        moneyPanel = new MoneyPanel(money);
        moneyPanel.setBounds(SCREEN_WIDTH - 260, 10, 200, 60);
        add(moneyPanel);

        shop = new Shop(sherryFont, money, moneyPanel, items, cats, employees, this);

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (gameState == STATE_MENU) {
            if (menuImage != null) {
                g.drawImage(menuImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            }
        } else if (gameState == STATE_GAME) {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            }

            if (selectedItem != null || selectedCat != null || selectedEmployee != null) {
                drawGrid(g);
            }

            for (Waste waste : wasteList) {
                waste.render(g);
            }

            for (Item item : items) {
                item.render(g);
            }

            for (Cat cat : cats) {
                cat.render(g);
            }

            for (Employee employee : employees) {
                employee.render(g);
            }

            g.setFont(sherryFont);
            g.setColor(Color.ORANGE);
            g.drawString("Money: $" + money, SCREEN_WIDTH - 250, 50);

            if (selectedItem != null) {
                selectedItem.render(g);
            }

            if (selectedCat != null) {
                selectedCat.render(g);
            }

            if (selectedEmployee != null) {
                selectedEmployee.render(g);
            }
        } else if (gameState == STATE_INSTRUCTIONS) {
        }
    }

    public void showGameScreen() {
        gameState = STATE_GAME;
        setupGameComponents();
        repaint();
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
            shop.showShop();
        } else if (command.equals("Start")) {
            gameState = STATE_GAME;
            setupGameComponents();
        } else if (command.equals("Instructions")) {
            gameState = STATE_INSTRUCTIONS;
            repaint();
        } else if (command.equals("Menu")) {
            gameState = STATE_MENU;
            setupMenuComponents();
        }
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        if (gameState == STATE_MENU) {

        } else if (gameState == STATE_GAME) {
            if (selectedItem != null) {
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                items.add(new Item(selectedItem.getType(), selectedItem.getColor(), snappedX, snappedY));
                selectedItem = null;
                repaint();
            } else if (selectedCat != null) {
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                cats.add(new Cat(snappedX, snappedY, selectedCat.getColor(), selectedCat.getMood()));
                selectedCat = null;
                repaint();
            } else if (selectedEmployee != null) {
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                employees.add(new Employee(selectedEmployee.getName(), selectedEmployee.getRole(), snappedX, snappedY));
                selectedEmployee = null;
                repaint();
            } else {
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
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (gameState == STATE_GAME) {
            for (Item item : items) {
                if (item.contains(e.getX(), e.getY())) {
                    dragOffset = new Point(e.getX() - item.getX(), e.getY() - item.getY());
                    selectedItem = item;
                    items.remove(item);
                    repaint();
                    break;
                }
            }

            for (Cat cat : cats) {
                if (cat.contains(e.getX(), e.getY())) {
                    dragOffset = new Point(e.getX() - cat.getX(), e.getY() - cat.getY());
                    selectedCat = cat;
                    cats.remove(cat);
                    repaint();
                    break;
                }
            }

            for (Employee employee : employees) {
                if (employee.contains(e.getX(), e.getY())) {
                    dragOffset = new Point(e.getX() - employee.getX(), e.getY() - employee.getY());
                    selectedEmployee = employee;
                    employees.remove(employee);
                    repaint();
                    break;
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (gameState == STATE_GAME) {
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameState == STATE_GAME) {
            if (selectedItem != null) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                selectedItem.setX(snappedX);
                selectedItem.setY(snappedY);
                repaint();
            } else if (selectedCat != null) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                selectedCat.setX(snappedX);
                selectedCat.setY(snappedY);
                repaint();
            } else if (selectedEmployee != null) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                selectedEmployee.setX(snappedX);
                selectedEmployee.setY(snappedY);
                repaint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameState == STATE_GAME) {
        }
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
        JFrame frame = new JFrame("Cat Famirry 사랑해 환희오빠");
        Driver gamePanel = new Driver();
        frame.add(gamePanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    public void setSelectedCat(Cat cat) {
        this.selectedCat = cat;
    }

    public void setSelectedEmployee(Employee employee) {
        this.selectedEmployee = employee;
    }
}