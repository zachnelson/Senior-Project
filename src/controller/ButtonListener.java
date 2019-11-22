package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import view.GamePanel;
import view.MainWindow;

public class ButtonListener implements ActionListener {
    
    GamePanel game = new GamePanel() ; 

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == MainWindow.startButton){
            //Main.animator.running = true;
            //changed to
            //Main.gameData.setRunning(true);
            Main.gameData.play();
            //Call this to add the starting enemies, may be useless once
            // an enemy generator gets created
          //  Main.gameData.addEnemies();                                        // vanne 
            GamePanel.StartGame();
        }

        else if( ae.getSource() == MainWindow.helpButton ){
            //Main.animator.running = true;
            //changed to
            Main.gameData.setRunning(true);
            
            if (Main.gamePanel.State == GamePanel.STATE.HELP){
                GamePanel.unpauseGame();
            } else {
                GamePanel.HelpButton();
            }
        }else if( ae.getSource() == MainWindow.settingButton ){
           //Main.animator.running = true;
            //changed to
            Main.gameData.setRunning(true);
            if (Main.gamePanel.State == GamePanel.STATE.SETTINGS){
                GamePanel.unpauseGame();
            } else {
                GamePanel.settingButton();
            }
        }

        else if (ae.getSource() == MainWindow.restartButton)
        {
            
            GamePanel.restartGame();
            
        }
        else if (ae.getSource() == MainWindow.quitButton) {
            if (Main.gameData.getRunning()) {
                //Main.animator.running = false;
                Main.gameData.setRunning(false);
                Main.endRun();
            } 
            else {
                System.exit(0);
            }
       
        }

    }

}

