package model;


import controller.Main;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

public class Trail extends EnemyFigure {

    public double width;
    public double height;
    private int frameCount;
    public boolean slowing = false;
    public double oldSpeed;
    public int size;
    
    public Trail(float x, float y, int size) {
        super(x, y); // origin: upper-left corner
        super.health = 1000000;
        super.damage = 0;
        super.pointValue = 2;
        super.state = GameFigureState.TRAIL_STATE_DROPPED;
        frameCount = 0;
        this.size = size;
        if (size == 1)
        setImage("/model/assets/trail.png");
        else
        setImage("/model/assets/trail2.png");    
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();  
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
        
    }
    
    @Override
    public void takeDamage(double damage)
    {
    }
    
    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                (int) width, (int) height, null);     
    }
    
    @Override
    public void update() {
        
        
        
            if (checkCollision())
            {
                if (!Main.gameData.getShooter().slowed)
                {
                    if (!slowing)
                    {
                        oldSpeed = Main.gameData.getShooter().getSpeedBuff();
                    }
                    Main.gameData.getShooter().setSpeedBuff(0.4);
                    slowing = true;
                    Main.gameData.getShooter().slowed = true;
                }
                
            }
            else
            {
                if (slowing)
                {
                    Main.gameData.getShooter().setSpeedBuff(oldSpeed);
                    slowing = false;
                    Main.gameData.getShooter().slowed = false;
                }
            }
            
        if (super.state == GameFigureState.TRAIL_STATE_DROPPED)
        {
            if (frameCount > 240)
            {
                super.state = GameFigureState.TRAIL_STATE_FADING;
                frameCount = 0;
            }
        }
        else if (super.state == GameFigureState.TRAIL_STATE_FADING)
        {
            if (frameCount > 20)
            {   
                super.state = GameFigureState.STATE_DONE;
                if (slowing)
                {
                    Main.gameData.getShooter().setSpeedBuff(oldSpeed);
                    slowing = false;
                    Main.gameData.getShooter().slowed = false;
                }
            }
            else
            {
                if (frameCount % 2 == 0)
                {
                    try {
                        mainImage = ImageIO.read(getClass().getResource("/model/assets/transparent_box.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
                        System.exit(-1);
                    }
                }
                else
                {   
                    try {
                        mainImage = ImageIO.read(getClass().getResource("/model/assets/trail.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
                        System.exit(-1);
                    }
                }
            }
        }
        
            
        frameCount++;
        
    }
    
    public boolean checkCollision()
    {
        if (this.getCollisionBox().intersects(Main.gameData.getShooter().getCollisionBox()))
        {
                if (Main.animator.checkPixelCollision(this, Main.gameData.getShooter()))
                {
                    return true;
                }
        }
        return false;
    }
    @Override
    public Rectangle2D.Float getCollisionBox()
    {
        return (new Rectangle2D.Float(this.x, this.y, (int) width, (int)height));
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
        return this.TRAIL;
    }


    @Override
    public void setSpeedBuff(double x) {
    }


   
}