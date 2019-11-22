package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import static view.GamePanel.PlayerBoom;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

public class LaserShot extends EnemyFigure {

    private static final int UNIT_TRAVEL_DISTANCE = 10; // per frame move
    public Timer missileTimer;
    public float width;
    public float height;
    public int frameCount;
    public double angle;
    private double speedBuff = 1;
    
    
    public LaserShot(float x, float y, float width, float height) {
        super(x, y); // origin: upper-left corner
        super.health = 1;
        super.damage = 2;
        this.width = width;
        this.height = height;
        super.state = GameFigureState.LASER_SHOT_STATE_LAUNCHED;
        mainImage = null;
        angle = Math.atan2((Main.gameData.getShooter().x + Main.gameData.getShooter().getWidth() / 2 - super.x), (Main.gameData.getShooter().y + Main.gameData.getShooter().getHeight() / 2 - super.y));
        frameCount = 0;
        
        try {
            mainImage = ImageIO.read(getClass().getResource("/model/assets/laserEnemyLaser.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open laserEnemyLaser.png");
            System.exit(-1);
        }
        
    }
    
    @Override
    public void takeDamage(double damage)
    {
        //health -= damage;
        health = 0;
    }
    
    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                (int)width, (int)height, null);
    }

    @Override
    public void update() {
        
        if (state == GameFigureState.LASER_SHOT_STATE_LAUNCHED)
        {
            updateState();
            updateLocation();
        }
            
    }

    public void updateLocation() {
        
        Point2D.Float nextPoint = track();
        super.y += nextPoint.y;
        super.x += nextPoint.x;
        frameCount++;

    }
    
    public Point2D.Float track() {
            float dx = (float)(UNIT_TRAVEL_DISTANCE * Math.sin(angle));
            float dy = (float)(UNIT_TRAVEL_DISTANCE * Math.cos(angle));
            return new Point2D.Float(dx, dy);
    }

    
    public void updateState() {
    
            if (health <= 0)
            {
                
                 // added by vanne 
                if (isSoundOn) //****************************sound implemented 
	           playMusic(PlayerBoom);
                
                
                
                try {
                     mainImage = ImageIO.read(getClass().getResource("/model/assets/bulletHole.png"));
                 } catch (IOException ex) {
                     JOptionPane.showMessageDialog(null, "Error: Cannot open bulletHole.png");
                     System.exit(-1);
                 }
                
                super.damage = 0;
                state = GameFigureState.LASER_SHOT_STATE_EXPLODED;
                missleHit();
                missileTimer.start();
            }

            if (super.y + 20 < 0 || super.y > Main.WIN_HEIGHT)
            {
                state = GameFigureState.STATE_DONE;
                super.damage = 0;
            } else if (super.x +20 < Main.gamePanel.getLocation().x ||
                    super.x > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)){
                state = GameFigureState.STATE_DONE;
                super.damage = 0;
            }
        

    }
    
    public void missleHit()
    {
        missileTimer = new Timer(25, new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (height < 3)
                {
                    state = GameFigureState.STATE_DONE;
                    missileTimer.stop();    
                }
                else
                {
                    x += width * 0.25f;
                    y += height * 0.25f; 
                    width = width * 0.5f;
                    height = height * 0.5f;
                }
            }
        });
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox()
    {
        return (new Rectangle2D.Float(this.x, this.y, width, height));
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
        return 0; //TODO add state
    }
    @Override
    public void setState(int state) {
        final int TODO_ADD_STATE = 777;
        state = TODO_ADD_STATE;
    }


    @Override
    public int getFigureType() {
        return this.LASERSHOT;
    }

    @Override
    public void setSpeedBuff(double x) {
        speedBuff = x;
    }


}
