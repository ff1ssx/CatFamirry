import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Shop implements ActionListener {
    private JFrame shopFrame;
    private JTabbedPane tabbedPane;
    private Font sherryFont;
    private double money;
    private MoneyPanel moneyPanel;
    private ArrayList<Item> items;
    private ArrayList<Cat> cats;
    private ArrayList<Employee> employees;
    private Item selectedItem;
    private Cat selectedCat;
    private Employee selectedEmployee;
    private Driver driver;

    private static final String[] ITEM_TYPES = {"Table", "Chair", "Coffee"};
    private static final int[] ITEM_PRICES = {50, 30, 20, 30, 25};

    private static final String[] CAT_TYPES = {"Cat1", "Cat2", "Cat3"};
    private static final int[] CAT_PRICES = {80, 90, 100};

    private static final String[] EMPLOYEE_TYPES = {"Waiter", "Chef", "Cleaner"};
    private static final int[] EMPLOYEE_PRICES = {100, 120, 80};

    public Shop(Font sherryFont, double money, MoneyPanel moneyPanel, ArrayList<Item> items, ArrayList<Cat> cats, ArrayList<Employee> employees, Driver driver) {
        this.sherryFont = sherryFont;
        this.money = money;
        this.moneyPanel = moneyPanel;
        this.items = items;
        this.cats = cats;
        this.employees = employees;
        this.driver = driver;
        setupShopFrame();
    }

    private void setupShopFrame() {
        shopFrame = new JFrame("Shop");
        shopFrame.setSize(600, 400);
        shopFrame.setLocationRelativeTo(null);
        shopFrame.setResizable(false);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(sherryFont);

        tabbedPane.addTab("Items", createShopPanel(ITEM_TYPES, ITEM_PRICES));
        tabbedPane.addTab("Cats", createShopPanel(CAT_TYPES, CAT_PRICES));
        tabbedPane.addTab("Employees", createShopPanel(EMPLOYEE_TYPES, EMPLOYEE_PRICES));

        shopFrame.add(tabbedPane);
        shopFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private JPanel createShopPanel(String[] items, int[] prices) {
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
        handleItemPurchase(command);
    }

    private void handleItemPurchase(String command) {
        int price = 0;
        if (isItem(command)) {
            price = getPrice(command, ITEM_TYPES, ITEM_PRICES);
            if (money >= price) {
                money -= price;
                selectedItem = new Item(command, Color.YELLOW, -50, -50);
                items.add(selectedItem);
                shopFrame.setVisible(false);
                moneyPanel.setMoney(money);
                driver.setSelectedItem(selectedItem);
            } else {
                showInsufficientFundsMessage();
            }
        } else if (isCat(command)) {
            price = getPrice(command, CAT_TYPES, CAT_PRICES);
            if (money >= price) {
                money -= price;
                selectedCat = new Cat(-50, -50, Color.YELLOW, "Happy");
                cats.add(selectedCat);
                shopFrame.setVisible(false);
                moneyPanel.setMoney(money);
                driver.setSelectedCat(selectedCat);
            } else {
                showInsufficientFundsMessage();
            }
        } else if (isEmployee(command)) {
            price = getPrice(command, EMPLOYEE_TYPES, EMPLOYEE_PRICES);
            if (money >= price) {
                money -= price;
                selectedEmployee = new Employee("New Employee", command, -50, -50);
                employees.add(selectedEmployee);
                shopFrame.setVisible(false);
                moneyPanel.setMoney(money);
                driver.setSelectedEmployee(selectedEmployee);
            } else {
                showInsufficientFundsMessage();
            }
        }
    }

    private boolean isItem(String command) {
        return contains(command, ITEM_TYPES);
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

    private int getPrice(String command, String[] types, int[] prices) {
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals(command)) {
                return prices[i];
            }
        }
        return 0;
    }

    private void showInsufficientFundsMessage() {
        JOptionPane.showMessageDialog(shopFrame, "You are broke (I only have $3).");
    }
}
