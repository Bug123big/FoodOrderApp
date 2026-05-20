import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainOrderPanel extends JPanel {

    private Order order;
    private Menu menu;

    private JPanel menuPanel;
    private JLabel bottomPriceButton;
    private OrderSidePanel orderSidePanel;

    private ArrayList<OrderFood> comboFood = new ArrayList<>();

    private static final double TAX_RATE = 0.13;

    public MainOrderPanel(Order order) {
        this.order = order;
        this.menu = new Menu();

        setLayout(new BorderLayout());
        setBackground(UIStyle.DARK_RED);

        add(new CategoryPanel(
                () -> showTodaySpecials(),
                () -> showEntrees(),
                () -> showDesserts(),
                () -> showDrinks(),
                () -> showComboPanel()), BorderLayout.WEST);

        menuPanel = new JPanel(new GridLayout(0, 3, 12, 12));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setBackground(UIStyle.DARK_RED);

        add(new JScrollPane(menuPanel), BorderLayout.CENTER);

        orderSidePanel = new OrderSidePanel(
                order,
                () -> updateBottomPrice(),
                () -> calculateFinalWithTax());

        add(orderSidePanel, BorderLayout.EAST);

        bottomPriceButton = new JLabel("", SwingConstants.CENTER);
        bottomPriceButton.setFont(new Font("Helvetica Neue", Font.BOLD, 18));
        bottomPriceButton.setOpaque(true);
        bottomPriceButton.setBackground(UIStyle.DEEP_RED);
        bottomPriceButton.setForeground(UIStyle.GOLD);
        bottomPriceButton.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(bottomPriceButton, BorderLayout.SOUTH);

        showEntrees();
        updateBottomPrice();
    }

    private void clearMenu() {
        menuPanel.removeAll();
        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private double calculateTax() {
        return order.calculateFinal() * TAX_RATE;
    }

    private double calculateFinalWithTax() {
        return order.calculateFinal() + calculateTax();
    }

    private void updateBottomPrice() {
        bottomPriceButton.setText(
                String.format(
                        "Original: $%.2f     Before Tax: $%.2f     Tax: $%.2f     Final: $%.2f",
                        order.calculateOriginal(),
                        order.calculateFinal(),
                        calculateTax(),
                        calculateFinalWithTax()));

        if (orderSidePanel != null) {
            orderSidePanel.refresh();
        }
    }

    private void showTodaySpecials() {
        clearMenu();

        menuPanel.setLayout(new GridLayout(0, 3, 12, 12));

        for (FoodStorage item : menu.getTodaySpecials()) {
            addFoodCard(item);
        }

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

        menuPanel.setLayout(new GridLayout(0, 3, 12, 12));

        for (FoodStorage item : menu.getItemsByType(type)) {
            addFoodCard(item);
        }

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private void addFoodCard(FoodStorage item) {
        boolean isSpecialToday = menu.isTodaySpecial(item);
        double finalPrice = menu.getFinalPrice(item);
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UIStyle.DARK_RED);
        card.setBorder(BorderFactory.createLineBorder(UIStyle.GOLD, 2));

        JLabel imageLabel = createImageLabel(item.getImagePath());

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Helvetica Neue", Font.BOLD, 16));
        nameLabel.setForeground(UIStyle.LIGHT_GOLD);

        JLabel priceLabel;

        if (isSpecialToday) {

            priceLabel = new JLabel(
                    "<html><center>"
                            + "LIMITED SPECIAL<br>"
                            + "<strike>$"
                            + String.format("%.2f", item.getPrice())
                            + "</strike>   $"
                            + String.format("%.2f", finalPrice)
                            + "</center></html>",
                    SwingConstants.CENTER);

        } else {

            priceLabel = new JLabel(
                    String.format("$%.2f", item.getPrice()),
                    SwingConstants.CENTER);
        }
        priceLabel.setForeground(UIStyle.LIGHT_GOLD);

        JButton addButton = new JButton("Add");

        UIStyle.styleGoldButton(addButton);

        if (isSpecialToday && order.hasLimitedSpecial(item.getName())) {

            addButton.setEnabled(false);

            addButton.setBackground(Color.GRAY);

            addButton.setForeground(Color.WHITE);

            addButton.setText("Sold Out");
        }

        addButton.addActionListener(e -> {

            if (isSpecialToday && order.hasLimitedSpecial(item.getName())) {
                return;
            }

            OrderFood food;

            String type = item.getType().toLowerCase();

            if (type.equals("entree")) {

                JPanel panel = new JPanel(new BorderLayout(10, 10));
                panel.setBackground(UIStyle.CREAM);
                panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                JLabel title = new JLabel("Choose Entree Type", SwingConstants.CENTER);
                title.setFont(new Font("Avenir Next", Font.BOLD, 22));
                title.setForeground(UIStyle.DARK_RED);

                JCheckBox childCheckBox = new JCheckBox("Child Food  35% off");
                childCheckBox.setFont(new Font("Avenir Next", Font.BOLD, 18));
                childCheckBox.setForeground(UIStyle.DARK_RED);
                childCheckBox.setBackground(UIStyle.CREAM);
                childCheckBox.setFocusPainted(false);

                panel.add(title, BorderLayout.NORTH);
                panel.add(childCheckBox, BorderLayout.CENTER);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Entree Option",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                boolean isChildFood = childCheckBox.isSelected();

                food = new OrderEntree(
                        item.getName(),
                        finalPrice,
                        1,
                        isChildFood,
                        isSpecialToday);

            } else if (type.equals("drink")) {

                JPanel panel = new JPanel(new BorderLayout(10, 10));
                panel.setBackground(UIStyle.CREAM);
                panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

                JLabel title = new JLabel("Choose Drink Size", SwingConstants.CENTER);
                title.setFont(new Font("Avenir Next", Font.BOLD, 22));
                title.setForeground(UIStyle.DARK_RED);

                JPanel optionPanel = new JPanel(new GridLayout(0, 1, 8, 8));
                optionPanel.setBackground(UIStyle.CREAM);

                JRadioButton small = new JRadioButton("Small");
                JRadioButton medium = new JRadioButton("Medium  +$1.50");
                JRadioButton large = new JRadioButton("Large  +$3.00");

                small.setSelected(true);

                ButtonGroup group = new ButtonGroup();
                group.add(small);
                group.add(medium);
                group.add(large);

                JRadioButton[] buttons = { small, medium, large };

                for (JRadioButton b : buttons) {
                    b.setFont(new Font("Avenir Next", Font.BOLD, 18));
                    b.setForeground(UIStyle.DARK_RED);
                    b.setBackground(UIStyle.CREAM);
                    b.setFocusPainted(false);
                    optionPanel.add(b);
                }

                panel.add(title, BorderLayout.NORTH);
                panel.add(optionPanel, BorderLayout.CENTER);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Drink Size",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (result != JOptionPane.OK_OPTION) {
                    return;
                }

                int size;

                if (small.isSelected()) {
                    size = 0;
                } else if (medium.isSelected()) {
                    size = 1;
                } else {
                    size = 2;
                }

                food = new OrderDrink(
                        item.getName(),
                        finalPrice,
                        1,
                        size,
                        isSpecialToday);
            } else {

                food = new OrderFood(
                        item.getName(),
                        item.getPrice(),
                        1,
                        item.getType(),
                        isSpecialToday);
            }

            order.addSingleFood(food);
            updateBottomPrice();

            if (isSpecialToday) {
                showTodaySpecials();
            } else {
                showMenuByType(item.getType());
            }
        });

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(UIStyle.DARK_RED);
        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        menuPanel.add(card);
    }

    private JLabel createImageLabel(String imagePath) {
        ImageIcon icon = new ImageIcon(imagePath);

        Image scaledImage = icon.getImage().getScaledInstance(
                150,
                120,
                Image.SCALE_SMOOTH);

        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        return imageLabel;
    }

    private void showComboPanel() {
        comboFood.clear();
        showComboEntreeStep();
    }

    private void showComboEntreeStep() {
        clearMenu();
        menuPanel.setLayout(new BorderLayout());

        JPanel page = createComboPage("Combo Step 1: Choose Entrees");

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        foodGrid.setBackground(UIStyle.DARK_RED);

        JLabel countLabel = createComboCountLabel("Entrees selected: 0");

        JButton nextButton = new JButton("Next: Choose Desserts");
        nextButton.setVisible(false);
        UIStyle.styleRedTextButton(nextButton);

        for (FoodStorage item : menu.getItemsByType("entree")) {
            foodGrid.add(createComboFoodCard(
                    item,
                    "adult entree",
                    countLabel,
                    nextButton,
                    "entree"));
        }

        nextButton.addActionListener(e -> showComboDessertStep());

        addComboPageContent(page, foodGrid, countLabel, nextButton);
    }

    private void showComboDessertStep() {
        clearMenu();
        menuPanel.setLayout(new BorderLayout());

        JPanel page = createComboPage("Combo Step 2: Choose Desserts");

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        foodGrid.setBackground(UIStyle.DARK_RED);

        JLabel countLabel = createComboCountLabel("");

        JButton nextButton = new JButton("Next: Choose Drinks");
        nextButton.setVisible(false);
        UIStyle.styleRedTextButton(nextButton);

        for (FoodStorage item : menu.getItemsByType("dessert")) {
            foodGrid.add(createComboFoodCard(
                    item,
                    "dessert",
                    countLabel,
                    nextButton,
                    "dessert"));
        }

        updateComboStepLabel(countLabel, "dessert");
        updateComboButtonVisibility(nextButton, "dessert");

        nextButton.addActionListener(e -> showComboDrinkStep());

        addComboPageContent(page, foodGrid, countLabel, nextButton);
    }

    private void showComboDrinkStep() {
        clearMenu();
        menuPanel.setLayout(new BorderLayout());

        JPanel page = createComboPage("Combo Step 3: Choose Drinks");

        JPanel foodGrid = new JPanel(new GridLayout(0, 3, 12, 12));
        foodGrid.setBackground(UIStyle.DARK_RED);

        JLabel countLabel = createComboCountLabel("");

        JButton orderButton = new JButton("Order Combo");
        orderButton.setVisible(false);
        UIStyle.styleRedTextButton(orderButton);

        for (FoodStorage item : menu.getItemsByType("drink")) {
            foodGrid.add(createComboFoodCard(
                    item,
                    "drink",
                    countLabel,
                    orderButton,
                    "drink"));
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

        addComboPageContent(page, foodGrid, countLabel, orderButton);
    }

    private JPanel createComboPage(String titleText) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(UIStyle.DARK_RED);

        JLabel title = new JLabel(titleText, SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 24));
        title.setForeground(UIStyle.DARK_RED);
        title.setOpaque(true);
        title.setBackground(UIStyle.CREAM);

        page.add(title, BorderLayout.NORTH);

        return page;
    }

    private JLabel createComboCountLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Avenir Next", Font.BOLD, 18));
        label.setForeground(UIStyle.DARK_RED);
        label.setOpaque(true);
        label.setBackground(UIStyle.CREAM);
        return label;
    }

    private void addComboPageContent(
            JPanel page,
            JPanel foodGrid,
            JLabel countLabel,
            JButton button) {
        JPanel bottom = new JPanel(new GridLayout(2, 1));
        bottom.setBackground(UIStyle.CREAM);
        bottom.add(countLabel);
        bottom.add(button);

        JScrollPane scrollPane = new JScrollPane(foodGrid);
        scrollPane.getViewport().setBackground(UIStyle.DARK_RED);

        page.add(scrollPane, BorderLayout.CENTER);
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
        card.setBackground(UIStyle.CREAM);
        card.setBorder(BorderFactory.createLineBorder(UIStyle.DARK_RED, 3));

        JLabel imageLabel = createImageLabel(item.getImagePath());

        JLabel nameLabel = new JLabel(item.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Avenir Next", Font.BOLD, 18));
        nameLabel.setForeground(UIStyle.DARK_RED);

        JLabel priceLabel = new JLabel(
                String.format("$%.2f", item.getPrice()),
                SwingConstants.CENTER);
        priceLabel.setForeground(UIStyle.DARK_RED);

        JLabel amountLabel = new JLabel("Selected: 0", SwingConstants.CENTER);
        amountLabel.setForeground(UIStyle.DARK_RED);

        JButton addButton = new JButton("+");
        JButton removeButton = new JButton("-");

        UIStyle.styleRedTextButton(addButton);
        UIStyle.styleRedTextButton(removeButton);

        addButton.addActionListener(e -> {

            OrderFood food;

            if (type.equals("adult entree")) {

                boolean isChildFood = showChildFoodDialog();

                food = new OrderEntree(
                        item.getName(),
                        item.getPrice(),
                        1,
                        isChildFood);

            } else if (type.equals("drink")) {

                int size = showDrinkSizeDialog();

                if (size == -1) {
                    return;
                }

                food = new OrderDrink(
                        item.getName(),
                        item.getPrice(),
                        1,
                        size);

            } else {

                food = new OrderFood(
                        item.getName(),
                        item.getPrice(),
                        1,
                        type);
            }

            comboFood.add(food);

            amountLabel.setText(
                    "Selected: " + countSpecificComboFood(item.getName(), food.getType()));

            updateComboStepLabel(countLabel, step);
            updateComboButtonVisibility(nextOrOrderButton, step);
        });

        removeButton.addActionListener(e -> {
            removeOneComboFood(item.getName(), type);

            amountLabel.setText(
                    "Selected: " + countSpecificComboFood(item.getName(), type));

            updateComboStepLabel(countLabel, step);
            updateComboButtonVisibility(nextOrOrderButton, step);
        });

        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(UIStyle.CREAM);
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

    private boolean showChildFoodDialog() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.CREAM);

        JLabel title = new JLabel("Choose Entree Type", SwingConstants.CENTER);
        title.setFont(new Font("Avenir Next", Font.BOLD, 20));
        title.setForeground(UIStyle.DARK_RED);

        JCheckBox childCheckBox = new JCheckBox("Child Food  35% off");
        childCheckBox.setFont(new Font("Avenir Next", Font.BOLD, 16));
        childCheckBox.setForeground(UIStyle.DARK_RED);
        childCheckBox.setBackground(UIStyle.CREAM);

        panel.add(title, BorderLayout.NORTH);
        panel.add(childCheckBox, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Entree Option",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return false;
        }

        return childCheckBox.isSelected();
    }

    private int showDrinkSizeDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 8, 8));
        panel.setBackground(UIStyle.CREAM);

        JLabel title = new JLabel("Choose Drink Size", SwingConstants.CENTER);
        title.setFont(new Font("Avenir Next", Font.BOLD, 20));
        title.setForeground(UIStyle.DARK_RED);

        JRadioButton small = new JRadioButton("Small");
        JRadioButton medium = new JRadioButton("Medium  +$1.50");
        JRadioButton large = new JRadioButton("Large  +$3.00");

        small.setSelected(true);

        ButtonGroup group = new ButtonGroup();
        group.add(small);
        group.add(medium);
        group.add(large);

        JRadioButton[] buttons = { small, medium, large };

        for (JRadioButton b : buttons) {
            b.setFont(new Font("Avenir Next", Font.BOLD, 16));
            b.setForeground(UIStyle.DARK_RED);
            b.setBackground(UIStyle.CREAM);
            panel.add(b);
        }

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setBackground(UIStyle.CREAM);
        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(panel, BorderLayout.CENTER);

        int result = JOptionPane.showConfirmDialog(
                this,
                wrapper,
                "Drink Size",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return -1;
        }

        if (small.isSelected()) {
            return 0;
        } else if (medium.isSelected()) {
            return 1;
        } else {
            return 2;
        }
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
}