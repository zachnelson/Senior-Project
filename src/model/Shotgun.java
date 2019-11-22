/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jiwer
 */
public class Shotgun extends WeaponFigure{

    private static final double baseDamage = 1;
    private static final double rateOfFire = 600; // in miliseconds
    
    private static final int UNIT_TRAVEL_DISTANCE = 20; // per frame move
        
    private int size = 8;
    
    private float startPosX, startPosY;
        
    public Shotgun(float sx, float sy, float tx, float ty){
        super(sx, sy, tx, ty, baseDamage, rateOfFire);
        setAngleDirection(sx, sy, tx, ty);
        setState(GameFigureState.STATE_ALIVE);
        startPosX = sx;
        startPosY = sy;
    }
    
    public void setAngleDirection(float sx, float sy, float tx, float ty){
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
    }

    
    @Override
    public double getDamage() {
        return this.damage;
    }


    @Override
    public void render(Graphics2D g) {
        g.setColor(new Color(0xe400ff));
        g.drawOval((int) (super.x - size / 2),
        (int) (super.y - size / 2),
        size, size);
    }
    
    private double getDistanceTravelled(){
        float deltaX = super.x - startPosX;
        float deltaY = super.y - startPosY;
        return Math.sqrt( (deltaX*deltaX) + (deltaY*deltaY) );
    }
    
    private void updateDamage(){
        double pix = getDistanceTravelled() / 30;

        this.damage = (baseDamage - (pix * 0.1)) * damageMultiplier;
    }

    @Override
    public void update() {
        updateState();
        updateDamage();
        if (state == GameFigureState.STATE_ALIVE) {
            updateLocation();
        } else if (state == GameFigureState.STATE_DYING) {
            //do nothing
        }
    }
    
    public void updateState() {
        if (state == GameFigureState.STATE_ALIVE) {
            if (isTargetReached()) {
                state = GameFigureState.STATE_DYING;
            } else if (super.damage <= 0){
               setState(GameFigureState.STATE_DYING);
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
                || super.x + size > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
                || super.y < Main.gamePanel.getLocation().y -150
                || super.y + size > (Main.gamePanel.getLocation().y + Main.gamePanel.getSize().height))
            return false;
        else {/*System.out.println("out of panel");*/return true;}
    }
    
    @Override
    public void takeDamage(double damage) {
        setTargetReached();
    }

        @Override
    public double getWidth() {
        return size;
    }

    @Override
    public double getHeight() {
        return size;
    }

    @Override
    public int getState() {
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return new Rectangle2D.Float (x - size / 2, y - size/2, size, size);
    }
}
    

