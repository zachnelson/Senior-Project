package model;

import controller.Main;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cardy
 */
public abstract class EnemyFigure extends GameFigure {

    ArrayList<WeaponFigure> hitWeaponsList;
    
    public EnemyFigure(float x, float y) {
        super(x, y);
    }
    
    protected double health;
    protected boolean dead = false;
    protected int pointValue;
    protected EnemyFactory enemyFactory = Main.gameData.getEnemyFacotry();
    
    public abstract int getFigureType();
    public abstract void setSpeedBuff(double x);
    //enemy charaters should be listed starting at 1
    public final static int CHARGER = 1;
    public final static int DIGGER = 2;
    public final static int FLYING = 3;
    public final static int FREEZER = 4;
    public final static int BIGHANDSBOSS = 5;
    public final static int BIGHAND = 6;
    public final static int LASER = 7;
    public final static int BARREL = 8;
    public final static int SPLITTER = 9;
    public final static int BOUNCER = 10;
    public final static int TRAILER = 11;
    public final static int TRAIL = 12;
    public final static int SPIKER = 13;
    
    //traps should be listed starting at 100
    public final static int SPIKETRAP = 100;
    public final static int EXPLOSIVETRAP = 101;
   
    
    //projectiles should be listed starting at 1000
    public final static int DIGGERSHOT = 1000;
    public final static int LASERSHOT = 1001;
    public final static int SPIKERSHOT = 1002;
    
    public void IncreaseScore(int pointValue) {
        Main.gameData.setScore(pointValue);
        Main.game.setScore();
    }
    
    //Keep a list of WeaponFigures that have hit this EnemyFigure
    public void addToHitWeaponList(WeaponFigure figure){
        if (hitWeaponsList == null){
            hitWeaponsList = new ArrayList<>();
        }
        hitWeaponsList.add(figure);
    }
    
    // Return true if the WeaponFigure has hit this EnemyFigure already
    public boolean isInHitWeaponList(WeaponFigure figure){
        if (hitWeaponsList == null){
            hitWeaponsList = new ArrayList<>();
        }
        return hitWeaponsList.contains(figure);
    }


}

