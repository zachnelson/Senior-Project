

package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class LightningBoltAnimation extends GameFigure{

    private BufferedImage lightningSheet;
    private boolean displayLightning;
    private int frame;
    private int height;
    private int width;
    private int imgX;
    private int imgY;
    private GameFigure ef;
    
    public LightningBoltAnimation(float x, float y, GameFigure ef) {
        super(x, y);
        try {
                super.mainImage = ImageIO.read(getClass().getResource("/model/assets/lightningboltsheet.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open lightningboltsheet.png");
                System.exit(-1);
        }
        this.ef = ef;
        this.displayLightning = true;
        this.width = 51;
        this.height = 186;
        this.frame = 1;
        this.imgX = 0;
        this.imgY = 48;
        /* 
        Sprite Sheet Coordinates
        1   0, 48, 51, 234
        2   113, 42, 191, 232
        3   236, 17, 303, 230
        4   337, 19, 388, 226
        5   459, 20, 507, 216
        6   543, 19, 608, 245
        7   653, 23, 728, 226    
        */
    }

    @Override
    public void render(Graphics2D g) {  
        if(displayLightning){
            if(ef instanceof StoreRobot){
                StoreRobot robot;
                robot = (StoreRobot) ef;
                robot.powerOff();
            }
            switch(frame){
                case(11):
                    width = 78;
                    height = 190;
                    imgX = 113;
                    imgY = 42;
                    break;
                case(21):
                    width = 67;
                    height = 213;
                    imgX = 236;
                    imgY = 17;
                    break;
                case(31):
                    width = 51;
                    height = 207;
                    imgX = 337;
                    imgY = 19;
                    break;
                case(41):
                    width = 48;
                    height = 196;
                    imgX = 459;
                    imgY = 20;
                    break;
                case(51):
                    width = 65;
                    height = 226;
                    imgX = 543;
                    imgY = 14;
                    break;
                case(61):
                    width = 75;
                    height = 203;
                    imgX = 653;
                    imgY = 23;
                    break;
                default:
                    break;
            }
            
            //get correct image from sprite sheet
            lightningSheet = mainImage.getSubimage(imgX, imgY, width, height);
            
            //draw correct sprite on top of enemy
            if(ef instanceof BigHandsBoss)
                g.drawImage(lightningSheet, (int)ef.getX() + 75, (int)ef.getY(), width, (int)ef.getHeight() - 50, null);
            else if(ef instanceof ChargerEnemy)
                g.drawImage(lightningSheet, (int)ef.getX() + 50, (int)ef.getY(), width, (int)ef.getHeight() - 50, null);
            else
                g.drawImage(lightningSheet, (int)ef.getX(), (int)ef.getY(), width, (int)ef.getHeight(), null);
            
            //once last image is displayed, set displayLightning to false
            if(frame == 61){
                displayLightning = false;
                for (int i=0; i < Main.gameData.getMiscFigures().size(); i++){
                    if (Main.gameData.getMiscFigures().get(i) == this){
                        Main.gameData.getMiscFigures().remove(i);
                        break;
                    }
                }
            }
            
            frame++;
        }
    }

    public boolean isDisplayLightning() {
        return displayLightning;
    }

    public void setDisplayLightning(boolean displayLightning) {
        this.displayLightning = displayLightning;
    }
    
    

    @Override
    public void update() {}

    @Override
    public void takeDamage(double d) {}

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState(int state) {}

    @Override
    public Rectangle2D.Float getCollisionBox() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
