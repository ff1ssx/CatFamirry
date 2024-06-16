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
    private ArrayList<Cat> shopCats;
    private ArrayList<Employee> employees;
    private Item selectedItem;
    private Cat selectedCat;
    private Employee selectedEmployee;
    private Driver driver;

    private static final String[] EMPLOYEE_TYPES = {"Waiter", "Chef", "Cleaner"};
    private static final int[] EMPLOYEE_PRICES = {100, 120, 80};

    private JButton ascendingButton;
    private JButton descendingButton;

    private HashMap<String, Item> shopItemsMap;
    private HashMap<String, Cat> shopCatsMap;
    private JTextField searchField;
    private JPanel itemPanel;
    private JPanel catPanel;

    public Shop(Font sherryFont, double money, ArrayList<Item> items, ArrayList<Cat> cats, ArrayList<Employee> employees, Driver driver) {
        this.sherryFont = sherryFont;
        this.money = money;
        this.items = items;
        this.cats = cats;
        this.employees = employees;
        this.driver = driver;
        this.shopItems = new ArrayList<>();
        this.shopItemsMap = new HashMap<>();
        this.shopCats = new ArrayList<>();
        this.shopCatsMap = new HashMap<>();
        setupShopFrame();
    }

    private void setupShopFrame() {
        shopFrame = new JFrame("Shop");
        shopFrame.setSize(600, 400);
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setResizable(false);

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

        addCatToShop(new Cat("Bombay", Color.YELLOW, "good", 0, 0, 500));
        addCatToShop(new Cat("Orange", Color.YELLOW, "good", 0, 0, 300));
        addCatToShop(new Cat("Tabby", Color.YELLOW, "good", 0, 0, 600));
        addCatToShop(new Cat("White", Color.YELLOW, "good", 0, 0, 250));
        addCatToShop(new Cat("British Shorthair", Color.YELLOW, "good", 0, 0, 1000));
        addCatToShop(new Cat("Maine Coon", Color.YELLOW, "good", 0, 0, 1250));
        addCatToShop(new Cat("Ragdoll", Color.YELLOW, "good", 0, 0, 1250));
        addCatToShop(new Cat("American Shorthair", Color.YELLOW, "good", 0, 0, 300));
        addCatToShop(new Cat("Siamese", Color.YELLOW, "good", 0, 0, 600));
        addCatToShop(new Cat("Calico", Color.YELLOW, "good", 0, 0, 200));
        addCatToShop(new Cat("Li Hua", Color.YELLOW, "good", 0, 0, 400));
        addCatToShop(new Cat("Russian Blue", Color.YELLOW, "good", 0, 0, 800));
        addCatToShop(new Cat("Balinese", Color.YELLOW, "good", 0, 0, 100));
        addCatToShop(new Cat("Persian", Color.YELLOW, "good", 0, 0, 1000));
        addCatToShop(new Cat("RagaMuffin", Color.YELLOW, "good", 0, 0, 700));


        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(sherryFont);

        tabbedPane.addTab("Items", createShopPanel());
        tabbedPane.addTab("Cats", createCatShopPanel());
        tabbedPane.addTab("Employees", createStaticShopPanel(EMPLOYEE_TYPES, EMPLOYEE_PRICES));

        shopFrame.add(tabbedPane);
        shopFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        shopFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                driver.resumeGame();
            }
        });
    }

    private void addItemToShop(Item item) {
        shopItems.add(item);
        shopItemsMap.put(item.getType(), item);
    }

    private void addCatToShop(Cat cat)
    {
        shopCats.add(cat);
        shopCatsMap.put(cat.getType(), cat);
    }

    private JPanel createShopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        itemPanel = new JPanel();
        itemPanel.setLayout(new GridLayout(0, 3, 10, 10));
        itemPanel.setBackground(Color.LIGHT_GRAY);
        itemPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        updateItemPanel("");

        JScrollPane scrollPane = new JScrollPane(itemPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCatShopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        catPanel = new JPanel();
        catPanel.setLayout(new GridLayout(0, 3, 10, 10));
        catPanel.setBackground(Color.LIGHT_GRAY);
        catPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        updateCatPanel("");

        JScrollPane catScrollPane = new JScrollPane(catPanel);
        catScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        catScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        catScrollPane.getVerticalScrollBar().setUnitIncrement(16);

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
        panel.add(catScrollPane, BorderLayout.CENTER);

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

    private void updateCatPanel(String query) {
        catPanel.removeAll();

        for (Cat cat : shopCats) {
            if (cat.getType().toLowerCase().contains(query)) {
                JPanel catPanelInner = new JPanel();
                catPanelInner.setLayout(new BoxLayout(catPanelInner, BoxLayout.Y_AXIS));
                catPanelInner.setBackground(Color.LIGHT_GRAY);
                catPanelInner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                ImageIcon rawIcon = new ImageIcon(cat.getType() + ".png");
                Image scaledImage = rawIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JButton imageButton = new JButton(new ImageIcon(scaledImage));
                imageButton.setActionCommand(cat.getType());
                imageButton.addActionListener(this);
                imageButton.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel nameLabel = new JLabel(cat.getType());
                nameLabel.setFont(sherryFont);
                nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel priceLabel = new JLabel("$" + cat.getPrice());
                priceLabel.setFont(sherryFont);
                priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                catPanelInner.add(imageButton);
                catPanelInner.add(Box.createVerticalStrut(5));
                catPanelInner.add(nameLabel);
                catPanelInner.add(priceLabel);

                catPanel.add(catPanelInner);
            }
        }

        catPanel.revalidate();
        catPanel.repaint();
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
            handleCatPurchase(command);
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

    private void handleCatPurchase(String command)
    {
        Cat selectedCat = getCatByType(command);
        if (selectedCat != null)
        {
            int price = selectedCat.getPrice();
            double currentMoney = driver.getMoney();
            if (currentMoney >= price) {
                currentMoney -= price;
                driver.setMoney(currentMoney);
                cats.add(new Cat(command, Color.YELLOW, "good", -50, -50, price));
                shopFrame.setVisible(false);
                driver.setSelectedCat(selectedCat);
            }
            else
            {
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

    private Cat getCatByType(String type) {
        for (Cat cat : shopCats) {
            if (cat.getType().equals(type)) {
                return cat;
            }
        }
        return null;
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
