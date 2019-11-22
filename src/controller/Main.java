package controller;

import java.io.IOException;
import javax.swing.JFrame;
import model.GameData;
import view.StatsDisplay;
import view.BarPanel;
import view.GamePanel;
import view.MainWindow;

public class Main {

    // vanne 
    // second comment
    public static GamePanel gamePanel;
    public static BarPanel barPanel;
    public static StatsDisplay healthDisplay;
    public static GameData gameData;
    public static Animator animator;
    public static MainWindow game;
    public static int WIN_WIDTH = 1280;//1280
    public static int WIN_HEIGHT = 720; // 680

//    public static int WIN_HEIGHT = 780;

    public static void main(String[] args) {

        startRun();
    }

    public static void startRun() {
        animator = new Animator();
        gameData = new GameData();
        gamePanel = new GamePanel();
        barPanel = new BarPanel();
        game = new MainWindow();
        game.setTitle("Space Fingers");
        game.setSize(WIN_WIDTH, WIN_HEIGHT);
        game.setLocation(100, 0);
        game.setResizable(false); // window size cannot change
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.setVisible(true);

        // start animation
        new Thread(animator).start();
    }

    /*
    public static void restartRun() throws IOException {
        gameData.clearGame();
        gameData.initGame();
        gamePanel.setGameStarted(true);
        
         gameData.addEnemies(GamePanel.Level);
        
        //Call this to add the starting enemies
        
       // Main.gameData.addEnemies();                                            // vanne
        
        Main.gamePanel.setLevelBackgroundImage("backgroundImageMenu1");
    }
*/

    public static void endRun() {
        System.exit(0);
    }
}
