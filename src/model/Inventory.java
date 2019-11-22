
package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.Arrays;

import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import view.MainWindow;

public class Inventory extends GameFigure{

    private final int height;
    private final int width;
    public boolean[] numPressed = new boolean[255];

    private Shooter shooter;
    private WeaponFigure mainWeapon;
    private WeaponFigure secondaryWeapon;

    Inventory inv1 = null;
    Inventory inv2 = null;
    
    public Inventory(float x, float y) {
        super(x, y);
        super.state = GameFigureState.INV_EMPTY; // empty
        shooter = (Shooter) GameData.getShooter();
        mainWeapon = (WeaponFigure) GameData.getMainWeapon();
        secondaryWeapon = (WeaponFigure) GameData.getSecondaryWeapon();
        

        try {
                super.mainImage = ImageIO.read(getClass().getResource("/model/assets/inventory-slot.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open inventory-slot.png");
                System.exit(-1);
            }
        this.height = 50;
        this.width = 50;
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int)super.x, (int)super.y, width, height, null);
    }

    @Override
    public void update() {
        if(inv1 == null && GameData.getInventory() != null)
            inv1 = (Inventory) GameData.getInventory();
        if(inv2 == null && GameData.getInventory2() != null)
            inv2 = (Inventory) GameData.getInventory2();
        if(secondaryWeapon == null)
            secondaryWeapon = (WeaponFigure) GameData.getSecondaryWeapon();
        //printInvContents();
        removeUpgrade();
    }
    
    public void removeUpgrade() {
        //Slot 1
        if (this == Main.gameData.getInventoryFigures().get(0) && 
                inv1.getState() == GameFigureState.INV_FULL && numPressed[KeyEvent.VK_1]){
            if(Main.gameData.getInventoryFigures().get(2) instanceof Upgrade){
                useUpgrade((Upgrade) Main.gameData.getInventoryFigures().get(2));

                Main.gameData.getInventoryFigures().add(2, new UpgradeTimer(30, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190, 20, 0));

                Main.gameData.getInventoryFigures().remove(3);
            }
        }
        //Slot 2
        else if (this == Main.gameData.getInventoryFigures().get(1) && 
                inv2.getState() == GameFigureState.INV_FULL && numPressed[KeyEvent.VK_2]){
            if (Main.gameData.getInventoryFigures().size() > 3) {
                if(Main.gameData.getInventoryFigures().get(3) instanceof Upgrade){
                    useUpgrade((Upgrade) Main.gameData.getInventoryFigures().get(3));

                    Main.gameData.getInventoryFigures().add(3, new UpgradeTimer(85, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190, 20, 1));

                    Main.gameData.getInventoryFigures().remove(4);
                }
            }
            else{
                if(Main.gameData.getInventoryFigures().get(2) instanceof Upgrade){
                    useUpgrade((Upgrade) Main.gameData.getInventoryFigures().get(2));

                    Main.gameData.getInventoryFigures().add(2, new UpgradeTimer(85, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190, 20, 1));

                    Main.gameData.getInventoryFigures().remove(3);
                }
            }
        }
    }
    
    private void useUpgrade(Upgrade upgrade){
        switch (upgrade.getType()) {
            case 1:
                shooter.setSpeedBuff(5);
                break;
            case 2:
                mainWeapon.setDamageMultiplier(3);
                if(secondaryWeapon != null)
                    secondaryWeapon.setDamageMultiplier(3);
                break;
            case 4:
                //get all enemies on screen and paint lightningbolts
                for(int i = 0; i < Main.gameData.getEnemyFigures().size(); i ++){
                    EnemyFigure ef;
                    ef = (EnemyFigure) Main.gameData.getEnemyFigures().get(i);
                    ef.setSpeedBuff(0.3);
                    Main.gameData.getMiscFigures().add(new LightningBoltAnimation (ef.getX(), ef.getY(), ef));
                }
                for(int i = 0; i < Main.gameData.getMiscFigures().size(); i ++){
                    StoreRobot robot;
                    if(Main.gameData.getMiscFigures().get(i) instanceof StoreRobot){
                        robot = (StoreRobot) Main.gameData.getMiscFigures().get(i);
                        Main.gameData.getMiscFigures().add(new LightningBoltAnimation (robot.getX(), robot.getY(), robot));
                    }
                }
                break;
            default:
                break;
        }
        removeUpgradeEnhancement(upgrade.getType());
    }
    
    private void removeUpgradeEnhancement(int x){
        //Speed is boosted for 20 seconds before returning to default
        if (x == 1){   
            new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException ex) {
                        System.out.println("Removing upgrade enhancement 1 didn't work!");
                    }
                    shooter.setSpeedBuff(1);
                }
            }.start();
        }
        //Damage is boosted for 20 seconds before returning to default
        else if (x == 2){
            new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException ex) {
                        System.out.println("Removing upgrade enhancement 2 didn't work!");
                    }
                    mainWeapon.setDamageMultiplier(1);
                    if(secondaryWeapon != null)
                        secondaryWeapon.setDamageMultiplier(1);
                }
            }.start();
        }
        //Enemies are slowed down for 20 seconds before returning to default
        else if (x == 4){
            new Thread(){
                @Override
                public void run(){
                    try {
                        TimeUnit.SECONDS.sleep(20);
                    } catch (InterruptedException ex) {
                        System.out.println("Removing upgrade enhancement 4 didn't work!");
                    }
                    for(int i = 0; i < Main.gameData.getEnemyFigures().size(); i ++){
                        EnemyFigure ef;
                        ef = (EnemyFigure) Main.gameData.getEnemyFigures().get(i);
                        ef.setSpeedBuff(1);
                    }
                }
            }.start();
        }
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    } 
    
    public void printInvContents(){
        Inventory inv1 = (Inventory) GameData.getInventory();
        Inventory inv2 = (Inventory) GameData.getInventory2();
        System.out.println(Arrays.toString(Main.gameData.getInventoryFigures().toArray()));
        
        String invs1 = "";
        String invs2 = "";
        
        if(inv1.getState() == 300)
            invs1 = "empty";
        if(inv1.getState() == 301)
            invs1 = "full";
        if(inv1.getState() == 305)
            invs1 = "used";
        
        if(inv2.getState() == 300)
            invs2 = "empty";
        if(inv2.getState() == 301)
            invs2 = "full";
        if(inv2.getState() == 305)
            invs2 = "used";
        
        GameFigure up1 = null;
        GameFigure up2 = null;
        
        if(Main.gameData.getInventoryFigures().size() > 2)
            up1 = Main.gameData.getInventoryFigures().get(2);
        if(Main.gameData.getInventoryFigures().size() > 3)
            up2 =  Main.gameData.getInventoryFigures().get(3);
                
       // System.out.println("inv1: " + invs1 + " " + up1);
       // System.out.println("inv2: " + invs2 + " " + up2);
    }
    
    @Override
    public void takeDamage(double damage){}
    
    @Override
    public Rectangle2D.Float getCollisionBox() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int getState(){
        return super.state;
    }
    
    @Override
    public void setState(int x){
        super.state = x;
    }
}

