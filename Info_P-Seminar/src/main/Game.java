package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import entities.Player;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Player player;
    private World world;
    private boolean worldInitialized = false, hitboxesInitialized = false;
    private Rectangle[] hitboxesOfPlattforms;
    private Rectangle hitboxOfPlayer;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();


        startGameLoop();
    }

    private void initClasses() {
        player = new Player(200.0f, 200.0f, 256, 160);

        ;

    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
        checkPlayerScreenCollision();
        if(!worldInitialized) {
            return;
        }
        if(!hitboxesInitialized) {
            hitboxesOfPlattforms = world.getPlattformHitboxes();
            hitboxOfPlayer = player.getHitbox();
            hitboxesInitialized = true;
            return;
        }
        for (Rectangle plattformHitbox : hitboxesOfPlattforms) {
            if(plattformHitbox.intersects(hitboxOfPlayer)) {
                System.out.println("Kollision erkannt!!");

                }
            }
        }





    private void checkPlayerScreenCollision() {
        float playerX = player.getX();
        float playerY = player.getY();
        int playerWidth = player.getWidth();
        int playerHeight = player.getHeight();

        // Check collision with screen borders
        if (playerX < 0) {
            player.setX(0);
        } else if (playerX + playerWidth > screenSize.getWidth()) {
            player.setX((float) (screenSize.getWidth() - playerWidth));
        }

        if (playerY < 0) {
            player.setY(0);
        } else if (playerY + playerHeight > screenSize.getHeight()) {
            player.setY((float) (screenSize.getHeight() - playerHeight));
        }
    }




    public void render(Graphics g) {
        if(!worldInitialized)
        {
            world = new World(1,g);
            worldInitialized = true;
        }
        else
        {
            world.zeichnePlattformen(g);
        }

        player.render(g);
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;

            }
        }

    }

    public void windowFocusLost() {
        player.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }

}
