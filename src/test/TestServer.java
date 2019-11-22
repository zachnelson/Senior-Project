package test;

import controller.Main;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Barrel;
import model.BigHandsBoss;
import model.ChargerEnemy;
import model.DiggerEnemy;
import model.EnemyFigure;
import model.FlyingEnemy;
import model.FreezerEnemy;
import model.LaserEnemy;
import model.BouncerEnemy;
import model.SpikerEnemy;

import model.Grenade;

import model.Laser;
import model.Pistol;
import model.Rifle;
import model.Shooter;
import model.Shotgun;
import model.SpaceShield;
import model.Store;
import model.Upgrade;
import model.WeaponFigure;
import model.WeaponInventory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jiwer
 */
public class TestServer extends JFrame{
    
    private JComboBox mainWeaponChoices;
    private JButton mainWeaponAdd;
    
    private JComboBox secondayWeaponChoices;
    private JButton secondayWeaponAdd;
    
    private JComboBox enemyChoices;
    private JButton enemyAdd;
    
    private JComboBox upgradeChoices;
    private JButton upgradeAdd;
    
    private JComboBox locationChoices;
    private JButton locationAdd;
    
    private String mainLabel, secondaryLabel;
    
    public TestServer(){
        super();
        createComponents();
        layoutComponents();
        this.setTitle("Test Server");
    }
    
    private void createComponents(){
        mainWeaponChoices = new JComboBox(getMainWeapons());
        mainWeaponAdd = new JButton("Set Main Weapon");
        mainWeaponAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMainWeapon((Class) mainWeaponChoices.getSelectedItem());
            }
        });
        
        secondayWeaponChoices = new JComboBox(getSecondaryWeapons());
        secondayWeaponAdd = new JButton("Set Secondary Weapon");
        secondayWeaponAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSecondaryWeapon((Class) secondayWeaponChoices.getSelectedItem());
            }
        });
        
        enemyChoices = new JComboBox(getEnemyList());
        enemyAdd = new JButton("Add Enemy");
        enemyAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEnemy((Class)enemyChoices.getSelectedItem());
            }
        });
        
        upgradeChoices = new JComboBox(getUpgradeList());
        upgradeAdd = new JButton("Add Upgrade");
        upgradeAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUpgrade((String)upgradeChoices.getSelectedItem());
            }
        });
        
        locationChoices = new JComboBox(getLocationList());
        locationAdd = new JButton("Change Location");
        locationAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    setLocation((String)locationChoices.getSelectedItem());
                } catch (IOException ex) {
                    Logger.getLogger(TestServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }
    
    private void layoutComponents(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        
        gbc.gridwidth = 1;        
        panel.add(enemyChoices, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(enemyAdd, gbc);
        
        gbc.gridwidth = 1;        
        panel.add(mainWeaponChoices, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(mainWeaponAdd, gbc);
        
        gbc.gridwidth = 1;        
        panel.add(secondayWeaponChoices, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(secondayWeaponAdd, gbc);
        
        gbc.gridwidth = 1;        
        panel.add(upgradeChoices, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(upgradeAdd, gbc);
        
        gbc.gridwidth = 1;        
        panel.add(locationChoices, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(locationAdd, gbc);
        
        Container c = getContentPane();
        c.add(panel, "Center");
    }
    
    private Class[] getMainWeapons(){
        Class[] mainWeapons = new Class[] {
            Pistol.class
        };
        
        return mainWeapons;
    }
    
    private Class[] getSecondaryWeapons(){
        Class[] secondaryWeapons = new Class[] {
            Grenade.class,
            Laser.class,
            Rifle.class,
            Shotgun.class, 
            SpaceShield.class
        };
        
        return secondaryWeapons;
    }
    
    private Class[] getEnemyList(){
        Class[] enemyList = new Class[] {
            DiggerEnemy.class, 
            FlyingEnemy.class,
            ChargerEnemy.class,
            FreezerEnemy.class,
            BigHandsBoss.class,
            Barrel.class,
            LaserEnemy.class,
            BouncerEnemy.class, 
            SpikerEnemy.class
        };
        
        return enemyList;
    }
    
    private String[] getUpgradeList(){
        return new String[]{
            "1",
            "2",
            "3",
            "4"
        };
    }
    
    private String[] getLocationList(){
        return new String[]{
            "store",
            "level1"
        };
    }
    
    private void setMainWeapon(Class mainWeapon){       
        for (Constructor<?> ctor : mainWeapon.getConstructors()){
            Class<?>[] paramTypes = ctor.getParameterTypes();
            if (paramTypes.length == 4){
                Object[] args = new Object[paramTypes.length];
                args[0]= 0;
                args[1]= 0;
                args[2]= 0;
                args[3]= 0;
                try{
                    WeaponFigure weapon = (WeaponFigure) ctor.newInstance(args);
                    Main.gameData.setMainWeapon(weapon);
                } catch (Exception e){
                    // Could not create the weapon
                    System.out.println("Unable to create weapon for: " 
                            + mainWeapon.getName());
                }
            }
        }
        
        Main.game.setWeaponLabels();
    }
    
    private void setSecondaryWeapon(Class mainWeapon){
//        for (Constructor<?> ctor : mainWeapon.getConstructors()){
//            Class<?>[] paramTypes = ctor.getParameterTypes();
//            if (paramTypes.length == 4){
//                Object[] args = new Object[paramTypes.length];
//                args[0]= 0;
//                args[1]= 0;
//                args[2]= 0;
//                args[3]= 0;
//                try{
//                    WeaponFigure weapon = (WeaponFigure) ctor.newInstance(args);
//                    Main.gameData.setSecondaryWeapon(weapon);
//                } catch (Exception e){
//                    // Could not create the weapon
//                    System.out.println("Unable to create weapon for: " 
//                            + mainWeapon.getName());
//                }
//            }
//        }
        int type = -1;
        BufferedImage image = null;
        if (mainWeapon == Grenade.class){
            type = 2;
        } else if (mainWeapon == Laser.class){
            type = 5;
        } else if (mainWeapon == Rifle.class){
            type = 4;
        } else if (mainWeapon == Shotgun.class){
            type = 1;
        } else if (mainWeapon == SpaceShield.class){
            type = 3;
        }
        
        try {
            image = ImageIO.read(getClass().getResource("/model/assets/treasure" + type + ".png"));
        } catch (Exception e){
            
        }
        if (image != null){
            WeaponInventory weaponInventory = (WeaponInventory)Main.gameData.getWeaponInventory().get(0);
            weaponInventory.addWeapon(type, image);
            weaponInventory.setSelectedWeapon();
            Main.game.setWeaponLabels();
        }
    }
    
    private void addEnemy(Class enemy){
        int xPos = (int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100)));
        int yPos = (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100)));
        
        for (Constructor<?> ctor : enemy.getConstructors()){
            Class<?>[] paramTypes = ctor.getParameterTypes();
            if (paramTypes.length == 2){
                Object[] args = new Object[paramTypes.length];
                args[0]= xPos;
                args[1]= yPos;
                try{
                    EnemyFigure temp = (EnemyFigure) ctor.newInstance(args);
                    Main.gameData.getEnemyFigures().add(temp);
                } catch (Exception e){
                    // Could not create the weapon
                    System.out.println("Unable to create instance for: " 
                            + enemy.getName());
                }
            }
        }
    }
    
    private void addUpgrade(String upgradeNum){
        Random rand = new Random();
        Main.gameData.getMiscFigures().add(
            new Upgrade(rand.nextInt(1100) + 1, rand.nextInt(650) + 1,
                Integer.parseInt(upgradeNum))
        );
    }
    
    private void setLocation(String location) throws IOException{
        if("store".equals(location)){
            Main.gameData.clearOutRoom();
            Store store = new Store();
            store.init();
            Shooter shooter = null;
            shooter = (Shooter) Main.gameData.getFriendFigures().get(0);
            shooter.setX(550);
            shooter.setY(550);
        }
        if("level1".equals(location)){
            Main.gameData.clearGame();
            Main.gameData.initGame();
            Main.gamePanel.setLevelBackgroundImage("backgroundImageMenu1");
        }
    }
}
