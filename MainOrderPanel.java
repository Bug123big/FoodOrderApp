import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainOrderPanel extends JPanel {

    private Order order;
    private Menu menu;
    private JPanel menuPanel;
    private JLabel bottomPriceButton;
    private DefaultListModel<String> orderListModel;
    private JList<String> orderList;
    private ArrayList<Object> realItems = new ArrayList<>();
    private JButton payButton;
    private final Color DARK_RED = new Color(90, 15, 15);
    private final Color DEEP_RED = new Color(130, 25, 20);
    private final Color GOLD = new Color(218, 165, 32);
    private final Color LIGHT_GOLD = new Color(255, 230, 160);
    private final Color CREAM = new Color(255, 248, 220);

    private static final double TAX_RATE = 0.13;
    private ArrayList<OrderFood> comboFood = new ArrayList<>();

    public MainOrderPanel(Order order) {
        this.order = order;
        this.menu = new Menu();

        setLayout(new BorderLayout());

        add(createCategoryPanel(), BorderLayout.WEST);

        menuPanel = new JPanel(new GridLayout(0, 3, 15, 15));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        add(scrollPane, BorderLayout.CENTER);
        add(createOrderSidePanel(), BorderLayout.EAST);
        bottomPriceButton = new JLabel("", SwingConstants.CENTER);
        bottomPriceButton.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        bottomPriceButton.setOpaque(true);
        bottomPriceButton.setBackground(DEEP_RED);
        bottomPriceButton.setForeground(GOLD);
        bottomPriceButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        bottomPriceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        bottomPriceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showOrderListWindow();
            }
        });
        add(bottomPriceButton, BorderLayout.SOUTH);
        showEntrees();
        updateBottomPrice();
    }

    private JPanel createCategoryPanel() {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(0, 1, 10, 10));
        categoryPanel.setPreferredSize(new Dimension(130, 0));
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        categoryPanel.setBackground(DARK_RED);
        categoryPanel.setBorder(BorderFactory.createLineBorder(GOLD, 1));

        JButton entreeButton = new JButton("Entrees");
        JButton dessertButton = new JButton("Dessert");
        JButton drinkButton = new JButton("Drink");
        JButton comboButton = new JButton("Combo");
        styleButton(entreeButton);
        styleButton(dessertButton);
        styleButton(drinkButton);
        styleButton(comboButton);

        entreeButton.addActionListener(e -> showEntrees());
        dessertButton.addActionListener(e -> showDesserts());
        drinkButton.addActionListener(e -> showDrinks());
        comboButton.addActionListener(e -> showComboPanel());

        categoryPanel.add(entreeButton);
        categoryPanel.add(dessertButton);
        categoryPanel.add(drinkButton);
        categoryPanel.add(comboButton);

        return categoryPanel;
    }

    private void clearMenu() {
        menuPanel.removeAll();
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void showEntrees() {
        showMenuByType("entree");
    }

    private void showDesserts() {
        showMenuByType("dessert");
    }

    private void showDrinks() {
        showMenuByType("drink");
    }

    private JPanel createOrderSidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(
                                new Color(180, 30, 30),
                                3),
                        "Order List",
                        0,
                        0,
                        new Font("Avenir Next", Font.BOLD, 24),
                        new Color(180, 30, 30)));

        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        orderList.setBackground(Color.WHITE);
        orderList.setForeground(new Color(120, 20, 20));
        orderList.setFont(
                new Font("Avenir Next", Font.PLAIN, 17));
        orderList.setFixedCellHeight(32);
        orderList.setSelectionBackground(
                new Color(255, 220, 220));
        orderList.setSelectionForeground(
                new Color(150, 20, 20));

        JButton deleteButton = new JButton("Delete Selected Item");
        deleteButton.setForeground(DARK_RED);

        deleteButton.addActionListener(e -> {
            int index = orderList.getSelectedIndex();

            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Please select an item to delete.");
                return;
            }

            Object selectedItem = realItems.get(index);

            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this, "Please select a food or combo item.");
                return;
            }

            if (selectedItem instanceof OrderFood) {
                order.deleteSingleFood((OrderFood) selectedItem);
            } else if (selectedItem instanceof Combo) {
                order.deleteCombo((Combo) selectedItem);
            }

            updateOrderSidePanel();
            updateBottomPrice();
        });

        payButton = new JButton("Pay");
        payButton.setForeground(DARK_RED);
        payButton.addActionListener(e -> showPaymentWindow(null));

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(deleteButton);
        bottomPanel.add(payButton);

        JScrollPane scrollPane = new JScrollPane(orderList);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        updateOrderSidePanel();

        return panel;
    }

    private void updateOrderSidePanel() {
        orderListModel.clear();
        realItems.clear();

        loadOrderList(orderListModel, realItems);

        double finalWithTax = calculateFinalWithTax();

        if (finalWithTax <= 0) {
            payButton.setEnabled(false);
            payButton.setText("Pay");
            payButton.setBackground(Color.GRAY);
            payButton.setForeground(Color.WHITE);
        } else {
            payButton.setForeground(DARK_RED);
            payButton.setEnabled(true);
            payButton.setText(String.format("Pay $%.2f", finalWithTax));
        }
    }

    private void styleButton(JButton button) {

        button.setBackground(new Color(140, 20, 20));

        button.setForeground(new Color(255, 215, 120));

        button.setFont(
                new Font("Avenir Next", Font.BOLD, 18));

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(255, 215, 120),
                        2));
    }
    private void styleButtonRED(JButton button) {

        button.setBackground(new Color(140, 20, 20));

        button.setForeground(DARK_RED);

        button.setFont(
                new Font("Avenir Next", Font.BOLD, 18));

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createLineBorder(
                        DARK_RED));
    }

    private double calculateTax() {
        return order.calculateFinal() * TAX_RATE;
    }

    private double calculateFinalWithTax() {
        return order.calculateFinal() + calculateTax();
    }

    private void showMenuByType(String type) {
        clearMenu();

        menuPanel.setLayout(new GridLayout(0, 3, 12, 12));

        for (FoodStorage item : menu.getItemsByType(type)) {
            addFoodCard(item);
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void addFoodCard(FoodStorage item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(DARK_RED);
        card.setBorder(BorderFactory.createLineBorder(DARK_RED, 2));
        java.io.File file = new java.io.File(item.getImagePath());

        System.out.println(item.getName());
        System.out.println(item.getImagePath());
        System.out.println(file.exists());
        ImageIcon icon = new ImageIcon(item.getImagePath());

        Image scaledImage = icon.getImage().getScaledInstance(
                150,
                120,
                Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));

        JLabel priceLabel = new JLabel(
                String.format("$%.2f", item.getPrice()),
                SwingConstants.CENTER);

        JButton addButton = new JButton("Add");

        addButton.addActionListener(e -> {
            OrderFood food = new OrderFood(
                    item.getName(),
                    item.getPrice(),
                    1,
                    item.getType());

            order.addSingleFood(food);
            updateBottomPrice();
        });

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        menuPanel.add(card);
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void showComboPanel() {
        comboFood.clear();
        showComboEntreeStep();
    }

    private void showComboEntreeStep() {
        clearMenu();
        menuPanel.setLayout(new BorderLayout());

        JPanel page = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Combo Step 1: Choose Entrees", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 24));

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));

        JLabel countLabel = new JLabel("Entrees selected: 0", SwingConstants.CENTER);

        JButton nextButton = new JButton("Next: Choose Desserts");
        nextButton.setVisible(false);
        styleButtonRED(nextButton);

        for (FoodStorage item : menu.getItemsByType("entree")) {
            JPanel card = createComboFoodCard(
                    item,
                    "adult entree",
                    countLabel,
                    nextButton,
                    "entree");

            foodGrid.add(card);
        }

        nextButton.addActionListener(e -> showComboDessertStep());

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.add(countLabel);
        bottom.add(nextButton);

        page.add(title, BorderLayout.NORTH);
        page.add(new JScrollPane(foodGrid), BorderLayout.CENTER);
        page.add(bottom, BorderLayout.SOUTH);

        menuPanel.add(page, BorderLayout.CENTER);
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void showComboDessertStep() {
        clearMenu();
        menuPanel.setLayout(new BorderLayout());

        JPanel page = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Combo Step 2: Choose Desserts", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 24));

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));

        JLabel countLabel = new JLabel("", SwingConstants.CENTER);

        JButton nextButton = new JButton("Next: Choose Drinks");
        nextButton.setVisible(false);
        styleButtonRED(nextButton);

        for (FoodStorage item : menu.getItemsByType("dessert")) {
            JPanel card = createComboFoodCard(
                    item,
                    "dessert",
                    countLabel,
                    nextButton,
                    "dessert");

            foodGrid.add(card);
        }

        updateComboStepLabel(countLabel, "dessert");
        updateComboButtonVisibility(nextButton, "dessert");

        nextButton.addActionListener(e -> showComboDrinkStep());

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.add(countLabel);
        bottom.add(nextButton);

        page.add(title, BorderLayout.NORTH);
        page.add(new JScrollPane(foodGrid), BorderLayout.CENTER);
        page.add(bottom, BorderLayout.SOUTH);

        menuPanel.add(page, BorderLayout.CENTER);
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void showComboDrinkStep() {
        clearMenu();
        menuPanel.setLayout(new BorderLayout());

        JPanel page = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Combo Step 3: Choose Drinks", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 24));

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 15, 15));

        JLabel countLabel = new JLabel("", SwingConstants.CENTER);

        JButton orderButton = new JButton("Order Combo");
        orderButton.setVisible(false);
        styleButtonRED(orderButton);

        for (FoodStorage item : menu.getItemsByType("drink")) {
            JPanel card = createComboFoodCard(
                    item,
                    "drink",
                    countLabel,
                    orderButton,
                    "drink");

            foodGrid.add(card);
        }

        updateComboStepLabel(countLabel, "drink");
        updateComboButtonVisibility(orderButton, "drink");

        orderButton.addActionListener(e -> {
            try {
                ArrayList<OrderFood> finalComboFood = new ArrayList<>(comboFood);

                Combo combo = new Combo(finalComboFood);
                order.addCombo(combo);

                comboFood.clear();

                updateBottomPrice();

                JOptionPane.showMessageDialog(this, "Combo added to order!");

                showEntrees();

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Invalid combo. Entrees, desserts, and drinks must have the same amount.",
                        "Combo Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.add(countLabel);
        bottom.add(orderButton);

        page.add(title, BorderLayout.NORTH);
        page.add(new JScrollPane(foodGrid), BorderLayout.CENTER);
        page.add(bottom, BorderLayout.SOUTH);

        menuPanel.add(page, BorderLayout.CENTER);
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private JPanel createComboFoodCard(
            FoodStorage item,
            String type,
            JLabel countLabel,
            JButton nextOrOrderButton,
            String step) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(DARK_RED);

        card.setBorder(
                BorderFactory.createLineBorder( DARK_RED,3));

        ImageIcon icon = new ImageIcon(item.getImagePath());

        Image scaledImage = icon.getImage().getScaledInstance(
                150,
                120,
                Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Avenir Next", Font.BOLD, 18));
        nameLabel.setForeground(DARK_RED);

        JLabel priceLabel = new JLabel(String.format("$%.2f", item.getPrice()), SwingConstants.CENTER);
        priceLabel.setForeground(DARK_RED);

        JLabel amountLabel = new JLabel("Selected: 0", SwingConstants.CENTER);

        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");
        addButton.setForeground(DARK_RED);
        removeButton.setForeground(DARK_RED);

        addButton.addActionListener(e -> {
            OrderFood food = new OrderFood(
                    item.getName(),
                    item.getPrice(),
                    1,
                    type);

            comboFood.add(food);

            amountLabel.setText("Selected: " + countSpecificComboFood(item.getName(), type));
            updateComboStepLabel(countLabel, step);
            updateComboButtonVisibility(nextOrOrderButton, step);
        });

        removeButton.addActionListener(e -> {
            removeOneComboFood(item.getName(), type);

            amountLabel.setText("Selected: " + countSpecificComboFood(item.getName(), type));
            updateComboStepLabel(countLabel, step);
            updateComboButtonVisibility(nextOrOrderButton, step);
        });

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(amountLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.add(removeButton);
        buttonPanel.add(addButton);

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private int countSpecificComboFood(String name, String type) {
        int count = 0;

        for (OrderFood food : comboFood) {
            if (food.getName().equals(name) && food.getType().equals(type)) {
                count++;
            }
        }

        return count;
    }

    private void removeOneComboFood(String name, String type) {
        for (int i = 0; i < comboFood.size(); i++) {
            OrderFood food = comboFood.get(i);

            if (food.getName().equals(name) && food.getType().equals(type)) {
                comboFood.remove(i);
                return;
            }
        }
    }

    private int countComboType(String type) {
        int count = 0;

        for (OrderFood food : comboFood) {
            if (food.getType().equals(type)) {
                count++;
            }
        }

        return count;
    }

    private int countComboEntrees() {
        return countComboType("adult entree")
                + countComboType("child entree")
                + countComboType("entree");
    }

    private void updateComboStepLabel(JLabel label, String step) {
        int entreeCount = countComboEntrees();
        int dessertCount = countComboType("dessert");
        int drinkCount = countComboType("drink");

        if (step.equals("entree")) {
            label.setText("Entrees selected: " + entreeCount);
        } else if (step.equals("dessert")) {
            label.setText("Entrees: " + entreeCount + "    Desserts: " + dessertCount);
        } else if (step.equals("drink")) {
            label.setText("Entrees: " + entreeCount + "    Drinks: " + drinkCount);
        }
    }

    private void updateComboButtonVisibility(JButton button, String step) {
        int entreeCount = countComboEntrees();
        int dessertCount = countComboType("dessert");
        int drinkCount = countComboType("drink");

        if (step.equals("entree")) {
            button.setVisible(entreeCount > 0);
        } else if (step.equals("dessert")) {
            button.setVisible(dessertCount == entreeCount && entreeCount > 0);
        } else if (step.equals("drink")) {
            button.setVisible(drinkCount == entreeCount && entreeCount > 0);
        }
    }

    private void updateBottomPrice() {
        double original = order.calculateOriginal();
        double beforeTax = order.calculateFinal();
        double tax = calculateTax();
        double finalWithTax = calculateFinalWithTax();

        bottomPriceButton.setText(
                String.format(
                        "Original: $%.2f     Before Tax: $%.2f     Tax: $%.2f     Final: $%.2f",
                        original,
                        beforeTax,
                        tax,
                        finalWithTax));

        if (orderListModel != null) {
            updateOrderSidePanel();
        }
    }

    private void showOrderListWindow() {
        JFrame frame = new JFrame("Order List");
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(this);
        frame.setLayout(new BorderLayout());

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(listModel);

        ArrayList<Object> realItems = new ArrayList<>();

        loadOrderList(listModel, realItems);

        JButton deleteButton = new JButton("Delete Selected Item");

        deleteButton.addActionListener(e -> {
            int index = orderList.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please select an item to delete.");
                return;
            }
            Object selectedItem = realItems.get(index);
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please select a food or combo item.");
                return;
            }
            if (selectedItem instanceof OrderFood) {
                order.deleteSingleFood((OrderFood) selectedItem);
            } else if (selectedItem instanceof Combo) {
                order.deleteCombo((Combo) selectedItem);
            }
            listModel.clear();
            realItems.clear();
            loadOrderList(listModel, realItems);
            updateBottomPrice();
        });

        JButton payButton = new JButton("Pay");

        payButton.addActionListener(e -> {
            showPaymentWindow(frame);
        });

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(deleteButton);
        bottomPanel.add(payButton);

        frame.add(new JScrollPane(orderList), BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void loadOrderList(DefaultListModel<String> listModel, ArrayList<Object> realItems) {
        listModel.addElement("----- Single Items -----");
        realItems.add(null);

        for (OrderFood food : order.getSingleFoods()) {
            listModel.addElement(
                    "[Single] " + food.getName() + " - $" + String.format("%.2f", food.getPrice()));
            realItems.add(food);
            listModel.addElement(" ");
            realItems.add(null);
        }

        listModel.addElement("----- Combo Items -----");
        realItems.add(null);

        int comboNumber = 1;

        for (Combo combo : order.getCombos()) {
            listModel.addElement(
                    "[Combo " + comboNumber + "] Original: $" + String.format("%.2f", combo.getOriginalComboPrice())
                            + " Final: $" + String.format("%.2f", combo.getFinalComboPrice()));
            realItems.add(combo);
            for (OrderFood food : combo.getFoods()) {
                listModel.addElement(
                        "    • " + food.getName()
                                + " (" + food.getType() + ")"
                                + " - $" + String.format("%.2f", food.getPrice()));
                realItems.add(null);
            }
            comboNumber++;
        }
        listModel.addElement("----- Total -----");
        realItems.add(null);
        listModel.addElement("Original Total: $" + String.format("%.2f", order.calculateOriginal()));
        realItems.add(null);
        listModel.addElement("Before Tax: $" + String.format("%.2f", order.calculateFinal()));
        realItems.add(null);
        listModel.addElement("Tax: $" + String.format("%.2f", calculateTax()));
        realItems.add(null);
        listModel.addElement("Final Total: $" + String.format("%.2f", calculateFinalWithTax()));
        realItems.add(null);
    }

    private void showPaymentWindow(JFrame orderFrame) {
        JFrame paymentFrame = new JFrame("Payment");
        paymentFrame.setSize(400, 300);
        paymentFrame.setLocationRelativeTo(this);
        paymentFrame.setLayout(new GridLayout(0, 1, 10, 10));

        JLabel title = new JLabel("Choose Payment Method", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 22));

        JLabel totalLabel = new JLabel(
                String.format("Total: $%.2f", calculateFinalWithTax()),
                SwingConstants.CENTER);

        JButton creditButton = new JButton("Credit / Debit Card");
        JButton cashButton = new JButton("Cash");
        JButton etransferButton = new JButton("E-transfer");

        creditButton.addActionListener(e -> finishPayment(paymentFrame, orderFrame, "Credit / Debit Card"));
        cashButton.addActionListener(e -> finishPayment(paymentFrame, orderFrame, "Cash"));
        etransferButton.addActionListener(e -> finishPayment(paymentFrame, orderFrame, "E-transfer"));

        paymentFrame.add(title);
        paymentFrame.add(totalLabel);
        paymentFrame.add(creditButton);
        paymentFrame.add(cashButton);
        paymentFrame.add(etransferButton);

        paymentFrame.setVisible(true);
    }

    private void finishPayment(JFrame paymentFrame, JFrame orderFrame, String method) {
        int orderNumber = (int) (Math.random() * 9000) + 1000;

        JOptionPane.showMessageDialog(
                this,
                "Payment Successful!\n" +
                        "Payment Method: " + method + "\n" +
                        "Order Number: " + orderNumber + "\n" +
                        String.format("Total Paid: $%.2f", order.calculateFinal()),
                "Payment Complete",
                JOptionPane.INFORMATION_MESSAGE);

        paymentFrame.dispose();
        orderFrame.dispose();

        order.clearOrder();
        updateBottomPrice();
    }
}