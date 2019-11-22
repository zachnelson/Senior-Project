package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.sql.Time;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.util.Timer;
import java.util.TimerTask;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

public class BouncerEnemy extends EnemyFigure {

    private int height;
    private int width;
    private double velocityVert = 0;
    private double velocityHoriz = 0;
    public boolean[] directions = new boolean[255];
    private double friction = 0.8;
    private boolean isCharging = false;
    private float dx;
    private float dy;
    //private int direction = 1;
    private int FLYING_UNIT_TRAVEL = 5; // per frame for BouncerEnemy
    private final int WIDTH = 25;
    private final int HEIGHT = 25;
    private long lastCharged;
    float ty;
    float tx;
    private double speedBuff = 1;

    DIR direction = DIR.DIR_NORTH_EAST;

    public BouncerEnemy(int x, int y) {
        super(x, y);
        lastCharged = System.currentTimeMillis();
        super.health = 5;
        super.damage = 1;
        super.pointValue = 1;
        super.state = GameFigureState.BOUNCER_ENEMY_STATE_MOVING;
        try {
            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/BouncerEnemy.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open BouncerEnemy.png");
            System.exit(-1);
        }
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
        System.out.println("Bouncer Enemy Reporting for Duty.");
    }

    @Override
    public void render(Graphics2D g) {

        g.drawImage(mainImage, (int) super.x, (int) super.y, width, height, null);
    }

    @Override
    public void takeDamage(double damage) {
        //damage = 1;
//        System.out.println("Damage: " + damage);
        health -= damage;
//         System.out.println(health + damage);

        if (super.health <= 0) {

            // added by vanne 
            if (isSoundOn) //****************************sound implemented 
            {
                playMusic(explosion);
            }

            state = GameFigureState.BOUNCER_ENEMY_STATE_DEAD;
            if (!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
                super.enemyFactory.removeEnemyCharater();
            }
        }
    }

    @Override
    public void update() {
        if (super.state == GameFigureState.BOUNCER_ENEMY_STATE_DEAD) {

            super.state = GameFigureState.STATE_DONE;

            System.out.println("DEAD");

        }

        translate();
    }

    public void translate() {
 
        if (super.state == GameFigureState.BOUNCER_ENEMY_STATE_DEAD) {

            super.state = GameFigureState.STATE_DONE;

            System.out.println("DEAD");

        }

        if (super.state == GameFigureState.BOUNCER_ENEMY_STATE_MOVING) {
           
                FLYING_UNIT_TRAVEL = 7;
              
                
 
           
             
           
            
            
              ty = Main.gameData.getShooter().y + ((float) Main.gameData.getShooter().getHeight() / 2);
                tx = Main.gameData.getShooter().x - ((float) Main.gameData.getShooter().getWidth() / 2);
               
              
            

            float sy = super.y;
            float sx = super.x;

            double angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));

            dx = (float) ((FLYING_UNIT_TRAVEL * speedBuff) * Math.cos(angle));
            dy = (float) ((FLYING_UNIT_TRAVEL * speedBuff) * Math.sin(angle));

            if (tx > sx && ty < sy) {
                // target is upper-right side
                dy = -dy + randomNumber(); // dx > 0, dx < 0
            } else if (tx < sx && ty < sy) { // target is upper-left side
                dx = -dx+ randomNumber();
                dy = -dy+ randomNumber();
            } else if (tx < sx && ty > sy) { // target is lower-left side
                dx = -dx+ randomNumber();
            } else { // target is lower-right side
                // dx > 0 , dy > 0
            }

            super.x += dx+ randomNumber();
            super.y += dy+ randomNumber();

         

        }
        
       

    }

    /*  if(super.state == GameFigureState.BOUNCER_ENEMY_STATE_HIT){
        if (direction > 0) {
            // moving to the right
            super.x += (FLYING_UNIT_TRAVEL * speedBuff);
            if (super.x + WIDTH/2 > GamePanel.width -50) {
                direction = 1;
                
            }
        }else {
            // moving to the left
            super.x += (FLYING_UNIT_TRAVEL * speedBuff);
            if (super.x - ((WIDTH/2)-25) <= 0) {
                direction = 1;
            }
        }
        
        } */

//    private boolean isInPanel(){
//        if((super.x < Main.gamePanel.getLocation().x) 
//                || super.x + WIDTH > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
//                || super.y < Main.gamePanel.getLocation().y -150
//                || super.y + HEIGHT > (Main.gamePanel.getLocation().y + Main.gamePanel.getSize().height))
//            return false;
//        else {return true;}
//    }
    public int randomNumber() {
        int random = (int) (Math.random() * ((40 - (-40)) + 1)) + (-40);

        return random;
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
        final int TODO_ADD_STATE = 777;
        state = TODO_ADD_STATE;
    }

    @Override
    public int getFigureType() {
        return this.BOUNCER;
    }

    @Override
    public void setSpeedBuff(double x) {
        speedBuff = x;
    }

}
//comment
