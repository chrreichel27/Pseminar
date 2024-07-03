package menu;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class StartMenu extends JFrame {

    public StartMenu() {
        setTitle("Game Menu");
        setSize(1280, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // set panel for the background
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(null);

        // Create buttons, set their size, position in space, pictures
        CustomButton startButton = new CustomButton("Start Game", "res/options.png", 320, 135);
        startButton.setBounds(400, 120, 500, 150);
        startButton.addActionListener(new ButtonClickListener());

        CustomButton optionsButton = new CustomButton("Options", "res/options.png", 420, 160);
        optionsButton.setBounds(350, 260, 600, 200);
        optionsButton.addActionListener(new ButtonClickListener());

        CustomButton exitButton = new CustomButton("Exit", "res/options.png", 320, 135);
        exitButton.setBounds(400,450 , 500, 150);
        exitButton.addActionListener(new ButtonClickListener());

        // add buttons on the panel(background)
        backgroundPanel.add(startButton);
        backgroundPanel.add(optionsButton);
        backgroundPanel.add(exitButton);

        // add panel on the frame(window)
        add(backgroundPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartMenu::new);
    }

    //
    private class ButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String buttonText = source.getText();
            switch (buttonText) {
                case "Start Game":
                    System.out.println("Game is starting...");
                    //start();
                    //.setVisible(true);
                    //setVisible(false);

                    break;
                case "Options":
                    System.out.println("Options button clicked");
                    //https://www.youtube.com/watch?v=PJ_odWsnwkY
                    break;
                case "Exit":
                    System.out.println("Exit button clicked");
                    System.exit(0);
                    break;
            }
        }
    }
}


//function for custom buttons
class CustomButton extends JButton {
    private BufferedImage buttonImage;

    public CustomButton(String text, String imagePath, int imageWidth, int imageHeight) {
        super(text);

        try {
            buttonImage = ImageIO.read(new File(imagePath));
            if (buttonImage != null) {
                buttonImage = resizeImage(buttonImage, imageWidth, imageHeight);
            } else {
                System.err.println("Failed to load image: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // make the buttons clear
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setContentAreaFilled(false);
        setBorderPainted(false);
    }

    //scaled original image to the desired dimension
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }

    @Override
    //set images to buttons
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (buttonImage != null) {
            int x = (getWidth() - buttonImage.getWidth()) / 2;
            int y = (getHeight() - buttonImage.getHeight()) / 2;
            g.drawImage(buttonImage, x, y, this);
        } else {
            System.err.println("buttonImage is null in paintComponent");
        }
    }
}

//create background using one brick image
class BackgroundPanel extends JPanel {
    private BufferedImage brickImage;

    public BackgroundPanel() {
        try {
            brickImage = ImageIO.read(new File("res/menuBack.brick.jpg"));
            if (brickImage == null) {
                System.err.println("Failed to load brick image");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (brickImage != null) {
            Graphics2D g2d = (Graphics2D) g;

            //render
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // change the size of bricks
            int scaledBrickWidth = brickImage.getWidth() * 2;
            int scaledBrickHeight = brickImage.getHeight() * 2;

            // draw bricks on the panel
            for (int y = 0; y < getHeight(); y += scaledBrickHeight) {
                for (int x = 0; x < getWidth(); x += scaledBrickWidth) {
                    g2d.drawImage(brickImage, x, y, scaledBrickWidth, scaledBrickHeight, this);
                }
            }
        } else {
            System.err.println("brickImage is null in paintComponent");
        }
    }
}
