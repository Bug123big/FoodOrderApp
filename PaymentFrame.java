import javax.swing.*;
import java.awt.*;

public class PaymentFrame extends JFrame {

    public PaymentFrame(Order order, double finalTotal, Runnable afterPayment) {
        setTitle("Payment");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 1, 10, 10));

        JLabel title = new JLabel("Choose Payment Method", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
        title.setForeground(UIStyle.DARK_RED);

        JLabel totalLabel = new JLabel(
                String.format("Total: $%.2f", finalTotal),
                SwingConstants.CENTER
        );
        totalLabel.setForeground(UIStyle.DARK_RED);

        JButton creditButton = new JButton("Credit / Debit Card");
        JButton cashButton = new JButton("Cash");
        JButton etransferButton = new JButton("E-transfer");

        UIStyle.styleRedTextButton(creditButton);
        UIStyle.styleRedTextButton(cashButton);
        UIStyle.styleRedTextButton(etransferButton);

        creditButton.addActionListener(e ->
                finish(order, finalTotal, "Credit / Debit Card", afterPayment)
        );

        cashButton.addActionListener(e ->
                finish(order, finalTotal, "Cash", afterPayment)
        );

        etransferButton.addActionListener(e ->
                finish(order, finalTotal, "E-transfer", afterPayment)
        );

        add(title);
        add(totalLabel);
        add(creditButton);
        add(cashButton);
        add(etransferButton);

        setVisible(true);
    }

    private void finish(
            Order order,
            double finalTotal,
            String method,
            Runnable afterPayment
    ) {
        int orderNumber = (int) (Math.random() * 9000) + 1000;

        JOptionPane.showMessageDialog(
                this,
                "Payment Successful!\n"
                        + "Payment Method: " + method + "\n"
                        + "Order Number: " + orderNumber + "\n"
                        + String.format("Total Paid: $%.2f", finalTotal),
                "Payment Complete",
                JOptionPane.INFORMATION_MESSAGE
        );

        order.clearOrder();
        afterPayment.run();
        dispose();
    }
}
