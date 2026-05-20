import javax.swing.*;
import java.awt.*;

public class PaymentFrame extends JFrame {

        public PaymentFrame(JFrame mainFrame, Order order, double finalTotal, Runnable afterPayment) {
                setTitle("Payment");
                setSize(400, 300);
                setLocationRelativeTo(null);
                setLayout(new GridLayout(0, 1, 10, 10));

                JLabel title = new JLabel("Choose Payment Method", SwingConstants.CENTER);
                title.setFont(new Font("Helvetica Neue", Font.BOLD, 22));
                title.setForeground(UIStyle.DARK_RED);

                JLabel totalLabel = new JLabel(
                                String.format("Total: $%.2f", finalTotal),
                                SwingConstants.CENTER);
                totalLabel.setForeground(UIStyle.DARK_RED);

                JButton creditButton = new JButton("Credit / Debit Card");
                JButton cashButton = new JButton("Cash");
                JButton etransferButton = new JButton("E-transfer");

                UIStyle.styleRedTextButton(creditButton);
                UIStyle.styleRedTextButton(cashButton);
                UIStyle.styleRedTextButton(etransferButton);

                creditButton.addActionListener(
                                e -> finish(mainFrame, order, finalTotal, "Credit / Debit Card", afterPayment));

                cashButton.addActionListener(e -> finish(mainFrame, order, finalTotal, "Cash", afterPayment));

                etransferButton.addActionListener(
                                e -> finish(mainFrame, order, finalTotal, "E-transfer", afterPayment));

                add(title);
                add(totalLabel);
                add(creditButton);
                add(cashButton);
                add(etransferButton);

                setVisible(true);
        }

        private void finish(
                        JFrame mainFrame,
                        Order order,
                        double finalTotal,
                        String method,
                        Runnable afterPayment) {
                int orderNumber = (int) (Math.random() * 9000) + 1000;

                JPanel panel = new JPanel(new BorderLayout(15, 15));
                panel.setBackground(UIStyle.CREAM);
                panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

                JLabel title = new JLabel(
                                "PAYMENT COMPLETE",
                                SwingConstants.CENTER);

                title.setFont(new Font("Helvetica Neue", Font.BOLD, 28));
                title.setForeground(UIStyle.DARK_RED);

                JLabel successIcon = new JLabel("✓", SwingConstants.CENTER);
                successIcon.setFont(new Font("Helvetica Neue", Font.BOLD, 60));
                successIcon.setForeground(new Color(30, 140, 60));

                JTextArea info = new JTextArea(
                                "Payment Method: " + method
                                                + "\n\nOrder Number: #" + orderNumber
                                                + "\n\n"
                                                + String.format("Total Paid: $%.2f", finalTotal)
                                                + "\n\nThank you for your order!");

                info.setEditable(false);
                info.setFont(new Font("Avenir Next", Font.BOLD, 18));
                info.setForeground(UIStyle.DARK_RED);
                info.setBackground(UIStyle.CREAM);

                panel.add(title, BorderLayout.NORTH);
                panel.add(successIcon, BorderLayout.CENTER);
                panel.add(info, BorderLayout.SOUTH);

                JOptionPane.showMessageDialog(
                                this,
                                panel,
                                "Payment Complete",
                                JOptionPane.PLAIN_MESSAGE);

                order.clearOrder();
                afterPayment.run();
                mainFrame.dispose();
                dispose();
                new StartFrame();
        }
}
