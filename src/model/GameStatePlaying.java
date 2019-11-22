/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Nathan
 */
public class GameStatePlaying implements GameState, Observer {

    private double currentHealth = 10;
    private double maxHealth;
    private double armorPercent;
    private double agility;
    private final Subject shooterSubject;
    private final double observerID;
    private static int observerIDTracker = 0;

    public GameStatePlaying(Subject shooter) {
        this.shooterSubject = shooter;
        this.observerID = ++observerIDTracker;
        System.out.println("New Observer " + this.observerID);
        shooter.add(this);
    }

    @Override
    public void updateCharaterInfo(double currentHealth, double maxHealth, double agility, double armorPercent) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.agility = agility;
        this.armorPercent = armorPercent;
    }

    @Override
    public void goNext(GameData context) {
        if (currentHealth != 0) {
            pause(context);
        }
    }

    private void pause(GameData context) {
        context.setGameState(new GameStatePaused(this.shooterSubject));
    }

}

