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
public class GameStateWin implements GameState {

    public GameStateWin() {
        System.out.println("Game over Win state --Nathan");

    }

    @Override
    public void goNext(GameData context) {
    }

}

