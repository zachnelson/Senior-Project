
package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Zach
 */
public class StoreRobot extends GameFigure{

    private BufferedImage img;
    private int imgWidth;
    private int imgHeight;
    private Shooter shooter;
    private boolean powerOn = true;
    private int frame = 0;
    
    public StoreRobot(float x, float y, int imgWidth, int imgHeight) {
        super(x, y);
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
        shooter = (Shooter) GameData.getShooter();
        try {
              img = ImageIO.read(getClass().getResource("/model/assets/store/storerobot.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open storerobot.png");
                System.exit(-1);
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(img, (int)super.x, (int)super.y, imgWidth, imgHeight, null);
    }
    @Override
    public void update() {
        // change robot facing direction to left
        if(shooter.getX() < super.x && powerOn){
            try {
              img = ImageIO.read(getClass().getResource("/model/assets/store/storerobot2.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open storerobot2.png");
                System.exit(-1);
            }
        }
        // change robot facing direction to right
        if(shooter.getX() > super.x && powerOn){
            try {
              img = ImageIO.read(getClass().getResource("/model/assets/store/storerobot.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open storerobot.png");
                System.exit(-1);
            }
        }
        //robot is shocked by upgrade 4
        if(!powerOn){
            switch(frame){
                case(0):
                    try {
                        img = ImageIO.read(getClass().getResource("/model/assets/store/storerobotshocked.png"));
                      } catch (IOException ex) {
                          JOptionPane.showMessageDialog(null, "Error: Cannot open storerobotshocked.png");
                          System.exit(-1);
                    }
                    break;
                case(11):
                    try {
                        img = ImageIO.read(getClass().getResource("/model/assets/store/storerobotoff.png"));
                      } catch (IOException ex) {
                          JOptionPane.showMessageDialog(null, "Error: Cannot open storerobotoff.png");
                          System.exit(-1);
                    }
                    break;
                default:
                    break;
            }
            frame++;
        }
        else if(powerOn)
            frame = 0;
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return(new Rectangle2D.Float(super.x , super.y, imgWidth, imgHeight));
    }
    
    public void powerOff(){
        powerOn = false;
        new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException ex) {
                        System.out.println("Turning robot power off didn't work!");
                    }
                    powerOn = true;
                }
            }.start();
    }

    public boolean isPowerOn() {
        return powerOn;
    }

    public void setPowerOn(boolean powerOn) {
        this.powerOn = powerOn;
    }
    
    @Override
    public double getWidth() {
        return imgWidth;
    }

    @Override
    public double getHeight() {
        return imgHeight;
    }
    
    //unused functions
    @Override
    public void takeDamage(double d) {}

    @Override
    public int getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState(int state) {}
    
}

