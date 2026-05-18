import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CoverPanel extends JPanel {

    private JFrame parentFrame;
    private Order order;
    private Image backgroundImage;

    public CoverPanel(JFrame parentFrame, Order order) {
        this.parentFrame = parentFrame;
        this.order = order;

        backgroundImage = new ImageIcon(
                "Image/6bd20859-8faf-46c4-90d7-7e368d6cbfdb.png"
        ).getImage();

        setLayout(new BorderLayout());

        JLabel title = new JLabel("WELCOME TO FOOD ORDER", SwingConstants.CENTER);
        title.setFont(new Font("Helvetica Neue", Font.BOLD, 56));
        title.setForeground(Color.WHITE);

        JLabel hint = new JLabel("Click Anywhere To Start", SwingConstants.CENTER);
        hint.setFont(new Font("Helvetica Neue", Font.PLAIN, 30));
        hint.setForeground(Color.WHITE);

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(title);
        textPanel.add(hint);

        add(textPanel, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parentFrame.dispose();
                new MainFrame(order);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(20, 0, 0, 120));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}