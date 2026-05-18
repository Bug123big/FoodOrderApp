import javax.swing.*;

public class StartFrame extends JFrame {

    public StartFrame() {
        Order order = new Order();

        setTitle("Food Order App");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        add(new CoverPanel(this, order));

        setVisible(true);
    }
}
