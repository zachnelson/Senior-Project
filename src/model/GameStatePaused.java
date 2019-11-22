/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;

/**
 *
 * @author Nathan
 */
public class GameStatePaused implements GameState, Observer {

    private double currentHealth;
    private double maxHealth;
    private double agility = 5;
    private double armorPercent = 0;
    private final Subject shooterSubject;
    private final double observerID;
    private static int observerIDTracker = 0;
    private int enemies;

    public GameStatePaused(Subject shooter) {
        this.maxHealth = 10;
        this.currentHealth = 10;
        this.shooterSubject = shooter;
        this.observerID = ++observerIDTracker;
        // System.out.println("New Observer " + this.observerID);
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
        System.out.println("accessed go next in pause");
            if ((context.getFriendFigures().size() == 0) || context.lost() || context.getCharacterSheet().loseCondition()) {
                System.out.println("landerd in lose");
                context.setGameState(new GameStateLose());
            } else if (context.getWin()) {
                System.out.println("landed in win");
                context.setGameState(new GameStateWin());
            } else {
                System.out.println("landed in else");
                play(context);
            }
    }

    private void play(GameData context) {
        context.setGameState(new GameStatePlaying(this.shooterSubject));
    }

}

