package controller;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.EnemyFigure;

import model.Barrel;
import model.BonusFigure;
import model.ExplosiveTrap;

import model.FreezerEnemy;
import model.SpikeTrap;
import model.GameFigure;

import model.GameFigureState;
import model.GameStatePaused;

import model.Grenade;
import model.Laser;
import model.Pistol;
import model.Shooter;
import model.Shotgun;
import model.Trail;

import model.Upgrade;
import model.WeaponFigure;


public class Animator implements Runnable {

    private final int FRAMES_PER_SECOND = 40;
    private boolean testOpen = false;

    public void setTestOpen(boolean testOpen) {
        this.testOpen = testOpen;
    }

    @Override
    public void run() {
       // boolean running = false;
        boolean running = Main.gameData.getRunning();
       // System.out.println(running + " = running");
        while (running) {
            running = Main.gameData.getRunning();
           // System.out.println(running + " = running");
            long startTime = System.currentTimeMillis();

            if (!(Main.gameData.paused())) {
                processCollisions();

                Main.gameData.update();
            }
            try {
                Main.gamePanel.gameRender();
            } catch (IOException ex) {
                Logger.getLogger(Animator.class.getName()).log(Level.SEVERE, null, ex);
            }
            Main.gamePanel.printScreen();
            Main.gameData.getStatDisplay().gameRender();
            Main.gameData.getStatDisplay().printScreen();

            //            Main.barPanel.gameRender();
            //            Main.barPanel.printScreen();
            long endTime = System.currentTimeMillis();
            int sleepTime = (int) (1.0 / FRAMES_PER_SECOND * 1000)
                    - (int) (endTime - startTime);

            if (sleepTime > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } catch (InterruptedException e) {

                }
            }
//            if (Main.gameData.allEnemiesDead()) {
//                CharaterSheet charaterSheet = Main.gameData.getCharaterSheet();
//
//                if (Main.gameData.getEnemyFigures().size() == 0 && !testOpen && !charaterSheet.loseCondition()) {
//                    testOpen = true;
//                    charaterSheet.activateCharaterSheet();
//                }
//            }
        }
        //System.exit(0);
    }

    private void processCollisions() {

        for (GameFigure f : Main.gameData.getEnemyFigures()) {
            Rectangle2D.Float enemyBox = f.getCollisionBox();
            Iterator<GameFigure> iter = Main.gameData.getFriendFigures().iterator();
            while (iter.hasNext()) {
                GameFigure t = iter.next();
                Rectangle2D.Float friendBox = t.getCollisionBox();
                if (enemyBox.intersects(friendBox)) {
                    if (isValidTarget(t, f)){
                        if ((f instanceof FreezerEnemy && t instanceof Shooter)) {
                            if (checkPixelCollision(t, f)) {
                                Main.gameData.getShooter().setFrozen(true);
                                //t.takeDamage(f.damage);
                            }
                        } else if (f instanceof SpikeTrap && t instanceof Shooter) {
                            if(f.getState() == GameFigureState.SPIKE_TRAP_STATE_DOWN) {
                                f.setState(GameFigureState.SPIKE_TRAP_STATE_UP);
                                t.takeDamage(f.getDamage());
                            }
                        } else if (f instanceof ExplosiveTrap && t instanceof Shooter) {
                            f.setState(GameFigureState.EXPLOSIVE_TRAP_STATE_TRIGGERED);
                            t.takeDamage(f.getDamage());
                        }
                        else {
                            if (t instanceof Upgrade) {
                                // Do nothing

                            } else if(f instanceof Barrel && t instanceof Shooter){

                            } 
                            else if (f instanceof Trail && t instanceof WeaponFigure) {
                                //Do nothing
                            }    
                            else if(f instanceof Barrel && t instanceof Shooter){

                                float currentX = Main.gameData.getShooter().getX();
                                Main.gameData.getShooter().setX(currentX - 20);
                            } else if (t instanceof Shotgun || t instanceof Grenade) {
                                t.takeDamage(f.getDamage());
                                f.takeDamage(t.getDamage());
                                ((EnemyFigure)f).addToHitWeaponList((WeaponFigure)t);
                            } else if (checkPixelCollision(t, f)) {
                                t.takeDamage(f.getDamage());
                                f.takeDamage(t.getDamage());
                                if (!(t instanceof Shooter)){
                                    ((EnemyFigure)f).addToHitWeaponList((WeaponFigure)t);
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        
        
       //******************************vanne*************************
        // allow collision for block destruction 
        
        for (GameFigure f : Main.gameData.getBonusFigures()) {
            Rectangle2D.Float enemyBox = f.getCollisionBox();
            Iterator<GameFigure> iter = Main.gameData.getFriendFigures().iterator();
            while (iter.hasNext()) {
                GameFigure t = iter.next();
                Rectangle2D.Float friendBox = t.getCollisionBox();
                if (enemyBox.intersects(friendBox)) {
                    if (isValidTarget(t, f)){
                        if (checkPixelCollision(t, f)) {
                            f.takeDamage(t.getDamage());
                            if (!(t instanceof Shooter))
                                ((BonusFigure)f).addToHitWeaponList((WeaponFigure)t);
                        }
                    }
                }
            }
        }
        
        
        //***********************************************************
         
        
        
        
        
        
        
        
        
        
    }

    public boolean checkPixelCollision(GameFigure t, GameFigure f) {
        if (t.getMainImage() == null || f.getMainImage() == null) {
            //Image not found, so assume collision box intersection is enough
            return true;
        }
        float width1 = t.getX() + t.getMainImage().getWidth() - 1;
        float width2 = f.getX() + f.getMainImage().getWidth() - 1;
        float height1 = t.getY() + t.getMainImage().getHeight() - 1;
        float height2 = f.getY() + f.getMainImage().getHeight() - 1;

        int xStart = (int) Math.max(t.getX(), f.getX());
        int xEnd = (int) Math.min(width1, width2);
        int yStart = (int) Math.max(t.getY(), f.getY());
        int yEnd = (int) Math.min(height1, height2);

        int totalY = Math.abs(yEnd - yStart);
        int totalX = Math.abs(xEnd - xStart);

        for (int y = 1; y < totalY - 1; y++) {
            int ny = Math.abs(yStart - (int) t.getY()) + y;
            int ny1 = Math.abs(yStart - (int) f.getY()) + y;

            for (int x = 1; x < totalX - 1; x++) {
                int nx = Math.abs(xStart - (int) t.getX()) + x;
                int nx1 = Math.abs(xStart - (int) f.getX()) + x;
                try {

                    if (t instanceof Pistol) {

                    }
                    if (((t.getMainImage().getRGB(nx, ny) & 0xFF000000) != 0x00) && ((f.getMainImage().getRGB(nx1, ny1) & 0xFF000000) != 0x00)) {
                        return true;
                    }
                } catch (Exception e) {
//                     System.out.println("s1nx+","+ny+"  -  s2 = "+nx1+","+ny1);
                    //System.out.println();
                }
            }
        }
        return false;
    }


    private boolean isValidTarget(GameFigure friend, GameFigure enemy){
        if (!(friend instanceof WeaponFigure)){
            // Friend is not a weapon, so it's valid
            return true;
        } else if (!(enemy instanceof EnemyFigure) && !(enemy instanceof BonusFigure)){
            // Enemy is not an EnemyFigure or BonusFigure, so it's valid
            return true;
        } else{
            if (enemy instanceof EnemyFigure){
                // Return true if EnemyFigure has not been hit by this WeaponFigure
                return !((EnemyFigure)enemy)
                    .isInHitWeaponList((WeaponFigure)friend);
            } else if (enemy instanceof BonusFigure){
            // Return true if EnemyFigure has not been hit by this WeaponFigure
                return !((BonusFigure)enemy)
                    .isInHitWeaponList((WeaponFigure)friend);
            } else{
                return false;
            }
        }
    }

}
