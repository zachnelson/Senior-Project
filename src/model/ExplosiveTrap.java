package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

/**
 *
 * @author Joseph
 */

public class ExplosiveTrap extends EnemyFigure {

    private int frameCount;
    private double timeUp;
    private double timeTriggered;
    public int height;
    public int width;
    public final List<BufferedImage> breakImages;

    public ExplosiveTrap(float x, float y) {
        super(x, y);
        super.health = 1;
        super.damage = 4;
        super.pointValue = 0;
        super.state = GameFigureState.EXPLOSIVE_TRAP_STATE_UNTRIGGERED;
        timeUp = 0;
        timeTriggered = 0;
        frameCount = 0;
        setImage("/model/assets/ExplosiveTrap.png");
        breakImages = new CopyOnWriteArrayList<>();
    }

    private void setImage(String file1) {
        mainImage = null;
        try {
            mainImage = ImageIO.read(getClass().getResource(file1));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
            System.exit(-1);
        }

        width = mainImage.getWidth() / 2;
        height = mainImage.getHeight() / 2;
    }

    @Override
    public int getFigureType() {
        return this.EXPLOSIVETRAP;
    }

    @Override
    public void setSpeedBuff(double x) {
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                this.width, this.height, null);
    }

    @Override
    public void update() {
        if (super.state == GameFigureState.EXPLOSIVE_TRAP_STATE_TRIGGERED) {
            if (frameCount == 0) {
                loadImages();
                width = 70;
                height = 70;
                super.x -= 5;
                super.y -= 5;
            }
            if (frameCount < 48) {
                if (breakImages.size() == 0) {
                    loadImages();
                }
                mainImage = breakImages.get(frameCount / 3);
            } else {
                super.state = GameFigureState.STATE_DONE;
            }
            frameCount++;

        }
    }

    public void loadImages() {
        try {
            mainImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open explosion.png.");
            System.exit(-1);
        }

        // added by vanne 
        if (isSoundOn) //****************************sound implemented 
        {
            playMusic(explosion);
        }

        for (int i = 0; i < 16; i++) {
            breakImages.add(mainImage.getSubimage((int) (i % 4) * 128, (int) (i / 4) * 128, 128, 128));
        }
    }

    @Override
    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            state = GameFigureState.EXPLOSIVE_TRAP_STATE_TRIGGERED;
            frameCount = 0;
            if (!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
                super.enemyFactory.removeEnemyCharater();
            }
        }
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public int getState() {
        return super.state;
    }

    @Override
    public void setState(int state) {
        super.state = state;
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        if (super.state == GameFigureState.EXPLOSIVE_TRAP_STATE_UNTRIGGERED) {
            return (new Rectangle2D.Float(this.x, this.y, width, height));
        } else {
            return (new Rectangle2D.Float(0, 0, 0, 0));
        }
    }

}

