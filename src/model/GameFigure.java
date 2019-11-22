package model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class GameFigure implements CollisionBox {

    // public for a faster access during animation
    protected float x;
    protected float y;
    protected BufferedImage mainImage;
    protected int state;
    protected double damage;
    protected boolean frozen = false;

    public GameFigure(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // how to render on the canvas
    public abstract void render(Graphics2D g);

    // changes per frame
    public abstract void update();

    /////////getters and setters/////////////////

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public BufferedImage getMainImage() {
        return mainImage;
    }

    public abstract void takeDamage(double d);

    public abstract double getWidth();

    public abstract double getHeight();

    public abstract int getState();

    public abstract void setState(int state);

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
    



}
