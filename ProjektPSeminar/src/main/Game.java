package main;

import java.awt.*;

import entities.Player;
import entities.Enemy;

public class Game implements Runnable {

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;
    private final static int FPS_SET = 120;
    private final static int UPS_SET = 200;

    private Player player;
    private Enemy enemy;
    private World world;
    private boolean worldInitialized = false, hitboxesInitialized = false;
    private Rectangle[] hitboxesOfPlatforms;
    private Rectangle hitboxOfPlayer;


    public Game() {
        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        player = new Player(200, 200, 256, 160);
        enemy = new Enemy(200,200,256,160);

    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update();
        enemy.update();
        if(!worldInitialized) {
            return;
        }
        if(!hitboxesInitialized) {
            hitboxesOfPlatforms = world.getPlattformHitboxes();
            hitboxOfPlayer = player.getHitbox();
            hitboxesInitialized = true;
            return;
        }
        for(Rectangle plattformHitbox : hitboxesOfPlatforms) {
            while(plattformHitbox.intersects(hitboxOfPlayer)) {
                doCollision();
            }
        }
    }

    public void doCollision() {
        char currentDir = player.isUp() ? 'u' : player.isDown() ? 'd' : player.isRight() ? 'r' : player.isLeft() ? 'l' : 'x';

        switch(currentDir) {
            case 'x' -> {return;}
            case 'u' -> {
                player.setUp(false);
                break;
            }
            case 'd' -> {
                player.setDown(false);
                break;
            }
            case 'r' -> {
                player.setRight(false);
                break;
            }
            case 'l' -> {
                player.setLeft(false);
                break;
            }
        }
    }


    public void render(Graphics g) {

        if(!worldInitialized)
        {
            world = new World(1,g);
            worldInitialized = true;

        }
        else {
            world.zeichnePlattformen(g);
        }
        player.render(g);
        enemy.render(g);

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
        enemy.resetDirBooleans();
    }

    public Player getPlayer() {
        return player;
    }
    public Enemy getEnemy() {
        return enemy;
    }

}