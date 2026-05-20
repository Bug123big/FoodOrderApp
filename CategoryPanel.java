import javax.swing.*;
import java.awt.*;

public class CategoryPanel extends JPanel {

    public CategoryPanel(
            Runnable showSpecial,
            Runnable showEntrees,
            Runnable showDesserts,
            Runnable showDrinks,
            Runnable showCombo
    ) {
        setLayout(new GridLayout(0, 1, 10, 10));
        setPreferredSize(new Dimension(130, 0));
        setBackground(UIStyle.DARK_RED);
        setBorder(BorderFactory.createLineBorder(UIStyle.GOLD, 1));

        JButton specialButton = new JButton("Today Special");
        JButton entreeButton = new JButton("Entrees");
        JButton dessertButton = new JButton("Dessert");
        JButton drinkButton = new JButton("Drink");
        JButton comboButton = new JButton("Combo");
 
        UIStyle.styleGoldButton(specialButton);
        UIStyle.styleGoldButton(entreeButton);
        UIStyle.styleGoldButton(dessertButton);
        UIStyle.styleGoldButton(drinkButton);
        UIStyle.styleGoldButton(comboButton);

        specialButton.addActionListener(e -> showSpecial.run());
        entreeButton.addActionListener(e -> showEntrees.run());
        dessertButton.addActionListener(e -> showDesserts.run());
        drinkButton.addActionListener(e -> showDrinks.run());
        comboButton.addActionListener(e -> showCombo.run());

        add(specialButton);
        add(entreeButton);
        add(dessertButton);
        add(drinkButton);
        add(comboButton);
    }
}
