import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/**
 * The Customer class represents a customer in the game. Customers move around the shop, interact with items, pay at the
 * cashier, and eventually leave. The class handles customer movement, interactions, and rendering. It mainly uses the A*
 * search algorithm for the movement logic.
 */
public class Customer {
    private int x, y;
    private int targetX, targetY;
    private int satisfaction;
    private int imageIndex;
    private boolean hasPaid;
    private boolean isPaused;
    private int tileSize;
    private int screenWidth, screenHeight;
    private Random random;
    private BufferedImage image;
    private BufferedImage heartImage;
    Font sherryFont;
    private Queue<Point> path;
    private static final int MOVE_DELAY = 111;
    private long lastMoveTime;
    private boolean interactingWithItem;
    private long interactionStartTime;
    private Item currentItem;
    private BufferedImage paymentImage;
    private boolean isPaying;
    private long paymentStartTime;

    /**
     * Constructor.
     * Initializes a new Customer with given position, satisfaction, image index, tile size, and screen dimensions.
     * @param x Initial x position of the customer
     * @param y Initial y position of the customer
     * @param satisfaction Initial satisfaction level of the customer
     * @param imageIndex Index for selecting the customer's image
     * @param tileSize Size of each tile in the game grid
     * @param screenWidth Width of the game screen
     * @param screenHeight Height of the game screen
     */
    public Customer(int x, int y, int satisfaction, int imageIndex, int tileSize, int screenWidth, int screenHeight) {
        try {
            sherryFont = Font.createFont(Font.TRUETYPE_FONT, new File("Neucha-Regular.ttf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(sherryFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        this.x = x;
        this.y = y;
        this.targetX = x;
        this.targetY = y;
        this.satisfaction = satisfaction;
        this.imageIndex = imageIndex;
        this.tileSize = tileSize;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.hasPaid = false;
        this.isPaused = false;
        this.random = new Random();
        this.path = new LinkedList<>();
        this.lastMoveTime = System.currentTimeMillis();
        this.interactingWithItem = false;
        this.interactionStartTime = 0;
        this.currentItem = null;
        loadImage();
    }

    /**
     * Loads customer and interaction images.
     */
    private void loadImage() {
        try {
            BufferedImage rawImage = ImageIO.read(new File("customer" + imageIndex + ".png"));
            image = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(rawImage, 0, 0, tileSize, tileSize, null);
            g2d.dispose();

            heartImage = ImageIO.read(new File("heart.png"));
            paymentImage = ImageIO.read(new File("payment.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders the customer, heart image during interaction, and payment image during payment.
     * @param g Graphics object used for drawing
     */
    public void render(Graphics g) {
        g.setColor(new Color(250, 250, 250, 150));
        g.fillRect(x, y - 10, 50, 10);

        if (image != null) {
            g.setColor(Color.BLACK);
            g.setFont(sherryFont);
            g.drawString("" + satisfaction, x + 20, y);
            g.drawImage(image, x, y, tileSize, tileSize, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, tileSize, tileSize); // Placeholder
        }

        if (interactingWithItem && heartImage != null) {
            g.drawImage(heartImage, x + 20, y - 30, 50, 50, null);
        }

        if (isPaying && paymentImage != null) {
            g.drawImage(paymentImage, x + 35, y - 10, 13, 13, null);
        }
    }

    /**
     * Moves the customer based on their state and interactions.
     * @param items List of items in the shop
     * @param driver The main game driver controlling the game logic
     */
    public void move(ArrayList<Item> items, Driver driver) {
        if (!isPaused && satisfaction > 0) {
            long currentTime = System.currentTimeMillis();
            if (interactingWithItem) {
                if (currentTime - interactionStartTime >= 4000) {
                    interactingWithItem = false;
                    if (currentItem != null) {
                        currentItem = null;
                    }
                }
                return;
            }

            if (currentTime - lastMoveTime >= MOVE_DELAY) {
                if (!hasPaid) {
                    moveToCashierTable(items);
                } else {
                    if (path.isEmpty()) {
                        generateRandomPath(items);
                    } else {
                        followPath();
                        if (random.nextInt(100) < 100) {
                            interactWithItem(items, driver);
                        }
                    }
                }
                lastMoveTime = currentTime;
                satisfaction -= 1;
            }
        }
    }

    /**
     * Generates a random path for the customer to follow.
     * @param items List of items in the shop
     */
    private void generateRandomPath(ArrayList<Item> items) {
        int steps = random.nextInt(6) + 5; // Move between 5 and 10 tiles
        int direction = random.nextInt(4); // Random direction

        switch (direction) {
            case 0: targetX = x + tileSize * steps; targetY = y; break;
            case 1: targetX = x - tileSize * steps; targetY = y; break;
            case 2: targetX = x; targetY = y + tileSize * steps; break;
            case 3: targetX = x; targetY = y - tileSize * steps; break;
        }

        // Validate the target position
        if (targetY < tileSize || targetY >= screenHeight || targetX < 0 || targetX >= screenWidth || collidesWithItems(targetX, targetY, items)) {
            targetX = x;
            targetY = y;
        } else {
            path = findPath(x, y, targetX, targetY, items);
        }
    }

    /**
     * Moves the customer to the cashier table.
     * @param items List of items in the shop
     */
    public void moveToCashierTable(ArrayList<Item> items) {
        if (isPaying) {
            if (System.currentTimeMillis() - paymentStartTime >= 4000) {
                // Payment complete
                isPaying = false;
                hasPaid = true;
            }
            return;
        }

        int cashierX1 = (screenWidth / tileSize - 2) * tileSize;
        int cashierX2 = (screenWidth / tileSize - 1) * tileSize;
        int cashierY = tileSize;

        if (path.isEmpty()) {
            path = findPath(x, y, cashierX1, cashierY, items);
        }
        followPath();

        // Check if customer is at the cashier
        if ((x == cashierX1 || x == cashierX2) && y == cashierY) {
            isPaying = true;
            paymentStartTime = System.currentTimeMillis();
        }
    }

    /**
     * Moves the customer to the entrance.
     * @param items List of items in the shop
     */
    public void moveToEntrance(ArrayList<Item> items) {
        int entranceX = 375 / tileSize * tileSize;
        int entranceY = tileSize;

        if (path.isEmpty()) {
            path = findPath(x, y, entranceX, entranceY, items);
        }
        followPath();
    }

    /**
     * Finds the shortest path from start to end avoiding items.
     * @param startX Starting x position
     * @param startY Starting y position
     * @param endX Target x position
     * @param endY Target y position
     * @param items List of items in the shop
     * @return Queue of Points representing the path
     */
    private Queue<Point> findPath(int startX, int startY, int endX, int endY, ArrayList<Item> items) {
        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingInt(node -> node.fCost));
        HashSet<Point> closedList = new HashSet<>();
        HashMap<Point, Point> cameFrom = new HashMap<>();

        Node startNode = new Node(startX, startY, 0, heuristic(startX, startY, endX, endY));
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            if (currentNode.x == endX && currentNode.y == endY) {
                return reconstructPath(cameFrom, new Point(currentNode.x, currentNode.y));
            }

            closedList.add(new Point(currentNode.x, currentNode.y));

            for (int[] direction : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                int neighborX = currentNode.x + direction[0] * tileSize;
                int neighborY = currentNode.y + direction[1] * tileSize;

                if (neighborX < 0 || neighborX >= screenWidth || neighborY < tileSize || neighborY >= screenHeight) continue;
                if (collidesWithItems(neighborX, neighborY, items)) continue;
                Point neighborPoint = new Point(neighborX, neighborY);

                if (closedList.contains(neighborPoint)) continue;

                int tentativeGCost = currentNode.gCost + 1;
                boolean isInOpenList = openList.stream().anyMatch(node -> node.x == neighborX && node.y == neighborY);
                if (!isInOpenList || tentativeGCost < currentNode.gCost) {
                    cameFrom.put(neighborPoint, new Point(currentNode.x, currentNode.y));
                    Node neighborNode = new Node(neighborX, neighborY, tentativeGCost, tentativeGCost + heuristic(neighborX, neighborY, endX, endY));
                    if (!isInOpenList) {
                        openList.add(neighborNode);
                    }
                }
            }
        }
        return new LinkedList<>();
    }

    /**
     * Reconstructs the path from start to end using the cameFrom map.
     * @param cameFrom Map of points to reconstruct the path
     * @param current Current point in the path
     * @return Queue of Points representing the path
     */
    private Queue<Point> reconstructPath(HashMap<Point, Point> cameFrom, Point current) {
        LinkedList<Point> totalPath = new LinkedList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.addFirst(current);
        }
        return totalPath;
    }

    /**
     * Heuristic function for A* pathfinding.
     * @param x1 Starting x position
     * @param y1 Starting y position
     * @param x2 Target x position
     * @param y2 Target y position
     * @return Heuristic cost (Manhattan distance)
     */
    private int heuristic(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    /**
     * Follows the path generated by the A* algorithm.
     */
    private void followPath() {
        if (!path.isEmpty()) {
            Point nextPoint = path.poll();
            if (nextPoint != null) {
                x = nextPoint.x;
                y = nextPoint.y;
            }
        }
    }

    /**
     * Checks if the customer has reached the entrance.
     * @return True if the customer has reached the entrance, false otherwise
     */
    public boolean hasReachedEntrance() {
        int entranceX = 375 / tileSize * tileSize;
        int entranceY = tileSize;
        return x == entranceX && y == entranceY;
    }

    /**
     * Checks if the customer has left the shop.
     * @return True if the customer has left the shop, false otherwise
     */
    public boolean hasLeft() {
        return x == 375 / tileSize * tileSize && y == tileSize && satisfaction <= 0;
    }

    /**
     * Checks if the customer collides with any items.
     * @param x X position to check
     * @param y Y position to check
     * @param items List of items in the shop
     * @return True if the customer collides with an item, false otherwise
     */
    private boolean collidesWithItems(int x, int y, ArrayList<Item> items) {
        for (Item item : items) {
            if (x < item.getX() + item.getWidth() &&
                    x + tileSize > item.getX() &&
                    y < item.getY() + item.getHeight() &&
                    y + tileSize > item.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Customer interacts with an item, increasing satisfaction and generating money and reputation for the driver.
     * @param items List of items in the shop
     * @param driver The main game driver controlling the game logic
     */
    public void interactWithItem(ArrayList<Item> items, Driver driver) {
        // Iterate over items to find one within one tile distance for potential interaction
        for (Item item : items) {
            if (Math.abs(x - item.getX()) <= tileSize && Math.abs(y - item.getY()) <= tileSize && random.nextInt(100) < 20) {
                interactWithItem(item, driver); // Call the single item interaction method if conditions are met
                break;
            }
        }
    }

    /**
     * Customer interacts with a specific item, increasing satisfaction and generating money and reputation for the driver.
     * @param item The item to interact with
     * @param driver The main game driver controlling the game logic
     */
    public void interactWithItem(Item item, Driver driver) {
        satisfaction += 5; // Increase satisfaction
        driver.setMoney(driver.getMoney() + item.getPrice() / 5); // Generate money for the driver
        driver.setReputation(driver.getReputation() + 1); // Increase reputation
        item.setColor(Color.GREEN); // Change item color to indicate interaction
        interactingWithItem = true; // Set interaction flag to true
        interactionStartTime = System.currentTimeMillis(); // Record the start time of the interaction
        currentItem = item; // Set the current interacting item
    }

    /**
     * Check if the customer contains the specified coordinates (mouse click position).
     * @param mouseX X coordinate of the mouse click
     * @param mouseY Y coordinate of the mouse click
     * @return true if the customer contains the specified coordinates, false otherwise
     */
    public boolean contains(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + tileSize && mouseY >= y && mouseY <= y + tileSize;
    }

    // Getters and setters for the Customer class attributes.
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getSatisfaction() { return satisfaction; }
    public void setSatisfaction(int satisfaction) { this.satisfaction = satisfaction; }
    public boolean hasPaid() { return hasPaid; }
    public boolean isPaused() { return isPaused; }
    public void setPaused(boolean isPaused) { this.isPaused = isPaused; }
    public boolean isPaying() { return isPaying; }
    public void setHasPaid(boolean hasPaid) { this.hasPaid = hasPaid; }

    /**
     * Node class for A* pathfinding algorithm.
     */
    private static class Node {
        int x, y, gCost, fCost;

        Node(int x, int y, int gCost, int fCost) {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.fCost = fCost;
        }
    }
}
