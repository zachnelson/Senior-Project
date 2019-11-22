/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author jiwer
 */
public class Rifle extends WeaponFigure {

    private static int shotFired;
    private static final int SIZE = 15;
    private static final double baseDamage = 0.8;
    private static final double rateOfFire = 1500; // in miliseconds
    
    private static final int UNIT_TRAVEL_DISTANCE = 20; // per frame move
    
    public Rifle(float sx, float sy, float tx, float ty){
        super(sx, sy, tx, ty, baseDamage, rateOfFire);
        super.state = GameFigureState.STATE_ALIVE;
        
        double angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));
        dx = (float) (UNIT_TRAVEL_DISTANCE * Math.cos(angle));
        dy = (float) (UNIT_TRAVEL_DISTANCE * Math.sin(angle));
        
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
        try {
            mainImage = ImageIO.read(getClass().getResource("/model/assets/YellowBullet.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open YellowBullet.png");
            System.exit(-1);
        }
        if (sx == 0 && sy == 0 && tx == 0 && ty == 0){
            // Don't increment this when its the placeholder in GameData
            shotFired = 0;
        } else{
            shotFired++;
        }
    }
    
    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                SIZE, SIZE, null);
    }

    @Override
    public void update() {
        updateDamage();

        updateState();
        if (state == GameFigureState.STATE_ALIVE) {
            updateLocation();
        } else if (state == GameFigureState.STATE_DYING) {
//            updateSize();
        }
    }
    
     private void updateDamage(){
        this.damage = baseDamage * damageMultiplier;
    }
     
    public void updateState() {
        if (state == GameFigureState.STATE_ALIVE) {
            if (isTargetReached()) {
                state = GameFigureState.STATE_DYING;
            }
        } else if (state == GameFigureState.STATE_DYING) {
            state = GameFigureState.STATE_DONE;
        }
        if (!isInPanel()){
            state = GameFigureState.STATE_DONE;
        }
    }
    
    public void updateLocation() {
        super.x += dx;
        super.y += dy;
    }

    private boolean isInPanel(){
        if((super.x < Main.gamePanel.getLocation().x) 
                || super.x + SIZE > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
                || super.y < Main.gamePanel.getLocation().y -150
                || super.y + SIZE > (Main.gamePanel.getLocation().y + Main.gamePanel.getSize().height))
            return false;
        else {/*System.out.println("out of panel");*/return true;}
    }
    
    @Override
    public void takeDamage(double d) {
        setTargetReached();
    }

    @Override
    public void setNotReady() {
        if (shotFired >= 6){
            super.setNotReady();
            shotFired = 0;
        }
    }

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
    public void setState(int state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return new Rectangle2D.Float (x - SIZE / 2, y - SIZE/2, SIZE, SIZE);
    }
    
}
