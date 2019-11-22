package controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import model.Inventory;
import model.Observer;
import model.Laser;
import model.Shooter;
import model.StoreItem;
import model.WeaponInventory;


public class KeyController extends KeyAdapter {
    private double currentHealth, maxHealth;
    private double agility;
    
    @Override
    public void keyPressed(KeyEvent e) {

        Shooter shooter = Shooter.getInstance();
        shooter.getDirections()[e.getKeyCode()] = true;
        
        Inventory slot1 = (Inventory) Main.gameData.getInventoryFigures().get(0);
        Inventory slot2 = (Inventory) Main.gameData.getInventoryFigures().get(1);
        slot1.numPressed[e.getKeyCode()] = true;
        slot2.numPressed[e.getKeyCode()] = true;   
        
        StoreItem[] si = new StoreItem[3];
        int counter = 0;
        for(int i = 0; i < Main.gameData.getMiscFigures().size(); i++){
            if(Main.gameData.getMiscFigures().get(i) instanceof StoreItem){
                StoreItem siTemp = (StoreItem) Main.gameData.getMiscFigures().get(i);
                if(siTemp.getType() != 0){
                    si[counter] = (StoreItem) Main.gameData.getMiscFigures().get(i);
                    si[counter].numPressed[e.getKeyCode()] = true;
                    counter++;
                }
            }
        }
        
        //------------------------------------------------
        //If space is clicked move weapon selector to next
        //------------------------------------------------
        if(e.getKeyChar() == KeyEvent.VK_SPACE){
            WeaponInventory weaponInventory = (WeaponInventory)Main.gameData.getWeaponInventory().get(0);
            weaponInventory.selectNextWeapon();
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        Shooter shooter = Shooter.getInstance();
        shooter.getDirections()[e.getKeyCode()] = false;  
        
        Inventory slot1 = (Inventory) Main.gameData.getInventoryFigures().get(0);
        Inventory slot2 = (Inventory) Main.gameData.getInventoryFigures().get(1);
        slot1.numPressed[e.getKeyCode()] = false;
        slot2.numPressed[e.getKeyCode()] = false;
        //System.out.println("this is agility in key controller" + agility);
        
        StoreItem[] si = new StoreItem[3];
        int counter = 0;
        for(int i = 0; i < Main.gameData.getMiscFigures().size(); i++){
            if(Main.gameData.getMiscFigures().get(i) instanceof StoreItem){
                StoreItem siTemp = (StoreItem) Main.gameData.getMiscFigures().get(i);
                if(siTemp.getType() != 0){
                    si[counter] = (StoreItem) Main.gameData.getMiscFigures().get(i);
                    si[counter].numPressed[e.getKeyCode()] = false;
                    counter++;
                }
            }
        }
    }

    
    public void update(double currentHealth, double maxHealth, double agility) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.agility = agility;
    }
    
}


