import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame(Order order) {
        setTitle("Food Order App");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new MainOrderPanel(order));

        setVisible(true);
    }
}
