/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author hp
 */
public class BigHandsBoss extends EnemyFigure{

    public int width;
    public int height;
    private int frameCount;
    public final List<BufferedImage> explodeImages;
    public static BigHand leftHand;
    public static BigHand rightHand;
    private static final int UNIT_TRAVEL_DISTANCE = 7;
    public static int dir;
    public static float theX;
    public static float theY;
    public int attack;
    public static boolean attacking = false;
    public boolean hit = false;
    private int maxHealth = 5, currentHealth = 5;   // 200
    private double speedBuff = 1;
    
    public BigHandsBoss(float x, float y) {
        super(x, y);
        theX = super.x;
        theY = super.y;
        dir = (int)Math.round(Math.random());
        super.damage = 1;
        super.pointValue = 10;
        super.state = GameFigureState.BIG_HANDS_BOSS_STARTED;
        frameCount = 0;
        setImage("/model/assets/bighandsboss.png");        
        width = mainImage.getWidth();
        height = mainImage.getHeight();
        makeHands();
        explodeImages = new CopyOnWriteArrayList<>();
    }
    
    public void makeHands()
    {
        leftHand = new BigHand(super.x - 20, super.y + height - 45, 1);
        rightHand = new BigHand(super.x + width - 50, super.y + height - 45, 2);
        Main.gameData.getEnemyFigures().add(leftHand);
        Main.gameData.getEnemyFigures().add(rightHand);
        
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
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                width, height, null); 
    }

    @Override
    public void update() {
        if (state == GameFigureState.BIG_HANDS_BOSS_STARTED)
        {
            super.y += (UNIT_TRAVEL_DISTANCE * speedBuff) / 2;
            theY = super.y;
            if(super.y > 0)
                state = GameFigureState.BIG_HANDS_BOSS_APPEARED;
        }
        if (state == GameFigureState.BIG_HANDS_BOSS_LOCKED)
        {
                if (super.x > 426 && super.x < 852)
                {
                    attack = (int)Math.floor(Math.random() * 3) + 1;
                }
                else
                {
                    if ((int)Math.floor(Math.random() * 2) == 0)
                    {
                        attack = 1;
                    }
                    else
                    {
                        attack = 3;
                    }
                }
                leftHand.attack(attack);
                rightHand.attack(attack);
                attacking = true;
                state = GameFigureState.BIG_HANDS_BOSS_WAITING;
              
        }
        if (state == GameFigureState.BIG_HANDS_BOSS_WAITING)
        {
            if (attacking == false)
            {
                state = GameFigureState.BIG_HANDS_BOSS_APPEARED;
            }
        }
        if (state == GameFigureState.BIG_HANDS_BOSS_APPEARED)
        {
            
            if ((Main.gameData.getShooter().getY() < height))
            {
                if(this.getCollisionBox().intersects(Main.gameData.getShooter().getCollisionBox()))
                {
                    if(Main.animator.checkPixelCollision(this, Main.gameData.getShooter()) && !hit)
                    {
                        if (dir == 0)
                            dir = 1;
                        else
                            dir = 0;
                        hit = true;
                    }
                }
                else
                {
                    hit = false;
                }
                if (dir == 0)
                {
                    if (super.x - (UNIT_TRAVEL_DISTANCE * speedBuff) * 3 < 10)
                    {
                        dir = 1;
                    }
                    else
                    {
                        super.x -= (UNIT_TRAVEL_DISTANCE * speedBuff) * 3;
                    }

                }
                else
                {
                    if (super.x + width > Main.WIN_WIDTH - 10)
                    {
                        dir = 0;
                    }
                    else
                    {
                        super.x += (UNIT_TRAVEL_DISTANCE * speedBuff) * 3;
                    }    
                }
                
            }
            else if (dir == 0)
            {
                if (super.x - (UNIT_TRAVEL_DISTANCE * speedBuff) < 10)
                {
                    dir = 1;
                }
                else
                {
                    super.x -= (UNIT_TRAVEL_DISTANCE * speedBuff);
                }
                
            }
            else
            {
                if (super.x + width > Main.WIN_WIDTH - 10)
                {
                    dir = 0;
                }
                else
                {
                    super.x += (UNIT_TRAVEL_DISTANCE * speedBuff);
                }    
            }
            theX = super.x;
            
            if (super.x + width / 2 > Main.gameData.getShooter().getX() && super.x + width / 2 < Main.gameData.getShooter().getX() + Main.gameData.getShooter().getWidth() && Main.gameData.getShooter().getY() > height)
            {
                state = GameFigureState.BIG_HANDS_BOSS_LOCKED;
            }
        }
        
       if (super.state == GameFigureState.BIG_HANDS_BOSS_DAMAGED)
        {
            
            if (frameCount == 0)
            {
                leftHand.state = GameFigureState.BIG_HAND_DAMAGED;
                rightHand.state = GameFigureState.BIG_HAND_DAMAGED;
                leftHand.resetFC();
                rightHand.resetFC();
                loadExplodeImages();
                width = 150;
                height = 150;
                super.x += 40;
                super.y += 10;
            }
            if (frameCount < 48)
            { 
                if (explodeImages.size() == 0) loadExplodeImages();
                mainImage = explodeImages.get(frameCount / 3);
            }
            else
            {
                super.state = GameFigureState.STATE_DONE;
            }
            frameCount++;
        }
    }

    public void loadExplodeImages()
    {
        try {
                    mainImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Cannot open explosion.png.");
                    System.exit(-1);
                }
        for (int i = 0; i < 16; i++)
        {
            explodeImages.add(mainImage.getSubimage((int)(i % 4) * 128, (int)(i / 4) * 128, 128, 128));
        }
    }
    
    @Override
    public void takeDamage(double d) {
        currentHealth -= d;
        
        /*
        if (state != GameFigureState.BIG_HANDS_BOSS_DAMAGED && currentHealth <= 0){
            state = GameFigureState.BIG_HANDS_BOSS_DAMAGED;
            frameCount = 0;
            if(!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
                super.enemyFactory.removeEnemyCharater();
                
            }
        }    
         */
        
        //*************************************************************************vanne 
        
        if (currentHealth <= 0) {
            
             if (isSoundOn) //****************************sound implemented 
            {
                playMusic(explosion);
            }
            
        state = GameFigureState.BIG_HANDS_BOSS_DAMAGED;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState(int state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return (new Rectangle2D.Float(this.x, this.y, width, height));
    }
    
    @Override
    public int getFigureType() {
        return this.BIGHANDSBOSS;
    }
    
    @Override
    public void setSpeedBuff(double x){
       speedBuff = x;
   }
}
