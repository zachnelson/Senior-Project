/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.System.currentTimeMillis;
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
public class SpikeTrap extends EnemyFigure {

    private int frameCount;
    private double timeUp;
    private double timeTriggered;
    public int height;
    public int width;
    public final List<BufferedImage> breakImages;
    private BufferedImage triggeredImage;

    public SpikeTrap(float x, float y) {
        super(x, y);
        super.health = 2;
        super.damage = 2;
        super.pointValue = 1;
        super.state = GameFigureState.SPIKE_TRAP_STATE_DOWN;
        timeUp = 0;
        timeTriggered = 0;
        frameCount = 0;
        setImage("/model/assets/spikeDown.png", "/model/assets/spikeUp.png");
        breakImages = new CopyOnWriteArrayList<>();
    }

    private void setImage(String file1, String file2) {
        mainImage = null;
        triggeredImage = null;
        try {
            mainImage = ImageIO.read(getClass().getResource(file1));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
            System.exit(-1);
        }
        try {
            triggeredImage = ImageIO.read(getClass().getResource(file2));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
            System.exit(-1);
        }

        width = mainImage.getWidth();
        height = mainImage.getHeight();
    }

    @Override
    public int getFigureType() {
        return this.SPIKETRAP;
    }

    @Override
    public void setSpeedBuff(double x) {
    }

    @Override
    public void render(Graphics2D g) {
        if (super.state == GameFigureState.SPIKE_TRAP_STATE_UP) {
            g.drawImage(triggeredImage, (int) super.x, (int) super.y,
                    width, height, null);
        } else if (super.state == GameFigureState.SPIKE_TRAP_STATE_DOWN) {
            g.drawImage(mainImage, (int) super.x, (int) super.y,
                    width, height, null);
        }
        else if (super.state == GameFigureState.SPIKE_TRAP_STATE_DESTROYED) {
            g.drawImage(mainImage, (int) super.x, (int) super.y,
                    width, height, null);
        }
    }

    @Override
    public void update() {
        if (super.state == GameFigureState.SPIKE_TRAP_STATE_UP
                && timeTriggered >= 1) {
            super.state = GameFigureState.SPIKE_TRAP_STATE_DOWN;
            timeTriggered = 0;
            timeUp = 0;
        } else if (super.state == GameFigureState.SPIKE_TRAP_STATE_UP
                && timeUp == 0) {
            timeUp = currentTimeMillis() / 1000;
        } else if (super.state == GameFigureState.SPIKE_TRAP_STATE_UP) {
            timeTriggered = (currentTimeMillis()/1000) - timeUp;
        }
        else if (super.state == GameFigureState.SPIKE_TRAP_STATE_DESTROYED)
        {
            if (frameCount == 0)
            {
                loadImages();
                width = 70;
                height = 70;
                super.x -= 5;
                super.y -= 5;
            }
            if (frameCount < 48)
            { 
                if (breakImages.size() == 0) loadImages();
                mainImage = breakImages.get(frameCount / 3);
            }
            else
            {
                super.state = GameFigureState.STATE_DONE;
            }
        }
        
        frameCount++;
    }

    public void loadImages() {
        try {
            mainImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
            triggeredImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
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
            state = GameFigureState.SPIKE_TRAP_STATE_DESTROYED;
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
        if (super.state == GameFigureState.SPIKE_TRAP_STATE_DOWN || super.state == GameFigureState.SPIKE_TRAP_STATE_UP) {
            return (new Rectangle2D.Float(this.x, this.y, width, height));
        } else {
            return (new Rectangle2D.Float(0, 0, 0, 0));
        }
    }

}

