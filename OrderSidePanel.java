import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Supplier;

public class OrderSidePanel extends JPanel {

    private Order order;
    private Runnable refreshMainPrice;
    private Supplier<Double> finalWithTaxSupplier;

    private DefaultListModel<String> listModel;
    private JList<String> orderList;
    private ArrayList<Object> realItems = new ArrayList<>();
    private JButton payButton;

    public OrderSidePanel(
            Order order,
            Runnable refreshMainPrice,
            Supplier<Double> finalWithTaxSupplier
    ) {
        this.order = order;
        this.refreshMainPrice = refreshMainPrice;
        this.finalWithTaxSupplier = finalWithTaxSupplier;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(360, 0));

        setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyle.DARK_RED, 3),
                "Order List",
                0,
                0,
                new Font("Avenir Next", Font.BOLD, 24),
                UIStyle.DARK_RED
        ));

        listModel = new DefaultListModel<>();
        orderList = new JList<>(listModel);
        orderList.setBackground(Color.WHITE);
        orderList.setForeground(UIStyle.DARK_RED);
        orderList.setFont(new Font("Avenir Next", Font.PLAIN, 17));
        orderList.setFixedCellHeight(32);
        orderList.setSelectionBackground(new Color(255, 220, 220));
        orderList.setSelectionForeground(UIStyle.DARK_RED);

        JButton deleteButton = new JButton("Remove");
        UIStyle.styleRedTextButton(deleteButton);
        deleteButton.addActionListener(e -> deleteSelectedItem());

        payButton = new JButton("Pay");
        UIStyle.styleRedTextButton(payButton);

        payButton.addActionListener(e -> {
            new PaymentFrame(order, finalWithTaxSupplier.get(), () -> {
                refresh();
                refreshMainPrice.run();
            });
        });

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(deleteButton);
        bottomPanel.add(payButton);

        JScrollPane scrollPane = new JScrollPane(orderList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refresh();
    }

    public void refresh() {
        listModel.clear();
        realItems.clear();

        loadOrderList();

        double total = finalWithTaxSupplier.get();

        if (total <= 0) {
            payButton.setEnabled(false);
            payButton.setText("Pay");
            payButton.setBackground(Color.GRAY);
            payButton.setForeground(Color.WHITE);
        } else {
            payButton.setEnabled(true);
            payButton.setText(String.format("Pay $%.2f", total));
            payButton.setBackground(UIStyle.CREAM);
            payButton.setForeground(UIStyle.DARK_RED);
        }
    }

    private void deleteSelectedItem() {
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

        refresh();
        refreshMainPrice.run();
    }

    private void loadOrderList() {
        listModel.addElement("----- Single Items -----");
        realItems.add(null);

        for (OrderFood food : order.getSingleFoods()) {
            listModel.addElement(
                    "[Single] " + food.getName()
                            + " - $" + String.format("%.2f", food.getPrice())
            );
            realItems.add(food);
        }

        listModel.addElement(" ");
        realItems.add(null);

        listModel.addElement("----- Combo Items -----");
        realItems.add(null);

        int comboNumber = 1;

        for (Combo combo : order.getCombos()) {
            listModel.addElement(
                    "[Combo " + comboNumber + "] Original: $"
                            + String.format("%.2f", combo.getOriginalComboPrice())
                            + " Final: $"
                            + String.format("%.2f", combo.getFinalComboPrice())
            );

            realItems.add(combo);

            for (OrderFood food : combo.getFoods()) {
                listModel.addElement(
                        "    • " + food.getName()
                                + " (" + food.getType() + ")"
                                + " - $" + String.format("%.2f", food.getPrice())
                );
                realItems.add(null);
            }

            comboNumber++;
        }

        listModel.addElement(" ");
        realItems.add(null);

        listModel.addElement(
                "Final Total: $" + String.format("%.2f", finalWithTaxSupplier.get())
        );
        realItems.add(null);
    }
}
