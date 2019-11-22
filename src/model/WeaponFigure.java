/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.Duration;
import java.time.Instant;

/**
 *
 * @author jiwer
 */
public abstract class WeaponFigure extends GameFigure{
    
    private double fireRate; // Time to wait between shots in ms
    private boolean readyToFire = true; // Flag to denote the weapon is ready
    private boolean targetReached = false; // Flag to denote that the weapon has reached it's target

    public static double damageMultiplier = 1;

    
    protected float dx; // displacement at each frame
    protected float dy; // displacement at each frame
    
    /**
     * @param sx start x of the weapon
     * @param sy start y of the weapon
     * @param tx target x of the weapon
     * @param ty target y of the weapon
     */
    public WeaponFigure(float sx, float sy, float tx, float ty) {
        super(sx, sy);
        damage = 1;
        fireRate = 1;
    }
    
    public WeaponFigure(float sx, float sy, float tx, float ty, double damage, double fireRate){
        super(sx,sy);
        this.damage = damage;
        this.fireRate = fireRate;
    }
               
    /** This needs to be implemented in the weapon class
     *  to also get any damage modifier bonuses the player
     *  may also have. Can also incorporate a loss of damage
     *  under certain scenarios (I.E. less damage more distance traveled.
     **/
    

    public double getDamage(){
        return damage;
    }
    
    public void setDamageMultiplier(double x){
        damageMultiplier = x;
    }

    
    public boolean isTargetReached(){
        return targetReached;
    }
    
    public void setTargetReached(){
        this.targetReached = true;
    }

    public boolean isReady(){
        return this.readyToFire;
    }
    
    public void setNotReady(){
        readyToFire = false;
        
        Thread fireDelayThread = new Thread(){
            public void run(){
                long start = System.currentTimeMillis();
                while (System.currentTimeMillis()-start < fireRate){
                    // just wait!
                }
                readyToFire = true;
            }
        };
        fireDelayThread.start();
    }
}


