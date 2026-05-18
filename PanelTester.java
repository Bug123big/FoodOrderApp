
import java.awt.Color;

import javax.swing.*;

public class PanelTester {

    public static void testPanel(JPanel panel, String title) {

        JFrame frame = new JFrame(title);

        frame.setSize(1300, 800);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        frame.add(panel);

        frame.setVisible(true);
    }
}
