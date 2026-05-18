
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
public class MainOrderPanel extends JPanel {

    private Order order;
    private Menu menu;
    private JPanel menuPanel;
    private JLabel bottomPriceButton;
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

        bottomPriceButton = new JLabel("", SwingConstants.CENTER);
        bottomPriceButton.setFont(new Font("Arial", Font.BOLD, 22));
        bottomPriceButton.setOpaque(true);
        bottomPriceButton.setBackground(new Color(40, 160, 70));
        bottomPriceButton.setForeground(Color.WHITE);
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
        categoryPanel.setPreferredSize(new Dimension(180, 0));
        categoryPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        categoryPanel.setBackground(new Color(245, 245, 245));

        JButton entreeButton = new JButton("Entrees");
        JButton dessertButton = new JButton("Dessert");
        JButton drinkButton = new JButton("Drink");
        JButton comboButton = new JButton("Combo");

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

    private void showMenuByType(String type) {
        clearMenu();

        menuPanel.setLayout(new GridLayout(0, 3, 15, 15));

        for (FoodStorage item : menu.getItemsByType(type)) {
            addFoodCard(item);
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void addFoodCard(FoodStorage item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

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
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

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
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 15, 15));

        JLabel countLabel = new JLabel("Entrees selected: 0", SwingConstants.CENTER);

        JButton nextButton = new JButton("Next: Choose Desserts");
        nextButton.setVisible(false);

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
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 15, 15));

        JLabel countLabel = new JLabel("", SwingConstants.CENTER);

        JButton nextButton = new JButton("Next: Choose Drinks");
        nextButton.setVisible(false);

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
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 15, 15));

        JLabel countLabel = new JLabel("", SwingConstants.CENTER);

        JButton orderButton = new JButton("Order Combo");
        orderButton.setVisible(false);

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
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        JLabel imageLabel = new JLabel("Image", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 120));
        imageLabel.setOpaque(true);
        imageLabel.setBackground(new Color(230, 230, 230));

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel priceLabel = new JLabel(
                String.format("$%.2f", item.getPrice()),
                SwingConstants.CENTER);

        JButton addButton = new JButton("Add to Combo");

        addButton.addActionListener(e -> {
            OrderFood food = new OrderFood(
                    item.getName(),
                    item.getPrice(),
                    1,
                    type);

            comboFood.add(food);

            updateComboStepLabel(countLabel, step);
            updateComboButtonVisibility(nextOrOrderButton, step);
        });

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        return card;
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
        return countComboType("adult entree") + countComboType("child entree");
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
        double finalPrice = order.calculateFinal();

        bottomPriceButton.setText(
                String.format(
                        "Original: $%.2f     Final: $%.2f     Click to view order",
                        original,
                        finalPrice));
    }

    private void showOrderListWindow() {
        JFrame frame = new JFrame("Order List");
        frame.setSize(600, 500);
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
        }

        listModel.addElement("----- Combo Items -----");
        realItems.add(null);

        int comboNumber = 1;

        for (Combo combo : order.getCombos()) {
            listModel.addElement(
                    "[Combo " + comboNumber + "] Original: $" +
                            String.format("%.2f", combo.getOriginalComboPrice()) +
                            " Final: $" +
                            String.format("%.2f", combo.getFinalComboPrice()));

            realItems.add(combo);
            comboNumber++;
        }

        listModel.addElement("----- Total -----");
        realItems.add(null);

        listModel.addElement(
                "Original Total: $" + String.format("%.2f", order.calculateOriginal()));
        realItems.add(null);

        listModel.addElement(
                "Final Total: $" + String.format("%.2f", order.calculateFinal()));
        realItems.add(null);
    }

    private void showPaymentWindow(JFrame orderFrame) {
        JFrame paymentFrame = new JFrame("Payment");
        paymentFrame.setSize(400, 300);
        paymentFrame.setLocationRelativeTo(this);
        paymentFrame.setLayout(new GridLayout(0, 1, 10, 10));

        JLabel title = new JLabel("Choose Payment Method", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel totalLabel = new JLabel(
                String.format("Total: $%.2f", order.calculateFinal()),
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