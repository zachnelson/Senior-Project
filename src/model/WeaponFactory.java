/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jiwer
 */
public class WeaponFactory {
    
    public WeaponFactory(){
        
    }
    
    public ArrayList<WeaponFigure> buildMainWeapon(Shooter shooter, int tx, int ty){
        WeaponFigure w = (WeaponFigure) Main.gameData.getMainWeapon();
        Class<?> clazz = w.getClass();
        try{
            if (w.isReady()){
                w.setNotReady();
                return createWeapon(clazz, shooter.getNozzlePosition().x, shooter.getNozzlePosition().y, tx, ty);
            }
        } catch (IllegalArgumentException e) {
            Logger.getLogger(WeaponFactory.class.getName()).log(Level.SEVERE, "Unable to Create Main Weapon", e);
        } catch (Exception ex) {
            Logger.getLogger(WeaponFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ArrayList<WeaponFigure> buildSeconaryWeapon(Shooter shooter, int tx, int ty){
        WeaponFigure w = (WeaponFigure) Main.gameData.getSecondaryWeapon();
        Class clazz = w.getClass();
        try{
            if (w.isReady()){
                w.setNotReady();
                return createWeapon(clazz, shooter.getNozzlePosition().x, shooter.getNozzlePosition().y, tx, ty);    
            }
        } catch (IllegalArgumentException e) {
            Logger.getLogger(WeaponFactory.class.getName()).log(Level.SEVERE, "Unable to Create Secondary Weapon", e);
        } catch (Exception ex) {
            Logger.getLogger(WeaponFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private ArrayList<WeaponFigure> createWeapon(Class clazz, float sx, float sy, float tx, float ty) throws Exception{
        ArrayList<WeaponFigure> weapons = new ArrayList<>();
        if (clazz == Shotgun.class){
            ShotgunFactory fact = new ShotgunFactory(sx, sy, tx, ty);
            weapons = fact.createFigures();
        } else {
            for (Constructor<?> ctor : clazz.getConstructors()){
                Class<?>[] paramTypes = ctor.getParameterTypes();

                if (paramTypes.length ==4){
                    Object[] args = new Object[paramTypes.length];
                    args[0]=sx;
                    args[1]=sy;
                    args[2]=tx;
                    args[3]=ty;
                    weapons.add((WeaponFigure) ctor.newInstance(args));
                }
            }
        }
        if (weapons.size() > 0){
            return weapons;
        } else{
            throw new IllegalArgumentException("Don't know how to instantiate " + Main.gameData.getMainWeapon().getClass());
        }
    }
}
