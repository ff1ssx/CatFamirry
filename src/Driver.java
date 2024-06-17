/*
Names: Harry Lee, Sherry Sun
Date: 2024-06-16
Program Description: This program simulates a minimalistic management game where players manage a cat-themed shop.
Players can place items and cats to earn money and reputation which attracts more customers. This game includes mini
tasks such as customer interaction management, waste cleaning, etc.
 */

/*
Class Description: The Driver class is the main class of the program, handling the game state, rendering, and
interactions. It extends JPanel and implements ActionListener, MouseListener, and MouseMotionListener to handle user
input and game updates. The class manages the game state, including the menu, game, instructions, and about screens.
It also handles the creation and management of game components like customers, items, and waste.
 */

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
    protected static final int TILE_SIZE = 50;

    private ArrayList<Waste> wasteList;
    private Timer wasteTimer;
    private ArrayList<Item> items;
    private HashSet<Item> uniqueItems;
    private ArrayList<Customer> customers;
    public double money = 100.0;
    private double reputation = 100.0;
    private int revolution = 0;
    private Item selectedItem;private Random random = new Random();
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
    private BufferedImage aboutImage;
    private BufferedImage instructionImage;
    private BufferedImage cashierTable;
    private BufferedImage entrance;

    private Timer customerTimer;

    // Button "areas"
    private Rectangle startButtonArea = new Rectangle(240, 189, 320, 47);
    private Rectangle instructionsButtonArea = new Rectangle(240, 321, 320, 47);
    private Rectangle aboutButtonArea = new Rectangle(240, 452, 320, 47);

    // In-game, In-Instruction, In-About
    private Rectangle menuButtonArea = new Rectangle(9, 5, 101, 38);

    // In-game
    private Rectangle shopButtonArea = new Rectangle(123, 5, 101, 38);

    private static final int MAX_WASTE_COUNT = 10;
    private static final int MAX_CUSTOMER_COUNT = 10;

    /**
     * Constructor for the Driver class. Initializes game components.
     */
    public Driver() {
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.WHITE);
        setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);

        wasteList = new ArrayList<>();
        items = new ArrayList<>();
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
            aboutImage = ImageIO.read(new File("aboutMenu.png"));
            instructionImage = ImageIO.read(new File("instructionMenu.png"));
            cashierTable = ImageIO.read(new File("Cashier Table.png"));
            entrance = ImageIO.read(new File("entrance.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        shop = new Shop(sherryFont, money, items, this);

        if (gameState == STATE_MENU) {
            setupMenuComponents();
        }
    }

    /**
     * Sets up the menu components.
     */
    private void setupMenuComponents() {
        stopAllTimers();
        revalidate();
        repaint();
    }

    /**
     * Sets up the game components.
     */
    private void setupGameComponents() {
        removeAll();

        // Start waste timer
        wasteTimer = new Timer(33000, e -> spawnWaste());
        wasteTimer.start();

        // Start customer timer
        customerTimer = new Timer(16, e -> manageCustomers());
        customerTimer.start();

        revalidate();
        repaint();
    }

    /**
     * Paints the component based on the current game state.
     * @param g Graphics object used to draw the component.
     */
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

            drawGrid(g);

            g.drawImage(entrance, 375, 0, 25, 50, this); // Entrance
            g.drawImage(cashierTable, 700, 50, 100, 50, this); // Cashier

            // Render all waste items
            for (Waste waste : wasteList) {
                waste.render(g);
            }

            // Render all items
            for (Item item : items) {
                item.render(g);
            }

            // Render all customers
            for (Customer customer : customers) {
                customer.render(g);
            }

            // Display reputation and money
            g.setFont(sherryFont);
            g.setColor(Color.WHITE);
            g.drawString("" + reputation, 545, 33);
            g.drawString("" + money, 690, 33);

            if (selectedItem != null) {
                selectedItem.render(g);
            }
        } else if (gameState == STATE_ABOUT) {
            if (aboutImage != null) {
                g.drawImage(aboutImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            }
        } else if (gameState == STATE_INSTRUCTIONS) {
            if (instructionImage != null) {
                g.drawImage(instructionImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            }
        }
    }

    /**
     * Draws the grid for the game.
     * @param g Graphics object used to draw the grid.
     */
    private void drawGrid(Graphics g) {
        for (int x = 0; x < SCREEN_WIDTH; x += TILE_SIZE) {
            for (int y = TILE_SIZE; y < SCREEN_HEIGHT; y += TILE_SIZE) {
                if (selectedItem != null) {
                    g.setColor(new Color(150, 150, 150, 150));
                } else {
                    g.setColor(new Color(200, 200, 200, 150));
                }
                g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    /**
     * Manages the movement and actions of customers.
     */
    private void manageCustomers() {
        // Check if more customers can be added
        if (customers.size() < MAX_CUSTOMER_COUNT && random.nextInt(300) < reputation / 3000) {
            int imageIndex = random.nextInt(15) + 1;
            int initialSatisfaction = random.nextInt(50) + 50;
            customers.add(new Customer(375 / TILE_SIZE * TILE_SIZE, TILE_SIZE, initialSatisfaction, imageIndex, TILE_SIZE, SCREEN_WIDTH, SCREEN_HEIGHT));
        }

        // Iterate through the customer list to update their state
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Customer customer = iterator.next();
            if (!customer.hasPaid()) {
                customer.moveToCashierTable(items);
                int cashierX1 = (SCREEN_WIDTH / TILE_SIZE - 2) * TILE_SIZE;
                int cashierX2 = (SCREEN_WIDTH / TILE_SIZE - 1) * TILE_SIZE;
                int cashierY = TILE_SIZE;

                if ((customer.getX() == cashierX1 || customer.getX() == cashierX2) && customer.getY() == cashierY && !customer.isPaying()) {
                    customer.setHasPaid(true);
                    money += 5;
                }
            } else if (customer.getSatisfaction() > 0) {
                customer.move(items, this);
            } else {
                customer.moveToEntrance(items);
                if (customer.hasReachedEntrance()) {
                    iterator.remove();
                }
            }
        }

        repaint();
    }

    /**
     * Handles various action events.
     * @param e ActionEvent object.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("spawnWaste")) {
            spawnWaste();
        } else if (command.equals("Shop")) {
            shop.showShop();
            pauseGame();
        } else if (command.equals("Start")) {
            gameState = STATE_GAME;
            setupGameComponents();
        } else if (command.equals("Instructions")) {
            gameState = STATE_INSTRUCTIONS;
            stopAllTimers();
            repaint();
        } else if (command.equals("Menu")) {
            gameState = STATE_MENU;
            setupMenuComponents();
        }
        repaint();
    }

    /**
     * Handles mouse click events.
     * @param e MouseEvent object.
     */
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
                pauseGame();
            }

            // Check if an item is selected and place it in the game
            if (selectedItem != null && mouseY > TILE_SIZE) {
                int snappedX = (mouseX / TILE_SIZE) * TILE_SIZE;
                int snappedY = (mouseY / TILE_SIZE) * TILE_SIZE;
                items.add(new Item(selectedItem.getType(), selectedItem.getColor(), snappedX, snappedY, selectedItem.getPrice()));
                selectedItem = null;
                resumeGame();
                repaint();
            } else {
                boolean interacted = false;
                // Check for customer interactions with items
                for (Customer customer : customers) {
                    if (customer.contains(mouseX, mouseY)) {
                        for (Item item : items) {
                            if (Math.abs(customer.getX() - item.getX()) <= TILE_SIZE && Math.abs(customer.getY() - item.getY()) <= TILE_SIZE) {
                                customer.interactWithItem(item, this);
                                interacted = true;
                                break;
                            }
                        }
                    }
                    if (interacted) break;
                }

                // Check for waste interactions
                if (!interacted) {
                    for (int i = 0; i < wasteList.size(); i++) {
                        Waste waste = wasteList.get(i);
                        if (waste.contains(mouseX, mouseY)) {
                            wasteList.remove(i);
                            reputation += 5.0;
                            break;
                        }
                    }
                }
                repaint();
            }
        } else if (gameState == STATE_ABOUT || gameState == STATE_INSTRUCTIONS) {
            if (menuButtonArea.contains(mouseX, mouseY)) {
                gameState = STATE_MENU;
                repaint();
            }
        }
    }

    /**
     * Handles mouse press events.
     * @param e MouseEvent object.
     */
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

    /**
     * Spawns a waste item in the game.
     */
    private void spawnWaste() {
        if (wasteList.size() < MAX_WASTE_COUNT) {
            int x = random.nextInt(SCREEN_WIDTH - 50);
            int y = random.nextInt(SCREEN_HEIGHT - TILE_SIZE - 50) + TILE_SIZE;
            wasteList.add(new Waste(x, y));
            repaint();
        }
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

    /**
     * Sets the selected item.
     * @param item The item to set as selected.
     */
    public void setSelectedItem(Item item) {
        this.selectedItem = item;
    }

    /**
     * Gets the current amount of money.
     * @return The current amount of money.
     */
    public double getMoney() {
        return money;
    }

    /**
     * Sets the amount of money.
     * @param money The amount of money to set.
     */
    public void setMoney(double money) {
        this.money = money;
        repaint();
    }

    /**
     * Gets the current reputation.
     * @return The current reputation.
     */
    public double getReputation() {
        return reputation;
    }

    /**
     * Sets the reputation.
     * @param reputation The reputation to set.
     */
    public void setReputation(double reputation) {
        this.reputation = reputation;
        repaint();
    }

    /**
     * Pauses the game by stopping all timers and pausing all customers.
     */
    private void pauseGame() {
        wasteTimer.stop();
        customerTimer.stop();
        for (Customer customer : customers) {
            customer.setPaused(true);
        }
    }

    /**
     * Resumes the game by starting all timers and resuming all customers.
     */
    public void resumeGame() {
        if (wasteTimer != null) {
            wasteTimer.start();
        }
        if (customerTimer != null) {
            customerTimer.start();
        }
        for (Customer customer : customers) {
            customer.setPaused(false);
        }
    }

    /**
     * Stops all timers in the game.
     */
    private void stopAllTimers() {
        if (wasteTimer != null) {
            wasteTimer.stop();
        }
        if (customerTimer != null) {
            customerTimer.stop();
        }
    }
}
