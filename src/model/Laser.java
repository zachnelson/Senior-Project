package model;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Laser extends WeaponFigure {

    private LaserState laserState;
    
        //Target
    public Point2D.Float target;

    public Laser(float x, float y, float tx, float ty) {
        super(x, y, tx, ty, 3, 500);
        this.state = GameFigureState.STATE_ALIVE;
        this.damage = 3;
        
        this.target = new Point2D.Float(tx, ty);
        laserState = new LaserStateSpawned();
    }

    public void goNextState() {
        laserState.goNext(this);
    }

    @Override
    public void render(Graphics2D g) {
        laserState.render(this, g);
    }

    @Override
    public void update() {
        updateDamage();
        laserState.update(this);
        if (getStateByID() == 3) {
            this.state = GameFigureState.STATE_DONE;
        }
    }

    public void updateDamage(){
        this.damage = damage * damageMultiplier;
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return laserState.getCollisionBox(this);
    }

    @Override

    public void takeDamage(double damage) {

        return;
    }

    public void setLaserState(LaserState state) {
        this.laserState = state;
    }

    public LaserState getLaserState() {
        return laserState;
    }
    
    public Point2D.Float getTarget() {
        return this.target;
    }

    public int getStateByID() {

        if (laserState instanceof LaserStateSpawned) {
            return 1;
        } else if (laserState instanceof LaserStateBurning) {
            return 2;
        } else if (laserState instanceof LaserStateDone) {
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

    @Override
    public BufferedImage getMainImage() {
        if (getStateByID() == 2){
            return ((LaserStateBurning) laserState).getImage();
        } else{
            return null;
        }
    }
    
    
   
}
//comment asdf