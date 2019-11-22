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
import model.GameFigure;

/**
 *
 * @author Administrator
 */
public class BigHand extends EnemyFigure{

    private int maxHealth = 30, currentHealth = 30;
    public int width;
    public int height;
    public int smashHeight = 192;
    public int smashWidth = 192;
    public int smashLocX;
    public int smashLocY;
    public int side;
    public int move = 0;
    public int frameCount; 
    private float radius = 150;
    private double angle;
    private double distance;
    private float startX;
    private float startY;
    public boolean atSmash = false;
    public boolean smashing = false;
    public boolean back = true;
    public final List<BufferedImage> explodeImages;
    public final List<BufferedImage> smashImages;
    public BufferedImage smash;
    private static final int UNIT_TRAVEL_DISTANCE = 7;
    private double speedBuff = 1;
    
    public BigHand(float x, float y, int side) {
        super(x,y);
        super.damage = 1;
        super.state = GameFigureState.BIG_HAND_APPEARED;
        frameCount = 0;
        this.side = side;
        if (side == 1)
        {
            angle = 2.0;
            setImage("/model/assets/lefthandopen.png");
        }
            
        else
        {
            angle = 1.14;
            setImage("/model/assets/righthandopen.png");
        }      
        width = mainImage.getWidth();
        height = mainImage.getHeight();
        explodeImages = new CopyOnWriteArrayList<>();
        smashImages = new CopyOnWriteArrayList<>();
        loadImages();
    }
    
    public void loadImages()
    {
        try {
                    smash = ImageIO.read(getClass().getResource("/model/assets/shockwave.png"));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Cannot open shockwave.png.");
                    System.exit(-1);
                }
        for (int i = 0; i < 25; i++)
        {
            smashImages.add(smash.getSubimage((int)(i % 5) * 192, (int)(i / 5) * 192, 192, 192));
        }
        
    }
    
    public void attack(int attack)
    {
        switch (attack)
        {
            case 1:
                state = GameFigureState.BIG_HAND_STRETCH_START;
                break;
            case 2:
                state = GameFigureState.BIG_HAND_SLAP_BACK;
                if (side == 1)
                {
                    setImage("/model/assets/lefthandside.png");
                }

                else
                {
                    setImage("/model/assets/righthandside.png");
                } 
                break;
            case 3:
                state = GameFigureState.BIG_HAND_SMASH_MOVE;
                if (side == 1)
                {
                    smashLocX =  (int)(Math.random() * (Main.WIN_WIDTH /2 - 100) + 100);
                    smashLocY = (int)(Math.random() * (Main.WIN_HEIGHT - 500) + 200);
                }
                else
                {
                    smashLocX = (int)(Math.random() * (Main.WIN_WIDTH / 2) + (Main.WIN_WIDTH / 2 - 100));
                    smashLocY = (int)(Math.random() * (Main.WIN_HEIGHT - 500) + 200);
                    
                }
                angle = Math.atan2((smashLocX - super.x), (smashLocY - super.y));
                distance = Math.sqrt(Math.abs(super.x - smashLocX) * Math.abs(super.x - smashLocX) 
                            + Math.abs(super.y - smashLocY) * Math.abs(super.y - smashLocY));
                startX = super.x;
                startY = super.y;
                break;
            default:
                break;
        }
        BigHandsBoss.attacking = true;
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
        if (smashing)
        {
            g.drawImage(smash, (int) super.x - 60, (int) super.y - 30,
                smashWidth, smashHeight, null);
        }
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                this.width, this.height, null);
        
    }

    @Override
    public void update() {
        if (state != GameFigureState.BIG_HAND_APPEARED && state!= GameFigureState.BIG_HAND_DAMAGED)
        {
            if(this.getCollisionBox().intersects(Main.gameData.getShooter().getCollisionBox()))
            {
                if(Main.animator.checkPixelCollision(this, Main.gameData.getShooter()))
                {
                    BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_RETURN;
//                    BigHandsBoss.leftHand.smashing = false;
//                    BigHandsBoss.rightHand.smashing = false;
                    back = false;
                }
            }
        }
        if (state == GameFigureState.BIG_HAND_RETURN)
        {
            if (frameCount < 25 && smashing)
            {
                smash = smashImages.get(frameCount);
                frameCount++;
            }
            else
            {
                smashing = false;
            }
            if (width > mainImage.getWidth())
            {
                width -= 4;
                height -= 4;
            }
            radius = 150;
            if (side == 1)
            {
                angle = 2.0;
            }
            else
            {
                angle = 1.14;
            } 
            double returnAngle;
            if (side == 1)
            {
                if (super.y <= BigHandsBoss.theY + 117)
                {
                    back = true;
                    if (BigHandsBoss.leftHand.back && BigHandsBoss.rightHand.back)
                    {
                        BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_APPEARED;
                        BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_APPEARED;
                        BigHandsBoss.leftHand.setImage("/model/assets/lefthandopen.png");
                        BigHandsBoss.rightHand.setImage("/model/assets/righthandopen.png");
                        BigHandsBoss.attacking = false;
                    }  
                }
                else
                {
                    returnAngle = Math.atan2((BigHandsBoss.theX - 20 - super.x), 
                                        (BigHandsBoss.theY + 117 - super.y));
                    super.x += (float)(((UNIT_TRAVEL_DISTANCE * speedBuff) * 3) * Math.sin(returnAngle));
                    super.y += (float)(((UNIT_TRAVEL_DISTANCE * speedBuff) * 3) * Math.cos(returnAngle));
                }
                
            }
            else
            {
                if (super.y <= BigHandsBoss.theY + 117)
                {
                    back = true;
                    if (BigHandsBoss.leftHand.back && BigHandsBoss.rightHand.back)
                    {
                        BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_APPEARED;
                        BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_APPEARED;
                        BigHandsBoss.leftHand.setImage("/model/assets/lefthandopen.png");
                        BigHandsBoss.rightHand.setImage("/model/assets/righthandopen.png");
                        BigHandsBoss.attacking = false;
                    }
                }
                else
                {
                    returnAngle = Math.atan2((BigHandsBoss.theX + 133 - super.x), 
                                        (BigHandsBoss.theY + 117 - super.y));
                    super.x += (float)(((UNIT_TRAVEL_DISTANCE * speedBuff) * 3) * Math.sin(returnAngle));
                    super.y += (float)(((UNIT_TRAVEL_DISTANCE * speedBuff) * 3) * Math.cos(returnAngle));
                }
                
            }
            
            
        }
        if (state == GameFigureState.BIG_HAND_STRETCH_START)
        {
            if (super.y > BigHandsBoss.theY + 50)
            {
                move += 1;
                super.y -= move;
            }
            else
            {
                BigHandsBoss.leftHand.move = 0;
                BigHandsBoss.rightHand.move = 0;
                BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_STRETCH_OUT;
                BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_STRETCH_OUT;
            }
                
        }
        else if (state == GameFigureState.BIG_HAND_STRETCH_OUT)
        {
                if (super.y + height > Main.WIN_HEIGHT - 100)
                {
                    BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.leftHand.move = 0;
                    BigHandsBoss.rightHand.move = 0;
                }
                else
                {
                    move += 2;
                    super.y += move;
                }
        }
        
        if (state == GameFigureState.BIG_HAND_SLAP_BACK)
        {
            if (super.y > BigHandsBoss.theY + 30)
            {
                if (side == 1)
                {
                    radius += 8;
                    angle += 2 * Math.PI / 200;
                    super.y = (float)(BigHandsBoss.theY - 10 + radius * Math.sin(angle));
                    super.x = (float)(BigHandsBoss.theX + 55 + radius * Math.cos(angle));
                }
                if (side == 2)
                {
                    radius += 8;
                    angle -= 2 * Math.PI / 200;
                    super.y = (float)(BigHandsBoss.theY - 10 + radius * Math.sin(angle));
                    super.x = (float)(BigHandsBoss.theX + 55 + radius * Math.cos(angle));
                }
            }
            else
            { 
                BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_SLAP_FORWARD;
                BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_SLAP_FORWARD;
            }    
        }
        else if (state == GameFigureState.BIG_HAND_SLAP_FORWARD)
        {
            if (radius < (int)Main.gameData.getShooter().getY() + (int)Main.gameData.getShooter().getHeight() - (int)BigHandsBoss.theY - 60)
            {
                radius += 3;
            }
            if (side == 1)
            {
                if (super.x + width < BigHandsBoss.theX + 90)
                {
                    angle -= 2 * Math.PI / 200;
                    super.y = (float)(BigHandsBoss.theY - 10 + radius * Math.sin(angle));
                    super.x = (float)(BigHandsBoss.theX + 55 + radius * Math.cos(angle));
                }
                else
                {
                    BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.leftHand.back = false;
                    BigHandsBoss.rightHand.back = false;
                }
            }
            if (side == 2)
            {
                if (super.x > BigHandsBoss.theX + 90)
                {
                    angle += 2 * Math.PI / 200;
                    super.y = (float)(BigHandsBoss.theY - 10 + radius * Math.sin(angle));
                    super.x = (float)(BigHandsBoss.theX + 55 + radius * Math.cos(angle));
                }
                else
                {
                    BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_RETURN;
                    BigHandsBoss.leftHand.back = false;
                    BigHandsBoss.rightHand.back = false;
                }
            }
        }
        if (state == GameFigureState.BIG_HAND_SMASH_MOVE)
        {
            double currentDistance = Math.sqrt(Math.abs(super.x - startX) * Math.abs(super.x - startX) 
                            + Math.abs(super.y - startY) * Math.abs(super.y - startY));
            
            if (Math.abs(currentDistance - distance) < 20)
            {
                atSmash = true;
            }
            else
            {
                super.x += (float)(((UNIT_TRAVEL_DISTANCE * speedBuff) * 3) * Math.sin(angle));
                super.y += (float)(((UNIT_TRAVEL_DISTANCE * speedBuff) * 3) * Math.cos(angle));
            }
            
            if (BigHandsBoss.leftHand.atSmash && BigHandsBoss.rightHand.atSmash)
            {
                BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_SMASH_UP;
                BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_SMASH_UP;
                BigHandsBoss.leftHand.atSmash = false;
                BigHandsBoss.rightHand.atSmash = false;
            }
        }
        else if (state == GameFigureState.BIG_HAND_SMASH_UP)
        {
            if (width < 100)
            {
                width+=2;
                height+=2;
            }
            else
            {
                BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_SMASH_DOWN;
                BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_SMASH_DOWN;
            }
        }
        else if (state == GameFigureState.BIG_HAND_SMASH_DOWN)
        {
                if (width > mainImage.getWidth())
                {
                    width -= 4;
                    height -= 4;
                }
                else
                {
                    width = mainImage.getWidth();
                    height = mainImage.getHeight();
                    BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_SMASH_WAVE;
                    BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_SMASH_WAVE;
                    
                }
                frameCount = 0;
        }
        else if (state == GameFigureState.BIG_HAND_SMASH_WAVE)
        {
            if (frameCount < 25)
            {
                smashing = true;
                smash = smashImages.get(frameCount);
            }
            else
            {
                BigHandsBoss.leftHand.smashing = false;
                BigHandsBoss.rightHand.smashing = false;
                BigHandsBoss.leftHand.state = GameFigureState.BIG_HAND_RETURN;
                BigHandsBoss.rightHand.state = GameFigureState.BIG_HAND_RETURN;
                BigHandsBoss.leftHand.back = false;
                BigHandsBoss.rightHand.back = false;
            }
            frameCount++;
        }
        
        if (state == GameFigureState.BIG_HAND_APPEARED)
        {
            if (side == 1)
            {
                super.x = BigHandsBoss.theX - 20;
                super.y = BigHandsBoss.theY + 117;
            }
            else
            {
                super.x = BigHandsBoss.theX + 133;
                super.y = BigHandsBoss.theY + 117;
            }
        }
        if (super.state == GameFigureState.BIG_HAND_DAMAGED)
        {
            if (frameCount == 0)
            {
                loadExplodeImages();
                width = 100;
                height = 100;
                super.x += 10;
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
    
    public void resetFC()
    {
        frameCount = 0;
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
        //Main.gameData.bigHandsBoss.takeDamage(damage / 2);
//        if (health <= 0){
//            state = GameFigureState.BIG_HAND_DAMAGED;
//            frameCount = 0;
//        }  
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
        if (!smashing)
        {
            return (new Rectangle2D.Float(this.x, this.y, width, height));
        }
        else
        {
            return (new Rectangle2D.Float((int) super.x - 60, (int) super.y - 30,
                smashWidth, smashHeight));
        }
        
    }

    @Override
    public int getFigureType() {
        return this.BIGHAND;
    }
    
    @Override
    public void setSpeedBuff(double x){
       speedBuff = x;
   }
}
