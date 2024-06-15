import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Driver extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;
    private static final int TILE_SIZE = 50;

    private ArrayList<Waste> wasteList;
    private Timer wasteTimer;
    private ArrayList<Item> items;
    private ArrayList<Cat> cats;
    private ArrayList<Employee> employees;
    private HashSet<Item> uniqueItems;
    private ArrayList<Customer> customers;
    private double money = 100.0;
    private double reputation = 100.0;
    private int revolution = 0;
    private Item selectedItem;
    private Cat selectedCat;
    private Employee selectedEmployee;
    private Random random = new Random();
    private Shop shop;
    private Point dragOffset;
    private BufferedImage backgroundImage;
    private Font sherryFont;
    private static final int STATE_MENU = 0;
    private static final int STATE_GAME = 1;
    private static final int STATE_INSTRUCTIONS = 2;
    private static final int STATE_ABOUT = 3;

    private int gameState = STATE_MENU;
    private BufferedImage menuImage;

    // Button "areas"
    private Rectangle startButtonArea = new Rectangle(240, 189, 320, 47);
    private Rectangle instructionsButtonArea = new Rectangle(240, 321, 320, 47);
    private Rectangle aboutButtonArea = new Rectangle(240, 452, 320, 47);

    // In-game
    private Rectangle menuButtonArea = new Rectangle(9, 14, 101, 38);
    private Rectangle shopButtonArea = new Rectangle(9, 60, 101, 38);

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
        customers = new ArrayList<>();
        uniqueItems = new HashSet<>();

        try {
            sherryFont = Font.createFont(Font.TRUETYPE_FONT, new File("Neucha-Regular.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(sherryFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        try {
            menuImage = ImageIO.read(new File("menuImage.png"));
            backgroundImage = ImageIO.read(new File("backgroundImage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gameState == STATE_MENU) {
            setupMenuComponents();
        }
    }

    private void setupMenuComponents() {
        revalidate();
        repaint();
    }

    private void setupGameComponents() {
        removeAll();

        shop = new Shop(sherryFont, money, items, cats, employees, this);

        wasteTimer = new Timer(13000, e -> spawnWaste());
        wasteTimer.start();

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

            for (Customer customer : customers) {
                customer.render(g);
            }

            g.setFont(sherryFont);
            g.setColor(Color.BLACK);
            g.drawString("" + reputation, 545, 38);
            g.drawString("" + money, 690, 38);

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
            // Render instructions screen
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

    private void addCustomer() {
        if (revolution > 0 && revolution <= 5) {
            int randomNum = (int) (Math.random() * (3 - 1 + 1)) + 1;

            for (int i = 0; i <= randomNum; i++) {
                customers.add(new Customer(600, 800));
                repaint();
            }
        } else if (revolution > 5 && revolution <= 10) {
            int randomNum = (int) (Math.random() * (7 - 1 + 1)) + 1;

            for (int i = 0; i <= randomNum; i++) {
                customers.add(new Customer(600, 800));
                repaint();
            }
        } else if (revolution > 10) {
            int randomNum = (int) (Math.random() * (15 - 1 + 1)) + 1;

            for (int i = 0; i <= randomNum; i++) {
                customers.add(new Customer(600, 800));
                repaint();
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
            if (startButtonArea.contains(mouseX, mouseY)) {
                gameState = STATE_GAME;
                setupGameComponents();
            } else if (instructionsButtonArea.contains(mouseX, mouseY)) {
                gameState = STATE_INSTRUCTIONS;
                repaint();
            } else if (aboutButtonArea.contains(mouseX, mouseY)) {
                gameState = STATE_ABOUT;
                repaint();
            }
        } else if (gameState == STATE_GAME) {
            if (menuButtonArea.contains(mouseX, mouseY)) {
                gameState = STATE_MENU;
                setupMenuComponents();
            } else if (shopButtonArea.contains(mouseX, mouseY)) {
                shop.showShop();
            }

            if (selectedItem != null) {
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                items.add(new Item(selectedItem.getType(), selectedItem.getColor(), snappedX, snappedY, selectedItem.getPrice()));
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
                        reputation += 5.0;
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

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
        repaint();
    }
}
