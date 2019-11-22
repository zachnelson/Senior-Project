package model;


import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

public class FreezerEnemy extends EnemyFigure {

    private int frameCount; 
    public final List<BufferedImage> explodeImages;
    private int width;
    private int height;
    private float dx;
    private float dy;
    private static final int UNIT_TRAVEL_DISTANCE = 5;
    private double speedBuff = 1;
    
    public FreezerEnemy(float x, float y) {
        super(x, y); // origin: upper-left corner
        super.health = 5;
        super.damage = 0;
        super.pointValue = 5;
        super.state = GameFigureState.FREEZER_ENEMY_STATE_STARTED;
        frameCount = 0;
        setImage("/model/assets/freezer.png");
        explodeImages = new CopyOnWriteArrayList<>();   
    }
    
    public void setImage(String file)
    {
        mainImage = null;
        try {
            mainImage = ImageIO.read(getClass().getResource(file));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
            System.exit(-1);
        }
        
        width = mainImage.getWidth();
        height = mainImage.getHeight();
    }
    
    @Override
    public void takeDamage(double damage)
    {
        health -= damage;
        if (health <= 0){
            state = GameFigureState.FREEZER_ENEMY_STATE_DAMAGED;
            frameCount = 0;
            if(!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
                super.enemyFactory.removeEnemyCharater();               
            }
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                width, height, null);     
    }
    
    @Override
    public void update() {
        if (super.state == GameFigureState.FREEZER_ENEMY_STATE_STARTED)
        {
            if (Main.gameData.getShooter().frozen)
            {
                super.state = GameFigureState.FREEZER_ENEMY_STATE_FREEZING;
                frameCount = 0;
            }
            else
            {
                float ty = Main.gameData.getShooter().y + (float)Main.gameData.getShooter().getHeight() / 2;
                float tx = Main.gameData.getShooter().x - (float)Main.gameData.getShooter().getWidth() / 2;
                float sy = super.y;
                float sx = super.x;

                double angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));
          
                dx = (float) ((UNIT_TRAVEL_DISTANCE * speedBuff) * Math.cos(angle));
                dy = (float) ((UNIT_TRAVEL_DISTANCE * speedBuff) * Math.sin(angle));

                if (tx > sx && ty < sy) { // target is upper-right side
                    dy = -dy; // dx > 0, dx < 0
                } else if (tx < sx && ty < sy) { // target is upper-left side
                    dx = -dx;
                    dy = -dy;
                } else if (tx < sx && ty > sy) { // target is lower-left side
                    dx = -dx;
                } else { // target is lower-right side
                    // dx > 0 , dy > 0
                }

                super.x += dx;
                super.y += dy; 
            }
            
        }
        else if (super.state == GameFigureState.FREEZER_ENEMY_STATE_FREEZING)
        {
            if (frameCount < 80)
            {
                float ty = Main.gameData.getShooter().y + (float)Main.gameData.getShooter().getHeight() / 2;
                float tx = Main.gameData.getShooter().x + (float)Main.gameData.getShooter().getWidth() / 2;
                float sy = super.y;
                float sx = super.x;

                double angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));   
                
                dx = (float) ((UNIT_TRAVEL_DISTANCE * speedBuff) * Math.cos(angle));
                dy = (float) ((UNIT_TRAVEL_DISTANCE * speedBuff) * Math.sin(angle));

                if (tx > sx && ty < sy) { // target is upper-right side
                    dx = -dx;
                } else if (tx < sx && ty < sy) { // target is upper-left side
              
                } else if (tx < sx && ty > sy) { // target is lower-left side
                    dy = -dy;
                } else { // target is lower-right side
                    dx = -dx;
                    dy = -dy;
                }

                super.x += dx;
                super.y += dy;
                   
            }   
            else
            {
                Main.gameData.getShooter().frozen = false;
                super.state = GameFigureState.FREEZER_ENEMY_STATE_STARTED;
            }
            frameCount++;
        }
        else if (super.state == GameFigureState.FREEZER_ENEMY_STATE_DAMAGED)
        {
            Main.gameData.getShooter().frozen = false;
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
                if (explodeImages.size() == 0) loadImages();
                mainImage = explodeImages.get(frameCount / 3);
            }
            else
            {
                super.state = GameFigureState.STATE_DONE;
            }
        }
        
        frameCount++;
        
    }
    
    public void loadImages()
    {
        try {
                    mainImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Cannot open explosion.png.");
                    System.exit(-1);
                }
        for (int i = 0; i < 16; i++)
        {
            
             // added by vanne 
            if (isSoundOn) //****************************sound implemented 
	          playMusic(explosion);
            
            
            explodeImages.add(mainImage.getSubimage((int)(i % 4) * 128, (int)(i / 4) * 128, 128, 128));
        }
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox()
    {
        if (super.state == GameFigureState.FREEZER_ENEMY_STATE_STARTED || super.state == GameFigureState.FREEZER_ENEMY_STATE_FREEZING)
        return (new Rectangle2D.Float(this.x, this.y, width, height));
        else
        return (new Rectangle2D.Float(0, 0, 0, 0));
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
        return this.FREEZER;
    }

    @Override
    public void setSpeedBuff(double x){
       speedBuff = x;
   }
}
