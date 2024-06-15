import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Shop implements ActionListener {
    private JFrame shopFrame;
    private JTabbedPane tabbedPane;
    private Font sherryFont;
    private double money;
    private ArrayList<Item> items;
    private ArrayList<Item> shopItems;
    private ArrayList<Cat> cats;
    private ArrayList<Employee> employees;
    private Item selectedItem;
    private Cat selectedCat;
    private Employee selectedEmployee;
    private Driver driver;

    private static final String[] CAT_TYPES = {"Cat1", "Cat2", "Cat3"};
    private static final int[] CAT_PRICES = {80, 90, 100};

    private static final String[] EMPLOYEE_TYPES = {"Waiter", "Chef", "Cleaner"};
    private static final int[] EMPLOYEE_PRICES = {100, 120, 80};

    private JButton ascendingButton;
    private JButton descendingButton;

    private HashMap<String, Item> shopItemsMap;
    private JTextField searchField;
    private JPanel itemPanel;

    public Shop(Font sherryFont, double money, ArrayList<Item> items, ArrayList<Cat> cats, ArrayList<Employee> employees, Driver driver) {
        this.sherryFont = sherryFont;
        this.money = money;
        this.items = items;
        this.cats = cats;
        this.employees = employees;
        this.driver = driver;
        this.shopItems = new ArrayList<>();
        this.shopItemsMap = new HashMap<>();
        setupShopFrame();
    }

    private void setupShopFrame() {
        shopFrame = new JFrame("Shop");
        shopFrame.setSize(600, 400);
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setResizable(false);

        addItemToShop(new Item("Table", Color.YELLOW, 0, 0, 0));
        addItemToShop(new Item("Left Chair", Color.YELLOW, 0, 0, 0));
        addItemToShop(new Item("Right Chair", Color.YELLOW, 0, 0, 0));
        addItemToShop(new Item("Sofa", Color.YELLOW, 0, 0, 0));
        addItemToShop(new Item("Cat Tree", Color.YELLOW, 0, 0, 0));
        addItemToShop(new Item("Cat Litter Box", Color.YELLOW, 0, 0, 0));
        addItemToShop(new Item("Coffee Machine", Color.YELLOW, 0, 0, 0));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(sherryFont);

        tabbedPane.addTab("Items", createShopPanel());
        tabbedPane.addTab("Cats", createStaticShopPanel(CAT_TYPES, CAT_PRICES));
        tabbedPane.addTab("Employees", createStaticShopPanel(EMPLOYEE_TYPES, EMPLOYEE_PRICES));

        shopFrame.add(tabbedPane);
        shopFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void addItemToShop(Item item) {
        shopItems.add(item);
        shopItemsMap.put(item.getType(), item);
    }

    private JPanel createShopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(0, 3, 10, 10));
        itemPanel.setBackground(Color.LIGHT_GRAY);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        updateItemPanel("");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        searchField = new JTextField(15);
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

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
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

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(itemPanel, BorderLayout.CENTER);

        return panel;
    }

    private void updateItemPanel(String query) {
        itemPanel.removeAll();

        for (Item item : shopItems) {
            if (item.getType().toLowerCase().contains(query)) {
                JPanel itemPanelInner = new JPanel();
                itemPanelInner.setLayout(new BoxLayout(itemPanelInner, BoxLayout.Y_AXIS));
                itemPanelInner.setBackground(Color.LIGHT_GRAY);
                itemPanelInner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                ImageIcon rawIcon = new ImageIcon(item.getType().toLowerCase() + ".png");
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

                itemPanel.add(itemPanelInner);
            }
        }

        itemPanel.revalidate();
        itemPanel.repaint();
    }

    private void searchItems() {
        String query = searchField.getText().toLowerCase();
        List<String> itemNames = new ArrayList<>(shopItemsMap.keySet());
        Collections.sort(itemNames);
        int index = Collections.binarySearch(itemNames, query);
        if (index >= 0) {
            Item foundItem = shopItemsMap.get(itemNames.get(index));
            JOptionPane.showMessageDialog(shopFrame, "Found: " + foundItem.getType() + " - $" + foundItem.getPrice());
        } else {
            JOptionPane.showMessageDialog(shopFrame, "Item not found.");
        }
    }

    private JPanel createStaticShopPanel(String[] items, int[] prices) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 3, 10, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < items.length; i++) {
            JPanel itemPanel = new JPanel();
            itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
            itemPanel.setBackground(Color.LIGHT_GRAY);
            itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            ImageIcon rawIcon = new ImageIcon(items[i].toLowerCase() + ".png");
            Image scaledImage = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            JButton imageButton = new JButton(new ImageIcon(scaledImage));
            imageButton.setActionCommand(items[i]);
            imageButton.addActionListener(this);
            imageButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel nameLabel = new JLabel(items[i]);
            nameLabel.setFont(sherryFont);
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel priceLabel = new JLabel("$" + prices[i]);
            priceLabel.setFont(sherryFont);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            itemPanel.add(imageButton);
            itemPanel.add(Box.createVerticalStrut(5));
            itemPanel.add(nameLabel);
            itemPanel.add(priceLabel);

            panel.add(itemPanel);
        }

        return panel;
    }

    public void showShop() {
        shopFrame.setVisible(true);
    }

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

    private void sortItems(boolean ascending) {
        Collections.sort(shopItems, new SortByPrice(ascending));
    }

    private void refreshShopPanel() {
        tabbedPane.setComponentAt(0, createShopPanel());
    }

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

    private Item getItemByType(String type) {
        for (Item item : shopItems) {
            if (item.getType().equals(type)) {
                return item;
            }
        }
        return null;
    }

    private boolean isCat(String command) {
        return contains(command, CAT_TYPES);
    }

    private boolean isEmployee(String command) {
        return contains(command, EMPLOYEE_TYPES);
    }

    private boolean contains(String command, String[] array) {
        for (String item : array) {
            if (item.equals(command)) return true;
        }
        return false;
    }

    private void showInsufficientFundsMessage() {
        JOptionPane.showMessageDialog(shopFrame, "Not enough money to buy this item.");
    }
}
