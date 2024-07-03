package entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import static utilz.Constants.EnemyConstants.*;
import static utilz.Constants.EnemyConstants.ATTACK;
import static utilz.Constants.EnemyConstants.GetSpriteAmount;
import static utilz.Constants.EnemyConstants.IDLE;
import static utilz.Constants.EnemyConstants.JUMP;
import static utilz.Constants.EnemyConstants.RUNNING;

public class Enemy extends Entity {
    private BufferedImage[][] animations;
    private int aniTick, aniIndex, aniSpeed = 25;
    private int enemyAction = IDLE;
    private boolean moving = false, attacking = false, jumping = false;
    private boolean left, up, right, down;
    private float enemySpeed = 2.0f;

    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
    }

    public void update() {
        updatePos();
        updateHitbox();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(animations[enemyAction][aniIndex], (int) x, (int) y, 256, 160, null);
        drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= GetSpriteAmount(enemyAction)) {
                aniIndex = 0;
                attacking = false;
            }

        }

    }

    private void setAnimation() {
        int startAni = enemyAction;

        if (moving)
            enemyAction = RUNNING;
        else
            enemyAction = IDLE;

        if (attacking)
            enemyAction = ATTACK;

        if (jumping)
            enemyAction = JUMP;


        if (startAni != enemyAction)
            resetAniTick();
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        if (left && !right) {
            x -= enemySpeed;
            moving = true;
        } else if (right && !left) {
            x += enemySpeed;
            moving = true;
        }

        if (up && !down) {
            y -= enemySpeed;
            moving = true;
        } else if (down && !up) {
            y += enemySpeed;
            moving = true;
        }
    }

    private void loadAnimations() {
        InputStream is = getClass().getResourceAsStream("/platzhalter.png");
        try {
            BufferedImage img = ImageIO.read(is);

            animations = new BufferedImage[9][6];
            for (int j = 0; j < animations.length; j++)
                for (int i = 0; i < animations[j].length; i++)
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetDirBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setJumping(boolean jumping) {this.jumping = jumping;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

}