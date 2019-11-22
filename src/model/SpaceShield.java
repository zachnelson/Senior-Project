/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author jiwer
 */
public class SpaceShield extends WeaponFigure{
    //Time the space shield is activated
    long startTime;
    //Length of time in ms that the shield remains active
    int duration;
    double width, height;
    final int sizeChange = 2;
    int stateSizeChangedAmount;

    public SpaceShield(float sx, float sy, float tx, float ty) {
        // Only important part of this super call is the last parameter
        // which sets the fire rate to every 30 seconds.
        super(sx, sy, tx, ty, 0, 30*1000);
        this.damage = 0;
        startTime = System.currentTimeMillis();
        duration = 10 * 1000; // convert seconds to ms
        width = Shooter.getInstance().getWidth() + 10;
        height = Shooter.getInstance().getHeight() + 10;
        this.state = GameFigureState.SPACE_SHIELD_EXPAND;
        stateSizeChangedAmount = 0;
        
        // If all the parameters are 0, then this is the
        // secondaryWeapon holder in GameData. 
        // DO NOT SET SHOOTER'S SHIELD TO ACTIVE IN THIS CASE!!!!!!!
        if (sx != 0 && sy != 0 && tx != 0 && ty != 0){
            Shooter.getInstance().setShieldActive(true);
        }
    }
    
    private void setPosition(){
        this.x = Shooter.getInstance().getX();
        this.y = Shooter.getInstance().getY();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(3));
        g.drawOval((int) (super.x),
                (int) (super.y),
                (int)width, (int)height);
    }

    @Override
    public void update() {
        setPosition();
        //Check to see if the duration has passed
        if (System.currentTimeMillis()-startTime >= duration){
            this.state = GameFigureState.STATE_DYING;
        }
        
        if (this.state == GameFigureState.SPACE_SHIELD_EXPAND){
            this.x -= sizeChange*stateSizeChangedAmount;
            this.y -= sizeChange*stateSizeChangedAmount;
            this.width += sizeChange*2;
            this.height += sizeChange*2;
            stateSizeChangedAmount += 1;
            if (stateSizeChangedAmount==10){
                this.state = GameFigureState.SPACE_SHIELD_DEXPAND;
            }
        } else if (this.state == GameFigureState.SPACE_SHIELD_DEXPAND){
            this.x -= sizeChange*stateSizeChangedAmount;
            this.y -= sizeChange*stateSizeChangedAmount;
            this.width -= sizeChange*2;
            this.height -= sizeChange*2;
            stateSizeChangedAmount -= 1;
            if (stateSizeChangedAmount==0){
                this.state = GameFigureState.SPACE_SHIELD_EXPAND;
            }
        }
        else if (this.state == GameFigureState.STATE_DYING){
            this.state = GameFigureState.STATE_DONE;
            // Make shooter vulnerable to damage again!
            Shooter.getInstance().setShieldActive(false);
        }
    }

    @Override
    public void takeDamage(double d) {
        return; //do nothing
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
        return this.state;
    }

    @Override
    public void setState(int state) {
        this.state = state;
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return new Rectangle2D.Float ((int)x, (int)y, (int)width, (int)height);
    }
    
}
