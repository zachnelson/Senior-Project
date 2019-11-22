package model;

import controller.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import view.GamePanel;
import java.util.Date;
import java.sql.Timestamp;

public class UpgradeTimer extends GameFigure{

    private int height;
    private int width;
    private int counter;
    private int counterDisplay;
    private Font font = new Font("arial", Font.BOLD, 24);
    private long startTime;
    private Date date = new Date();
    private int slot;
    private int x;
    private int y;
    Inventory inv;
            
    public UpgradeTimer(float x, float y, int z, int slot) {
        super(x, y);
        super.state = GameFigureState.UPGRADE_STATE_ALIVE;
        this.height = 50;
        this.width = 50;
        this.counter = z;
        this.startTime = date.getTime();
        counterDisplay = counter;
        this.slot = slot;
        this.x = (int) x;
        this.y = (int) y;
    }

    @Override
    public void render(Graphics2D g) {
        String stringCounter = Integer.toString(counterDisplay);
        g.setFont(font);
        g.setColor(Color.red);
        g.drawString(stringCounter + "s", x, y);
    }
    @Override
    public void takeDamage(double damage){}

    @Override
    public void update() {
        long time = getCurrentTime();
        long diff = time - startTime;
        counterDisplay = (int) (counter - (diff / 1000));
        if(time > startTime + 20000){
            for (int i=0; i < Main.gameData.getInventoryFigures().size(); i++){
                if (Main.gameData.getInventoryFigures().get(i) == this){
                    Main.gameData.getInventoryFigures().remove(i);
                    if(slot == 0)
                        GameData.getInventory().setState(GameFigureState.INV_EMPTY);
                    else if (slot == 1) 
                        GameData.getInventory2().setState(GameFigureState.INV_EMPTY);
                    break;
                }
            }
        }
    }
    
    public long getCurrentTime(){
        Date date = new Date();
        long time = date.getTime();
        return time;
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox() {
        return (new Rectangle2D.Float(super.x , super.y, width, height));
    }
    
    public void setState(int x){
        super.state = x;
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
    public int getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}


