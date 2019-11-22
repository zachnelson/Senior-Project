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

public class TrailerEnemy extends EnemyFigure {

    public double width;
    public double height;
    private int frameCount; 
    public final List<BufferedImage> explodeImages;
    private double speedBuff = 1;
    private boolean moving = false;
    private float dx;
    private float dy;
    private int destinationX;
    private int destinationY;
    private static final int UNIT_TRAVEL_DISTANCE = 3;
    
    public TrailerEnemy(float x, float y) {
        super(x, y); // origin: upper-left corner
        super.health = 5;
        super.damage = 0;
        super.pointValue = 2;
        super.state = GameFigureState.TRAILER_ENEMY_STATE_MOVING;
        frameCount = 0;
        setImage("/model/assets/trailer.png");
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
        health -= damage;
        if (health <= 0){
            state = GameFigureState.TRAILER_ENEMY_STATE_DAMAGED;
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
                (int) width, (int) height, null);     
    }
    
    @Override
    public void update() {
        if (super.state == GameFigureState.TRAILER_ENEMY_STATE_MOVING)
        {
            if (!moving)
            {
                destinationX = (int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 200)));
                destinationY = (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 200)));
                moving = true;
            }
            else
            {
                if (frameCount % 40 == 0)
                {
                    Trail t = new Trail(this.x, this.y, 1);
                    Main.gameData.getEnemyFigures().add(0, t);
                }
                float ty = destinationY;
                float tx = destinationX;
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
                    
                if(Math.abs(super.x - destinationX) < 10)
                {
                    moving = false;
                }
            }
            frameCount++;
        }
        else if (super.state == GameFigureState.TRAILER_ENEMY_STATE_DAMAGED)
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
                if (frameCount == 30)
                {
                    Trail t = new Trail(this.x-40, this.y-40, 2);
                    Main.gameData.getEnemyFigures().add(0, t);
                }
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
        return this.TRAILER;
    }


    @Override
    public void setSpeedBuff(double x) {
        speedBuff = x;
    }


   
}