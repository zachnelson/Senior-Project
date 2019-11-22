
package model;

import controller.Main;
import java.util.ArrayList;



public abstract class BonusFigure extends GameFigure{
    
 ArrayList<WeaponFigure> hitWeaponsList;
    
    public BonusFigure(float x, float y) {
        super(x, y);
    }
    
    protected double health;
    protected boolean dead = false;
    protected int pointValue;
    
    // protected BonusFactory bonusFactory = Main.gameData.getBonusFacotry();  
    protected BonusFactory bonusFactory = Main.gameData.getBonusFacotry();
    
    public abstract int getBonusType();
    public abstract void setSpeedBuff(double x);
    //enemy charaters should be listed starting at 1
    
     public final static int BLOCK1 = 1;           // vanne sprint 5 
     public final static int BLOCK2 = 2;  
     public final static int BLOCK3 = 3; 
     public final static int BLOCK4 = 4; 
     
    //projectiles should be listed starting at 1000
   
    
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
