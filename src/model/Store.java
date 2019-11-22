

package model;

import controller.Main;
import java.io.IOException;

/**
 *
 * @author Zach
 */
public class Store{
    
    private boolean w = true;
    private boolean a = true;
    private boolean s = true;
    private boolean d = true;
    
    public void init() throws IOException{
        
        Main.gameData.setScore(10);
        Main.gamePanel.setAccessedStore(true);

        
        Shooter shooter = null;
        shooter = (Shooter) Main.gameData.getFriendFigures().get(0);
        shooter.setX(550);
        shooter.setY(500);

        
        Main.gamePanel.setLevelBackgroundImage("store/storefloor");
        Main.gameData.getMiscFigures().add(new StoreItem(500, 100, "storecounter", 250, 250));
        Main.gameData.getMiscFigures().add(new StoreRobot(580, 175, 80, 80));
        Main.gameData.getMiscFigures().add(new StoreItem(75, 0, "tube", 140, 240));
        Main.gameData.getMiscFigures().add(new StoreItem(250, 0, "bookshelf", 140, 180));
        Main.gameData.getMiscFigures().add(new StoreItem(750, 0, "bookshelf2", 138, 130));
        Main.gameData.getMiscFigures().add(new StoreItem(920, 0, "hologram", 154, 173));
//        Main.gameData.getMiscFigures().add(new StoreItem(520, 260, "OOS", 57, 41));
//        Main.gameData.getMiscFigures().add(new StoreItem(620, 260, "OOS", 57, 41));
        Main.gameData.getMiscFigures().add(new StoreItem(510, 200, "upgrade1", 50, 50));
        Main.gameData.getMiscFigures().add(new StoreItem(600, 260, "upgrade4", 50, 50));
        Main.gameData.getMiscFigures().add(new StoreItem(690, 200, "upgrade3", 50, 50));
        Main.gameData.getMiscFigures().add(new StoreItem(900, 475, "wires", 215, 230));
        Main.gameData.getMiscFigures().add(new StoreItem(1085, 255, "exit", 185, 113));
    }

    public boolean getW() {
        return w;
    }

    public void setW(boolean w) {
        this.w = w;

    }

    public boolean getA() {
        return a;
    }

    public void setA(boolean a) {
        this.a = a;
    }


    public boolean getS() {
        return s;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public boolean getD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }
    
    
}


