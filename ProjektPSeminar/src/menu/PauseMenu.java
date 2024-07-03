package menu;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseMenu extends JFrame implements ActionListener {
    JButton button1, button2, button3;
    JLabel imageLabel;
    int menuWidth = 290;
    int menuHeight = 370;

    public PauseMenu() {
        setTitle("Paused");
        setSize(menuWidth, menuHeight);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new BorderLayout());

        // Add image
        ImageIcon icon = new ImageIcon("image.jpg"); // Path to image
        imageLabel = new JLabel(icon);
        panel.add(imageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        //
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        JLabel titleLabel = new JLabel("Paused", SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(100, 30));
        panel.add(titleLabel, BorderLayout.NORTH);

        button1 = new JButton("Resume");
        buttonPanel.add(button1, gbc);
        button1.addActionListener(this);

        button2 = new JButton("Options");// bzw. "Main Menu"
        gbc.gridy = 1;
        buttonPanel.add(button2, gbc);
        button2.addActionListener(this);

        button3 = new JButton("Quit");
        gbc.gridy = 2;
        buttonPanel.add(button3, gbc);
        button3.addActionListener(this);

        Dimension buttonSize = new Dimension(100, 30);
        button1.setPreferredSize(buttonSize);
        button2.setPreferredSize(buttonSize);
        button3.setPreferredSize(buttonSize);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            // Handle button1 action
        } else if (e.getSource() == button2) {
            // Handle button2 action
        } else if (e.getSource() == button3) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new PauseMenu();
    }
}
