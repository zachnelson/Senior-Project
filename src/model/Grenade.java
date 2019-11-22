package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Grenade extends WeaponFigure {

    private GrenadeState grenadeState;

    public Grenade(float x, float y, float tx, float ty) {
        super(x, y, tx, ty, 3, 2000);
        this.state = GameFigureState.STATE_ALIVE;
        this.damage = 3;
        grenadeState = new GrenadeStateSpawned();
        
        double angle = Math.atan2(Math.abs(ty - y), Math.abs(tx - x));
        dx = (float) (3 * Math.cos(angle));
        dy = (float) (3 * Math.sin(angle));
        
        if (tx > x && ty < y) { // target is upper-right side
            dy = -dy; // dx > 0, dx < 0
        } else if (tx < x && ty < y) { // target is upper-left side
            dx = -dx;
            dy = -dy;
        } else if (tx < x && ty > y) { // target is lower-left side
            dx = -dx;
        } else { // target is lower-right side
            // dx > 0 , dy > 0
        }
    }

    public void goNextState() {
        grenadeState.goNext(this);
    }

    @Override
    public void render(Graphics2D g) {
        grenadeState.render(this, g);
    }

    @Override
    public void update() {
        grenadeState.update(this);
        if (getStateByID() == 3) {
            this.state = GameFigureState.STATE_DONE;
        }
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return (Rectangle2D.Float) grenadeState.getCollisionBox(this);
    }

    @Override

    public void takeDamage(double damage) {

        return;
    }

    public void setGrenadeState(GrenadeState state) {
        this.grenadeState = state;
    }

    public GrenadeState getGrenadeState() {
        return grenadeState;
    }

    public int getStateByID() {

        if (grenadeState instanceof GrenadeStateSpawned) {
            return 1;
        } else if (grenadeState instanceof GrenadeStateExploding) {
            return 2;
        } else if (grenadeState instanceof GrenadeStateDone) {
            return 3;
        } else {
            return 4;  //return 4 for unknown state
        }
    }

    @Override
    public double getDamage() {
        return damage;
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
    public void setState(int state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }  
    
}

//comment asdf