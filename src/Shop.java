import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The Shop class represents the shop interface in the game where players can buy items and cats.
 * It handles the display of items, search functionality, sorting, and purchasing of items.
 */
public class Shop implements ActionListener {
    private JFrame shopFrame;
    private JTabbedPane tabbedPane;
    private Font sherryFont;
    private double money;
    private ArrayList<Item> items;
    private ArrayList<Item> shopItems;
    private Item selectedItem;
    private Driver driver;

    private JButton ascendingButton;
    private JButton descendingButton;

    private HashMap<String, Item> shopItemsMap;
    private JTextField searchField;
    private JPanel itemPanel;
    private JPanel catPanel;

    /**
     * Constructor.
     * Initializes the shop with the given font, money, items, and driver.
     * @param sherryFont Font used in the shop
     * @param money Initial money available
     * @param items List of items in the shop
     * @param driver Driver object controlling the game
     */
    public Shop(Font sherryFont, double money, ArrayList<Item> items, Driver driver) {
        this.sherryFont = sherryFont;
        this.money = money;
        this.items = items;
        this.driver = driver;
        this.shopItems = new ArrayList<>();
        this.shopItemsMap = new HashMap<>();
        setupShopFrame();
    }

    /**
     * Sets up the shop frame and initializes the UI components.
     */
    private void setupShopFrame() {
        shopFrame = new JFrame("Shop");
        shopFrame.setSize(600, 400);
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setResizable(false);

        // Adding non-cat items
        addItemToShop(new Item("Table", Color.YELLOW, 0, 0, 30));
        addItemToShop(new Item("Left Chair", Color.YELLOW, 0, 0, 15));
        addItemToShop(new Item("Right Chair", Color.YELLOW, 0, 0, 15));
        addItemToShop(new Item("Sofa", Color.YELLOW, 0, 0, 50));
        addItemToShop(new Item("Cat Tree", Color.YELLOW, 0, 0, 50));
        addItemToShop(new Item("Cat Litter Box", Color.YELLOW, 0, 0, 30));
        addItemToShop(new Item("Cat Food", Color.YELLOW, 0, 0, 40));
        addItemToShop(new Item("Cat Can", Color.YELLOW, 0, 0, 60));
        addItemToShop(new Item("Cat Toy 1", Color.YELLOW, 0, 0, 30));
        addItemToShop(new Item("Cat Toy 2", Color.YELLOW, 0, 0, 30));
        addItemToShop(new Item("Cat Comb", Color.YELLOW, 0, 0, 45));
        addItemToShop(new Item("Cake", Color.YELLOW, 0, 0, 50));
        addItemToShop(new Item("Coffee Machine", Color.YELLOW, 0, 0, 100));
        addItemToShop(new Item("Ice Cream Machine", Color.YELLOW, 0, 0, 100));

        // Adding cat items
        addItemToShop(new Item("Bombay Cat", Color.ORANGE, 0, 0, 500));
        addItemToShop(new Item("Orange Cat", Color.ORANGE, 0, 0, 300));
        addItemToShop(new Item("Tabby Cat", Color.ORANGE, 0, 0, 600));
        addItemToShop(new Item("White Cat", Color.ORANGE, 0, 0, 250));
        addItemToShop(new Item("British Shorthair Cat", Color.ORANGE, 0, 0, 1000));
        addItemToShop(new Item("Maine Coon Cat", Color.ORANGE, 0, 0, 1250));
        addItemToShop(new Item("Ragdoll Cat", Color.ORANGE, 0, 0, 1250));
        addItemToShop(new Item("American Shorthair Cat", Color.ORANGE, 0, 0, 300));
        addItemToShop(new Item("Siamese Cat", Color.ORANGE, 0, 0, 600));
        addItemToShop(new Item("Calico Cat", Color.ORANGE, 0, 0, 200));
        addItemToShop(new Item("Li Hua Cat", Color.ORANGE, 0, 0, 400));
        addItemToShop(new Item("Russian Blue Cat", Color.ORANGE, 0, 0, 800));
        addItemToShop(new Item("Balinese Cat", Color.ORANGE, 0, 0, 100));
        addItemToShop(new Item("Persian Cat", Color.ORANGE, 0, 0, 1000));
        addItemToShop(new Item("RagaMuffin Cat", Color.ORANGE, 0, 0, 700));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(sherryFont);

        // Create the item and cat panels
        tabbedPane.addTab("Items", createShopPanel(false));
        tabbedPane.addTab("Cats", createShopPanel(true));

        shopFrame.add(tabbedPane);
        shopFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        shopFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.resumeGame();
            }
        });
    }

    /**
     * Adds an item to the shop inventory.
     * @param item The item to add
     */
    private void addItemToShop(Item item) {
        shopItems.add(item);
        shopItemsMap.put(item.getType(), item);
    }

    /**
     * Creates a shop panel for either items or cats.
     * @param isCatPanel True if creating a cat panel, false if creating an item panel
     * @return The created JPanel
     */
    private JPanel createShopPanel(boolean isCatPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextField searchField;
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(15);
        searchPanel.add(searchField);

        JPanel buttonPanel = new JPanel();
        ascendingButton = new JButton("Ascending");
        descendingButton = new JButton("Descending");

        ascendingButton.addActionListener(this);
        ascendingButton.setActionCommand("Ascending");

        descendingButton.addActionListener(this);
        descendingButton.setActionCommand("Descending");

        buttonPanel.add(ascendingButton);
        buttonPanel.add(descendingButton);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        if (isCatPanel) {
            catPanel = new JPanel();
            catPanel.setLayout(new GridLayout(0, 3, 10, 10));
            catPanel.setBackground(Color.LIGHT_GRAY);
            catPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            updateCatPanel("");

            // Add search functionality for cat panel
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updateCatPanel(searchField.getText().toLowerCase());
                }

                public void removeUpdate(DocumentEvent e) {
                    updateCatPanel(searchField.getText().toLowerCase());
                }

                public void insertUpdate(DocumentEvent e) {
                    updateCatPanel(searchField.getText().toLowerCase());
                }
            });

        } else {
            itemPanel = new JPanel();
            itemPanel.setLayout(new GridLayout(0, 3, 10, 10));
            itemPanel.setBackground(Color.LIGHT_GRAY);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            updateItemPanel("");

            // Add search functionality for item panel
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                public void changedUpdate(DocumentEvent e) {
                    updateItemPanel(searchField.getText().toLowerCase());
                }

                public void removeUpdate(DocumentEvent e) {
                    updateItemPanel(searchField.getText().toLowerCase());
                }

                public void insertUpdate(DocumentEvent e) {
                    updateItemPanel(searchField.getText().toLowerCase());
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(isCatPanel ? catPanel : itemPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Added horizontal scrollbar
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(topPanel, BorderLayout.NORTH); // Add top panel containing search and buttons
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Updates the item panel with items matching the search query.
     * @param query The search query
     */
    private void updateItemPanel(String query) {
        if (itemPanel == null) return;

        itemPanel.removeAll();

        // Add items matching the query to the panel
        for (Item item : shopItems) {
            if (item.getType().toLowerCase().contains(query) && item.getColor() == Color.YELLOW) {
                addItemToPanel(itemPanel, item);
            }
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    /**
     * Updates the cat panel with items matching the search query.
     * @param query The search query
     */
    private void updateCatPanel(String query) {
        if (catPanel == null) return;

        catPanel.removeAll();

        // Add items matching the query to the panel
        for (Item item : shopItems) {
            if (item.getType().toLowerCase().contains(query) && item.getColor() == Color.ORANGE) {
                addItemToPanel(catPanel, item);
            }
        }

        catPanel.revalidate();
        catPanel.repaint();
    }

    /**
     * Adds an item to the specified panel.
     * @param panel The panel to add the item to
     * @param item The item to add
     */
    private void addItemToPanel(JPanel panel, Item item) {
        JPanel itemPanelInner = new JPanel();
        itemPanelInner.setLayout(new BoxLayout(itemPanelInner, BoxLayout.Y_AXIS));
        itemPanelInner.setBackground(Color.LIGHT_GRAY);
        itemPanelInner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String itemType = item.getType().toLowerCase();
        ImageIcon rawIcon = new ImageIcon(itemType + ".png");
        Image scaledImage = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        JButton imageButton = new JButton(new ImageIcon(scaledImage));
        imageButton.setActionCommand(item.getType());
        imageButton.addActionListener(this);
        imageButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(item.getType());
        nameLabel.setFont(sherryFont);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("$" + item.getPrice());
        priceLabel.setFont(sherryFont);
        priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        itemPanelInner.add(imageButton);
        itemPanelInner.add(Box.createVerticalStrut(5));
        itemPanelInner.add(nameLabel);
        itemPanelInner.add(priceLabel);

        panel.add(itemPanelInner);
    }

    /**
     * Sorts the shop items by price.
     * @param ascending True for ascending order, false for descending
     */
    private void sortItems(boolean ascending) {
        Collections.sort(shopItems, new SortByPrice(ascending));
    }

    /**
     * Refreshes the shop panel to reflect changes.
     */
    private void refreshShopPanel() {
        tabbedPane.setComponentAt(0, createShopPanel(false));
        tabbedPane.setComponentAt(1, createShopPanel(true));
    }

    /**
     * Handles the purchase of an item.
     * @param command The command representing the item to purchase
     */
    private void handleItemPurchase(String command) {
        Item selectedItem = getItemByType(command);
        if (selectedItem != null) {
            int price = selectedItem.getPrice();
            double currentMoney = driver.getMoney();
            if (currentMoney >= price) {
                currentMoney -= price;
                driver.setMoney(currentMoney);
                items.add(new Item(command, Color.YELLOW, -50, -50, price));
                shopFrame.setVisible(false);
                driver.setSelectedItem(selectedItem);
            } else {
                showInsufficientFundsMessage();
            }
        }
    }

    /**
     * Retrieves an item by its type.
     * @param type The type of the item
     * @return The item matching the type, or null if not found
     */
    private Item getItemByType(String type) {
        for (Item item : shopItems) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Shows a message indicating insufficient funds.
     */
    private void showInsufficientFundsMessage() {
        JOptionPane.showMessageDialog(shopFrame, "Not enough money to buy this item.");
    }

    /**
     * Handles action events such as button clicks.
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Ascending")) {
            sortItems(true);
            refreshShopPanel();
        } else if (command.equals("Descending")) {
            sortItems(false);
            refreshShopPanel();
        } else {
            handleItemPurchase(command);
        }
    }

    /**
     * Shows the shop window.
     */
    public void showShop() {
        shopFrame.setVisible(true);
    }
}
