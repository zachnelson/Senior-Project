

package model;

import controller.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import view.GamePanel;

import view.MainWindow;



/**
 *
 * @author Zach
 */
public final class StoreItem extends GameFigure{
    
    private BufferedImage img;
    public boolean[] numPressed = new boolean[255];
    private int imgWidth;
    private int imgHeight;
    private String imgName;
    private Shooter shooter;
    private StoreRobot robot = null;
    private boolean bought = false;
    private int type = 0;
    private int cost = 0;
    private Font font = new Font("arial", Font.BOLD, 36);
    private boolean showPrice = false;
    private boolean steal = false;
    private boolean collidedA = false;
    private boolean collidedW = false;
    private boolean collidedS = false;
    private boolean collidedD = false;
    
    
    private Store store = Main.gameData.getStore();
    
    public StoreItem(float x, float y, String imgName, int imgWidth, int imgHeight) {
        super(x, y);
        try {
              img = ImageIO.read(getClass().getResource("/model/assets/store/" + imgName + ".png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open " + imgName + ".png");
                System.exit(-1);
        }
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.imgName = imgName;
        shooter = (Shooter) GameData.getShooter();
        setType(imgName);
    }
    
    @Override
    public void render(Graphics2D g) {
        g.drawImage(img, (int)super.x, (int)super.y, imgWidth, imgHeight, null);
        if(showPrice == true){
            g.setColor(Color.green);
            g.setFont(font);
            g.drawString("$" + cost, 590, 165);
        }
        else if(steal == true){
            g.setColor(Color.red);
            g.setFont(font);
            g.drawString("Steal?", 570, 165);
        }
    }

    @Override
    public void update() {
        
        if(shooter.getCollisionBox().intersects(this.getCollisionBox()) && "exit".equals(imgName)){
            try {
                leaveStore();
            } catch (IOException ex) {
                Logger.getLogger(StoreItem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(shooter.getCollisionBox().intersects(this.getCollisionBox()) && !"exit".equals(imgName)){
            if(shooter.getX() > x && shooter.getA()){
                store.setA(false);
                collidedA = true;
            }
            if(shooter.getX() < x && shooter.getD()){
                store.setD(false);
                collidedD = true;
            }
            if(shooter.getY() > y && shooter.getW()){
                store.setW(false);
                collidedW = true;
            }
            if(shooter.getY() < y && shooter.getS()){
                store.setS(false);
                collidedS = true;
            }
        }
        if(!shooter.getCollisionBox().intersects(this.getCollisionBox())){
            if(this.collidedA == true){
                collidedA = false;
                store.setA(true);
            }
            if(this.collidedS == true){
                collidedS = false;
                store.setS(true);
            }
            if(this.collidedW == true){
                collidedW = false;
                store.setW(true);
            }
            if(this.collidedD == true){
                collidedD = false;
                store.setD(true);
            }
        }
        
        //set robot instance
        if(robot == null)
            setRobot();
        
        //display price over robot's head
        showPrice = shooter.getCollisionBox().intersects(getCollisionBox()) && type != 0 
                && bought != true && robot.isPowerOn();
        
        //display "steal" over robot's head
        steal = shooter.getCollisionBox().intersects(getCollisionBox()) && type != 0 
                && bought != true && !robot.isPowerOn();
        
        //buy item
        if(shooter.getCollisionBox().intersects(getCollisionBox()) && numPressed[KeyEvent.VK_E] 
                && type != 0 && type != 3 && (Main.gameData.getScore() >= cost || 
                !robot.isPowerOn()) && bought != true && Main.gameData.getInventoryFigures().size() <= 3) {
            setBought();
        }    
        
        //buy health specifically
        else if(shooter.getCollisionBox().intersects(getCollisionBox()) && numPressed[KeyEvent.VK_E] 
                && type == 3 && shooter.getCurrentHealth() < shooter.getMaxHealth() && 
                (Main.gameData.getScore() >= cost || !robot.isPowerOn()) && bought != true){
            setBought();
        }
        
        //player doesnt have enough money
        else if(shooter.getCollisionBox().intersects(getCollisionBox()) && 
                numPressed[KeyEvent.VK_E] && type != 0 && Main.gameData.getScore() < cost 
                && bought != true && robot.isPowerOn()) {
            StoreItem temp = new StoreItem(430, 460, "robotcard", 387, 156);
            Main.gameData.getMiscFigures().add(temp);
            new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ex) {
                       // System.out.println("Removing robotcard didn't work!");
                    }
                    Main.gameData.getMiscFigures().remove(temp);
                }
            }.start();
        }
        
        //player's inventory is full
        else if(shooter.getCollisionBox().intersects(getCollisionBox()) && Main.gameData.getInventoryFigures().size() > 3 &&
                numPressed[KeyEvent.VK_E] && type != 0 && type != 3 && Main.gameData.getScore() >= cost 
                && bought != true && robot.isPowerOn()){
            StoreItem temp = new StoreItem(430, 460, "robotcard2", 387, 156);
            Main.gameData.getMiscFigures().add(temp);
            new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ex) {
                       // System.out.println("Removing robotcard2 didn't work!");
                    }
                    Main.gameData.getMiscFigures().remove(temp);
                }
            }.start();
        }
        
        //player's health is full
        else if(shooter.getCollisionBox().intersects(getCollisionBox()) && numPressed[KeyEvent.VK_E] 
                && type == 3 && shooter.getCurrentHealth() >= shooter.getMaxHealth() && 
                bought != true&& robot.isPowerOn()){
            StoreItem temp = new StoreItem(430, 460, "robotcard3", 387, 156);
            Main.gameData.getMiscFigures().add(temp);
            new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException ex) {
                       // System.out.println("Removing robotcard3 didn't work!");
                    }
                    Main.gameData.getMiscFigures().remove(temp);
                }
            }.start();
        }
    }

    public void setBought(){
        if(bought != true){
            
            //subtract cost of purchase
            if(robot.isPowerOn())
                Main.gameData.setScore(-cost);
            
            bought = true;
            
            //if player stole an item, this turns the robot back on
            robot.setPowerOn(true);
            
            //change image to out of stock
            try {
                  img = ImageIO.read(getClass().getResource("/model/assets/store/OOS.png"));
                  imgWidth = 57;
                  imgHeight = 41;
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Cannot open OOS.png");
                    System.exit(-1);
                }
            checkPurchase();
        }
    }
    
    public void setType(String x){
        if (null != imgName) switch (imgName) {
            case "upgrade1":
                type = 1;
                break;
            case "upgrade2":
                type = 2;
                break;
            case "upgrade3":
                type = 3;
                break;
            case "upgrade4":
                type = 4;
                break;
            default:
                break;
        }
        cost = 10;
    }
    
    public void checkPurchase(){
        if(bought){
            if(shooter.getCurrentHealth() < shooter.getMaxHealth() && this.type == 3){
                shooter.increaseHealth(3);
            }
            //check if inventory is empty/full to pick up upgrade
            else if((GameData.getInventory().getState() == GameFigureState.INV_EMPTY || 
                    GameData.getInventory2().getState() == GameFigureState.INV_EMPTY) &&
                    this.type != 3){
                if(GameData.getInventory().getState() == GameFigureState.INV_EMPTY){
                    GameData.getInventory().setState(GameFigureState.INV_FULL);

                    // if theres an upgrade in slot 2 but not slot 1, insert into the array, dont add to end
                    // or in a rare instance where slot 2 has a timer and 1 is empty
                    if(GameData.getInventory2().getState() == GameFigureState.INV_FULL || 
                            (Main.gameData.getInventoryFigures().size() > 2 && 
                            Main.gameData.getInventoryFigures().get(2) instanceof UpgradeTimer))
                        Main.gameData.getInventoryFigures().add(2, new Upgrade(25, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190, type));
                    
                    else
                        Main.gameData.getInventoryFigures().add(new Upgrade(25, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190, type));
                }
                else if(GameData.getInventory2().getState() == GameFigureState.INV_EMPTY){
                    GameData.getInventory2().setState(GameFigureState.INV_FULL);
                    Main.gameData.getInventoryFigures().add(new Upgrade(80, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190, type));
                }
                super.state = GameFigureState.STATE_DONE;
                Upgrade u = null;
                if(Main.gameData.getInventoryFigures().get(Main.gameData.getInventoryFigures().size() - 1) instanceof Upgrade)
                    u = (Upgrade) Main.gameData.getInventoryFigures().get(Main.gameData.getInventoryFigures().size() - 1);
                if (u != null)
                    u.setState(GameFigureState.UPGRADE_STATE_DEAD);
            }
            cost = 0;
            type = 0;
        }
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox() {
        if(type == 4)
            return(new Rectangle2D.Float(super.x , super.y + 10, imgWidth, imgHeight + 10));
        else
            return(new Rectangle2D.Float(super.x , super.y, imgWidth, imgHeight));
    }
    
    public int getType(){
        return type;
    }
    
    public void setRobot(){
        for(int i = 0; i < Main.gameData.getMiscFigures().size(); i ++){
            if(Main.gameData.getMiscFigures().get(i) instanceof StoreRobot){
                robot = (StoreRobot) Main.gameData.getMiscFigures().get(i);
            }
        }
    }
    
    public void leaveStore() throws IOException{
        Main.gameData.getMiscFigures().clear();
        Main.gamePanel.setLevelBackgroundImage("backgroundImageMenu1");
        GamePanel.Level += 1;
    }
    
    
    //unused functions
    
    @Override
    public void takeDamage(double d) {}

    @Override
    public double getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getHeight() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState(int state) {}  
    
}
