
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

/**
 *
 * @author Desmond
 */
public class Barrel extends EnemyFigure {
    private int height;
    private int width;
    private int frame;

    public Barrel(int x, int y) {
        super(x, y);
        super.health = 5;
        super.damage = 0;
        frame = 0;

        super.state = GameFigureState.BARREL_STATE_STARTED;
        try {
            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/barrel.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open barrel.png");
            System.exit(-1);
        }
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
    }

    @Override
    public void render(Graphics2D g) {
        
            //------desmond--------------------------------
            //constrain to the game scene the game scene
            //---------------------------------------------
            if (super.x - mainImage.getWidth() / 2 < 0) {
                super.x = mainImage.getWidth() / 2;
            }
            if (super.x + mainImage.getWidth() / 2 > Main.WIN_WIDTH) {
                super.x = Main.WIN_WIDTH - mainImage.getWidth() / 2;
            }
            if (super.y - mainImage.getHeight() / 2 < 0) {
                super.y = mainImage.getHeight() / 2;
            }
            if (super.y + mainImage.getHeight() + 100 > Main.WIN_HEIGHT) {       
                super.y = Main.WIN_HEIGHT /2;
            }
            //---------------------------------------------

        g.drawImage(mainImage, (int) super.x, (int) super.y, width, height, null);
    }

    @Override
    public void takeDamage(double damage) {
        //damage = 1;
//        System.out.println("Damage: " + damage);
        health -= damage;
        //         System.out.println(health + damage);
        frame = 0;
        super.state = GameFigureState.BARREL_STATE_HIT;
        if (super.health <= 0) {
            state = GameFigureState.BARREL_STATE_DAMAGED;
            if (!super.dead) {
                super.dead = true;
                super.enemyFactory.removeEnemyCharater();
            }
        }
    }

    @Override
    public void update() {
        

        if (super.state == GameFigureState.BARREL_STATE_DAMAGED) {
            //***vanne****
            if (isSoundOn) //****************************sound implemented 
            {
                playMusic(explosion);
            }
            //--------------------------------
            //Create treasure 
            //--------------------------------
            GameFigure treasure = new Treasure(this.x, this.y, (int) (Math.random() * 2) + 1);
            List<GameFigure> mfigures = Main.gameData.getMiscFigures();
            mfigures.add(treasure);
            super.state = GameFigureState.STATE_DONE;

        }
        if (super.state == GameFigureState.BARREL_STATE_HIT) {
            //--------------------
            //agitate the barrel
            //--------------------
            frame++;
            if (frame < 10) {
                if (frame % 2 == 0) {
                    this.x += 5;
                } else {
                    this.x -= 5;
                }
            } else {
                frame = 0;
                super.state = GameFigureState.BARREL_STATE_STARTED;
            }
        }

    }

    @Override
    public Rectangle2D.Float getCollisionBox() {

        return (new Rectangle2D.Float(this.x, this.y, width, height));

    }

    // Make some modification to the game figure
    // if a collision is detected.
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
        return 0; //TODO add state
    }

    @Override
    public void setState(int state) {

    }

    @Override
    public int getFigureType() {
        return this.BARREL;
    }

    @Override
    public void setSpeedBuff(double x) {
    }

}
//asdf
