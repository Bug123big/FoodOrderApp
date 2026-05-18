
import javax.swing.*;

public class PanelTester {

    public static void testPanel(JPanel panel, String title) {

        JFrame frame = new JFrame(title);

        frame.setSize(900, 600);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.add(panel);

        frame.setVisible(true);
    }
}
