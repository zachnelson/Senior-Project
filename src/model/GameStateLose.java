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
public class GameStateLose implements GameState {

    public GameStateLose(){
        System.out.println("Game over Lose State --Nathan");
    }


    @Override
    public void goNext(GameData context) {
        
    }

 
    
}

