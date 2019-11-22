/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Rectangle;
import view.GamePanel;

/**
 *
 * @author jiwer
 */
public class SettingsData {
        
     public void checkSettings(int x, int y){
         GamePanel game = Main.gamePanel;
         
         // Make sure current Game Panel state is Settings
         if (game.State == GamePanel.STATE.SETTINGS) {
            //Pressed Mute Music Button inside Settings
           if(x > 125  && x <= 350){
               if(y >= 150 && y <= 220) {      
                   game.setMusicMute(true);
               }
           }

           //Pressed UnMute Button inside Mute Music
           if(x > 450 && x <= 720 ){
               if(y >= 150 && y <= 220) {
                   game.setMusicMute(false);
               }
           }
        
        // added by vanne 
            Rectangle muteSoundButton = new Rectangle( 125, 255, 225, 40);
            Rectangle unMuteSoundButton = new Rectangle( 450, 255, 225, 40);
            if (muteSoundButton.contains(x, y)) {
                //Pressed Mute Sound vanne 
                    game.setSoundMute(true);
            }
            else if (unMuteSoundButton.contains(x, y)) {
                    game.setSoundMute(false);
            }
        }
     }
}
