import javax.swing.*;
import java.awt.*;

public class UIStyle {

    public static final Color DARK_RED = new Color(90, 15, 15);
    public static final Color DEEP_RED = new Color(130, 25, 20);
    public static final Color GOLD = new Color(218, 165, 32);
    public static final Color LIGHT_GOLD = new Color(255, 230, 160);
    public static final Color CREAM = new Color(255, 248, 220);

    public static void styleGoldButton(JButton button) {
        button.setBackground(DEEP_RED);
        button.setForeground(LIGHT_GOLD);
        button.setFont(new Font("Avenir Next", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(GOLD, 2));
    }

    public static void styleRedTextButton(JButton button) {
        button.setBackground(CREAM);
        button.setForeground(DARK_RED);
        button.setFont(new Font("Avenir Next", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(DARK_RED, 2));
    }
}
