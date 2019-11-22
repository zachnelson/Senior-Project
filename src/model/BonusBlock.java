/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static model.GameData.upgrade3;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;




public class BonusBlock extends BonusFigure{
    
     private int height;
    private int width;
    private int frame;
    
    
    //vanne 
    private double velocityVert;               // vanne 
     private double velocityHoriz;             // vanne 
    
     private final double FRICTION = 0.8;
      private double friction = 0.8;
     
     
    private final Random rand;
    //vanne 
    private int rotationAngle;

    public BonusBlock(int x, int y) {
        super(x, y);
        super.health = 5;
        super.damage = 0;
        frame = 0;
        
         velocityVert = 0;
        velocityHoriz = 0;
        
        
        rotationAngle = 0;
        this.rand = new Random();

        super.state = GameFigureState.BLOCKBONUS_STATE_STARTED;
        try {
            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/block23.png")); // block3
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open barrel.png");
            System.exit(-1);
        }
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
    }

    @Override
    public void render(Graphics2D g) {

        // vanne 
          g.drawImage( rotateImage( mainImage, rotationAngle) , (int) super.x, (int) super.y, width, height, null);
        //g.drawImage(mainImage, (int) super.x, (int) super.y, width, height, null);
        //g.drawImage(rotateImage(image, rotationAngle), (int) super.x, (int) super.y, width, height, null);
    }

    @Override
    public void takeDamage(double damage) {
        //damage = 1;
//        System.out.println("Damage: " + damage);
        health -= damage;
        //         System.out.println(health + damage);
        frame = 0;
        super.state = GameFigureState.BLOCKBONUS_STATE_HIT;
        if (super.health <= 0) {
            state = GameFigureState.BLOCKBONUS_STATE_DAMAGED;
            if (!super.dead) {
                super.dead = true;
                super.bonusFactory.removeBonusCharater();
            }
        }
    }

    @Override
    public void update() {
        
        // vanne 
        rotationAngle += 5;

        if (super.state == GameFigureState.BLOCKBONUS_STATE_DAMAGED) {
            //***vanne****
            if (isSoundOn) //****************************sound implemented 
            {
                playMusic(explosion);
            }
            //--------------------------------
            //Create treasure 
            //--------------------------------
           // upgrade3 = new Upgrade(this.x,  3);
           upgrade3 = new Upgrade(rand.nextInt(1100) + 1, rand.nextInt(500) + 1, 3);
           List<GameFigure> mfigures = Main.gameData.getMiscFigures();
           mfigures.add(upgrade3);
            super.state = GameFigureState.STATE_DONE;

        }
        if (super.state == GameFigureState.BLOCKBONUS_STATE_HIT) {
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
                super.state = GameFigureState.BLOCKBONUS_STATE_STARTED;
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
    public int getBonusType() {
        return this.BLOCK1;
    }

    @Override
    public void setSpeedBuff(double x) {
    }

    public BufferedImage rotateImage(BufferedImage imag, int n) { //n rotation in gradians
        double rotationRequired = Math.toRadians(n);
        double locationX = imag.getWidth() / 2;
        double locationY = imag.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage = new BufferedImage(imag.getWidth(), imag.getHeight(), imag.getType()); //20, 20 is a height and width of imag ofc
        op.filter(imag, newImage);

        //this.img = newImage;
        return (newImage);
    }
    
    
    //********************vanne 
    public void translate() {

        if (super.y + this.height + this.velocityVert > Main.WIN_HEIGHT - 100) {
            super.y = Main.WIN_HEIGHT - 100 - this.height;
        } else if (super.y + this.velocityVert < 0) {
            super.y = 0;
        } else {
            super.y += this.velocityVert;
        }

        if (super.x + this.width + this.velocityHoriz > Main.WIN_WIDTH) {
            super.x = Main.WIN_WIDTH - this.width;
        } else if (super.x + this.velocityHoriz < 0) {
            super.x = 1;
        } else {
            super.x += this.velocityHoriz;
        }

      

        velocityHoriz *= friction;
        velocityVert *= friction;
        
        //if(this.getCollisionBox().intersects(Main.gameData.getEnemyFigures().indexOf(Barrel))){
            
       // }
    }
    
    //***********************
    
    
    
}
