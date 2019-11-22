/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author jiwer
 */
public class ShotgunFactory {
    
    float startX, startY, targetX, targetY;
    
    private int UNIT_SPACE_DISTANCE = 40;
    
    public ShotgunFactory(float sx, float sy, float tx, float ty){
        startX = sx;
        startY = sy;
        targetX = tx;
        targetY = ty;
    }
    
    public ArrayList<WeaponFigure> createFigures(){
        ArrayList<WeaponFigure> weapons = new ArrayList<>();
        
        double angle = Math.atan2(Math.abs(targetY - startY), Math.abs(targetX - startX));
        
        float movePosX = 0;
        float movePosY = 0;

        if (Math.cos(angle) < 0.2){
            movePosX = (float) UNIT_SPACE_DISTANCE;
            movePosY = 0;
        }else if (Math.sin(angle) < 0.2){
            movePosX = 0;
            movePosY = (float) UNIT_SPACE_DISTANCE;
        } else{
            movePosX = (float) (UNIT_SPACE_DISTANCE * Math.cos(angle));
            movePosY = (float) (UNIT_SPACE_DISTANCE * Math.sin(angle));
        }
        
        
        
        if (targetX > startX && targetY < startY) { // target is upper-right side
            //Do nothing
        } else if (targetX < startX && targetY < startY) { // target is upper-left side
            movePosX = -movePosX;
        } else if (targetX < startX && targetY > startY) { // target is lower-left side
            movePosX = -movePosX;
            movePosY = -movePosY;
        } else { // target is lower-right side
            movePosX = -movePosX;
        }
        
        weapons.add(new Shotgun(startX-movePosX, startY-movePosY, targetX-movePosX, targetY-movePosY));
        weapons.add(new Shotgun(startX-(movePosX/2), startY-(movePosY/2), targetX-(movePosX/2), targetY-(movePosY/2)));
        weapons.add(new Shotgun(startX, startY, targetX, targetY));
        weapons.add(new Shotgun(startX+movePosX, startY+movePosY, targetX+movePosX, targetY+movePosY));
        weapons.add(new Shotgun(startX+(movePosX/2), startY+(movePosY/2), targetX+(movePosX/2), targetY+(movePosY/2)));
        
        return weapons;
    }
}

