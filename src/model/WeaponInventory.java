package model;


import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class WeaponInventory extends GameFigure{

    private int width = 50;
    private int height = 50;  
    private int highlightedWeapon = 1;
    private boolean slot1 = false;
    private boolean slot2 = false;
    private boolean slot3 = false;
    private boolean slot4 = false;
    
    private int pEmptySlot = 1;       //Initialize the empty slot pointer to first cell
    
    private BufferedImage slot1Image = null;
    private BufferedImage slot2Image = null;
    private BufferedImage slot3Image = null;
    private BufferedImage slot4Image = null;
    
    private int slot1Type = 0;
    private int slot2Type = 0;
    private int slot3Type = 0;
    private int slot4Type = 0;
    
    public boolean[] numPressed = new boolean[255];
    
    private static final Map<Integer, GameFigure> weapons = initMap();

    private static Map<Integer, GameFigure> initMap() {
        Map<Integer, GameFigure> map = new HashMap<>();
        map.put(1, new Shotgun(0,0,0,0));
        map.put(2, new Grenade(0,0,0,0));
        map.put(3, new SpaceShield(0,0,0,0));
        map.put(4, new Rifle(0,0,0,0));
        map.put(5, new Laser(0,0,0,0));
        return Collections.unmodifiableMap(map);
    }
    
    public WeaponInventory(float x, float y) {
        super(x, y);

        try {
                super.mainImage = ImageIO.read(getClass().getResource("/model/assets/inventory-slot.png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open inventory-slot.png");
                System.exit(-1);
            }
        
    }

    @Override
    public void render(Graphics2D g) {

        //--------------------------------------
        //draw inventory slots
        //--------------------------------------
        g.drawImage(mainImage, (int)super.x, (int)super.y, width, height, null);
        g.drawImage(mainImage, (int)super.x + width + 5, (int)super.y, width, height, null);
        g.drawImage(mainImage, (int)super.x, (int)super.y + height+5, width, height, null);
        g.drawImage(mainImage, (int)super.x + width + 5, (int)super.y + height+5, width, height, null); 
        
        //--------------------------------------
        //draw slot images
        //--------------------------------------        

        if(slot1){
             g.drawImage(slot1Image, (int)super.x, (int)super.y, width, height, null);
        }
        if(slot2){
             g.drawImage(slot2Image, (int)super.x + width + 5, (int)super.y, width, height, null);
        }
        if(slot3){
             g.drawImage(slot3Image, (int)super.x, (int)super.y + height+5, width, height, null);
        }
        if(slot4){
             g.drawImage(slot4Image, (int)super.x + width + 5, (int)super.y + height+5, width, height, null);
        }
        
        //--------------------------------------
        //draw highlighted slots
        //--------------------------------------       
        if(this.highlightedWeapon == 1){
          g.drawRect((int)super.x - 2, (int)super.y-2, width+2, height+2);   
        }else if(this.highlightedWeapon == 2){
            g.drawRect((int)super.x + width + 5 - 2, (int)super.y-2, width+2, height+2); 
        }else if(this.highlightedWeapon == 3){
          g.drawRect((int)super.x - 2, (int)super.y + height+5 -2, width+2, height+2);   
        }else{
            g.drawRect((int)super.x + width + 5 - 2, (int)super.y + height+5 -2, width+2, height+2); 
        } 
        
        
            
    }

    @Override
    public void update() {
        
    }
    


    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
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
    
    
    public void setHighlightedWeapon(int cellNumber){
        if(cellNumber <= 0 || cellNumber > 4){
            throw new UnsupportedOperationException("Not parameter value not allowed");
        }else{
            this.highlightedWeapon = cellNumber;
        }
    }
    
    public void addWeapon(int type, BufferedImage img){
        
        switch(pEmptySlot){
            case 1:
                    this.slot1 = true;
                    this.slot1Type = type;
                    this.slot1Image = img;
                    break;
            case 2:
                    this.slot2 = true;
                    this.slot2Type = type;
                    this.slot2Image = img;
                    break;                
            case 3:
                    this.slot3 = true;
                    this.slot3Type = type;
                    this.slot3Image = img;
                    break;
            case 4:
                    this.slot4 = true;
                    this.slot4Type = type;
                    this.slot4Image = img;
                    break;
            default:
                    break;                         
        }
        
        //--------------------------------
        //increment point to the next slot
        //and cycle to first when full
        //--------------------------------
        pEmptySlot %= 4;
        pEmptySlot += 1;
       
    }
    
    public void clearSlotImage(int slot){
                switch(slot){
            case 1:
                    this.slot1 = false;
                    this.slot1Type = 0;
                    break;
            case 2:
                    this.slot2 = false;
                    this.slot2Type = 0;
                    break;                
            case 3:
                    this.slot3 = false;
                    this.slot3Type = 0;
                    break;
            case 4:
                    this.slot4 = false;
                    this.slot4Type = 0;
                    break;
            default:
                    break;                         
        }
    }
    
    public boolean getSelectedSlotState(){
        switch(this.highlightedWeapon){
            case 1:
                    return this.slot1;
                    
            case 2:
                    return this.slot2;
                    
            case 3:
                    return this.slot3;
                    
            case 4:
                    return this.slot4;
                     
            default:
                    return false;
                    
        }
    }
    
    public void setSelectedWeapon(){
        //-------------------------------
        //Set the appropriate main weapon
        //-------------------------------
        //Kind of brut force method
        //-------------------------------
        switch(this.highlightedWeapon){
            case 1:
                if(this.slot1){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot1Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
            case 2:
                if(this.slot2){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot2Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
            case 3:
                if(this.slot3){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot3Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
            case 4:
                if(this.slot4){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot4Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
        }
        Main.game.setWeaponLabels();
    }
    
    public void selectNextWeapon(){
        this.highlightedWeapon %= 4;
        this.highlightedWeapon += 1;
        
        //-------------------------------
        //Set the appropriate main weapon
        //-------------------------------
        //Kind of brut force method
        //-------------------------------
        switch(this.highlightedWeapon){
            case 1:
                if(this.slot1){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot1Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
            case 2:
                if(this.slot2){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot2Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
            case 3:
                if(this.slot3){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot3Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
            case 4:
                if(this.slot4){
                    Main.gameData.setSecondaryWeapon(weapons.get(this.slot4Type));
                }else{
                    Main.gameData.setSecondaryWeapon(null);
                }
                break;
        }
        Main.game.setWeaponLabels();
    }
  
    
    
}


