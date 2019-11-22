package model;


import controller.Main;
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

public class SplitterEnemy extends EnemyFigure {

    public double width;
    public double height;
    private int frameCount; 
    public final List<BufferedImage> explodeImages;
    private double speedBuff = 1;
    public double totalHealth;
    private int splitLevel;
    private int side;
    private float dx;
    private float dy;
    private static final int UNIT_TRAVEL_DISTANCE = 3;
    
    public SplitterEnemy(float x, float y, int splitLevel, int side) {
        super(x, y); // origin: upper-left corner
        this.splitLevel = splitLevel;
        this.side = side;
        super.health = 5;
        totalHealth = super.health;
        super.damage = 1;
        super.pointValue = 2;
        
        if (splitLevel == 0)
        super.state = GameFigureState.SPLITTER_ENEMY_CHASING;
        else
        super.state = GameFigureState.SPLITTER_ENEMY_SEPERATING;
        
        frameCount = 0;
        setImage("/model/assets/splitter" + Integer.toString(splitLevel) + ".png");
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
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
    }
    
    @Override
    public void takeDamage(double damage)
    {
        if (state != GameFigureState.SPLITTER_ENEMY_SPLITTING && state != GameFigureState.SPLITTER_ENEMY_SEPERATING)
        {
            health -= damage;
        }
        if (health <= 0 && state != GameFigureState.STATE_DONE){
            if (splitLevel == 2)
            {
                state = GameFigureState.SPLITTER_ENEMY_DAMAGED;
                frameCount = 0;
                if(!super.dead) {
                    super.dead = true;
                    super.IncreaseScore(super.pointValue);
                    super.enemyFactory.removeEnemyCharater();
                }
            }
            else
            {
                state = GameFigureState.SPLITTER_ENEMY_SPLITTING;
            }   
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int)super.x, (int)super.y,
                (int)width, (int)height, null);   
    }
    
    @Override
    public void update() {
        if (super.state == GameFigureState.SPLITTER_ENEMY_CHASING)
        {
            float ty = Main.gameData.getShooter().y + (float)Main.gameData.getShooter().getHeight() / 2;
            float tx = Main.gameData.getShooter().x + (float)Main.gameData.getShooter().getWidth() / 2;
            float sy = super.y + (int)height / 2;
            float sx = super.x + (int)width / 2;

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

            if (splitLevel == 0)
            {
                if (frameCount % 30 > 18)
                {
                    super.x += dx;
                    super.y += dy;
                }
            }
            else if (splitLevel == 1)
            {
                if (frameCount % 20 > 10)
                {
                    super.x += dx * 1.5;
                    super.y += dy * 1.5;
                }
            }
            else
            {
                if (frameCount % 10 > 4)
                {
                    super.x += dx * 2;
                    super.y += dy * 2;
                }
            }
            
            frameCount++;
        }
        else if (super.state == GameFigureState.SPLITTER_ENEMY_SPLITTING)
        {
            if (splitLevel == 0)
            {
                if (width <= 70)
                {
                    width = 70;
                    height = 47;
                    super.state = GameFigureState.STATE_DONE;
                    SplitterEnemy s1 = new SplitterEnemy(this.x, this.y, splitLevel + 1, 0);
                    Main.gameData.getEnemyFigures().add(s1);
                    EnemyFactory.getInstance().addEnemyCharater();
                    SplitterEnemy s2 = new SplitterEnemy(this.x, this.y, splitLevel + 1, 1);
                    Main.gameData.getEnemyFigures().add(s2);
                    EnemyFactory.getInstance().addEnemyCharater();
                }
                else
                {
                    width -= 1.48;
                    height -= 1;
                    this.x += .5;
                    this.y += .5;
                }
            }
            else
            {
                if (width <= 35)
                {
                    width = 35;
                    height = 24;
                    super.state = GameFigureState.STATE_DONE;
                    SplitterEnemy s1 = new SplitterEnemy(this.x, this.y, splitLevel + 1, 0);
                    Main.gameData.getEnemyFigures().add(s1);
                    EnemyFactory.getInstance().addEnemyCharater();
                    SplitterEnemy s2 = new SplitterEnemy(this.x, this.y, splitLevel + 1, 1);
                    Main.gameData.getEnemyFigures().add(s2);
                    EnemyFactory.getInstance().addEnemyCharater();
                }
                else
                {
                    width -= 1.45;
                    height -= 1;
                    this.x += .5;
                    this.y += .5;
                }
            }
            
            
        }
        else if (super.state == GameFigureState.SPLITTER_ENEMY_SEPERATING)
        {
            if (frameCount < 40)
            {
                if (side == 0)
                {
                    super.x-=2;
                }
                else
                {
                    super.x+=2;
                }  
            }
            else
            {
                super.state = GameFigureState.SPLITTER_ENEMY_CHASING;
            }
        }
        else if (super.state == GameFigureState.SPLITTER_ENEMY_DAMAGED)
        {
            if (frameCount == 0)
            {
                loadImages();
                width = 35;
                height = 35;
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
        
        
       // added by vanne 
        if (isSoundOn) //****************************sound implemented 
	         playMusic(explosion); 
        
        
        for (int i = 0; i < 16; i++)
        {
            explodeImages.add(mainImage.getSubimage((int)(i % 4) * 128, (int)(i / 4) * 128, 128, 128));
        }
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox()
    {
        return (new Rectangle2D.Float(this.x, this.y, (int)width, (int)height));

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
        return this.SPLITTER;
    }


    @Override
    public void setSpeedBuff(double x) {
        speedBuff = x;
    }


   
}