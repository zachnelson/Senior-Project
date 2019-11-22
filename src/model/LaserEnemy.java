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

public class LaserEnemy extends EnemyFigure {
    
    private int height;
    private int width;
    private double velocityVert = 0;
    private double velocityHoriz = 0;
    public boolean[] directions = new boolean[255];
    private double friction = 0.8;
    //private int direction = 1;
    private final int FLYING_UNIT_TRAVEL = 0; // per frame for FlyingEnemy
    private final int WIDTH = 25;
    private final int HEIGHT = 25;
    private double speedBuff = 1;
    
    private boolean hasShot = false;
     
    private long lastShot;
    public final List<BufferedImage> explodeImages;
    private int frameCount; 
    
  
    
   
    
    
    
    public LaserEnemy(int x, int y) {
        super(x, y);
        lastShot = System.currentTimeMillis();
         super.health = 5;
        super.damage = 1;
        super.pointValue = 1;
        super.state = GameFigureState.LASER_ENEMY_STATE_SHOOTING;
        try {
                super.mainImage = ImageIO.read(getClass().getResource("/model/assets/LaserEnemy.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open LaserEnemy.png");
                System.exit(-1);
            }
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
       // System.out.println("Laser Enemy Reporting for Duty.");
        explodeImages = new CopyOnWriteArrayList<>();
        frameCount = 0;
    }
    @Override
    public void render(Graphics2D g) {
        
        g.drawImage(mainImage, (int)super.x, (int)super.y, width, height, null);
    }
    @Override  
    public void takeDamage(double damage) {
        //damage = 1;
//        System.out.println("Damage: " + damage);
         health -= damage;
//         System.out.println(health + damage);
         
        if (super.health <= 0){
            state = GameFigureState.LASER_ENEMY_STATE_DEAD;
            if(!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
            }
        }
    }
    @Override
    public void update() {
        if (super.state == GameFigureState.LASER_ENEMY_STATE_DEAD)
        {
             
         if (frameCount == 0)
            {
                loadImages();
                width = 160;
                height = 160;
                super.x += 20;
                super.y += 20;
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
            frameCount++;
        }
        
        translate();
    }
    
    public void translate() {
        if(super.state == GameFigureState.LASER_ENEMY_STATE_NOTHING){
            
       if(lastShot + 4500 <= System.currentTimeMillis()){
          super.state = GameFigureState.LASER_ENEMY_STATE_SHOOTING;
          hasShot = false;
          
          
         
         } else {
            
            hasShot = true;
           
       }
        }
        if(super.state == GameFigureState.LASER_ENEMY_STATE_SHOOTING){
            if(!hasShot){
             LaserShot l = new LaserShot(this.x + width/2, this.y + height / 2, 20, 20);
                Main.gameData.getEnemyFigures().add(l);
               
                    lastShot = System.currentTimeMillis();
                     hasShot = true;
                     super.state = GameFigureState.LASER_ENEMY_STATE_NOTHING;
                     
            }
        
        }
       
        
        
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
        return this.LASER;
    }

    @Override
   public void setSpeedBuff(double x){
       speedBuff = x;
   }
    
}
//asdf
