package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import view.MainWindow;

public class Upgrade extends GameFigure{

    private int height;
    private int width;
    private int type;
    private Shooter shooter;
    
    public Upgrade(float x, float y, int z) {
        super(x, y);
        damage = 0;
        super.state = GameFigureState.UPGRADE_STATE_ALIVE;
        try {
                super.mainImage = ImageIO.read(getClass().getResource("/model/assets/upgrade"+z+".png"));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error: Cannot open upgrade"+z+".png");
                System.exit(-1);
            }
        this.height = 50;
        this.width = 50;
        type = z;
        shooter = (Shooter) GameData.getShooter();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(mainImage, (int)super.x, (int)super.y, width, height, null);
    }
    @Override
    public void takeDamage(double damage){}

    @Override
    public void update() {      
        
        if(shooter.getCollisionBox().intersects(getCollisionBox()) && super.state == GameFigureState.UPGRADE_STATE_ALIVE){
            
            //picked up health
            if(shooter.getCurrentHealth() < shooter.getMaxHealth() && this.type == 3){
                
                //increase health
                shooter.increaseHealth(3);
                super.state = GameFigureState.STATE_DONE;
                
                // Delete health upgrade object
                for (int i=0; i < Main.gameData.getMiscFigures().size(); i++){
                    if (Main.gameData.getMiscFigures().get(i) == this){
                        Main.gameData.getMiscFigures().remove(i);
                        break;
                    }
                }
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
                Main.gameData.getMiscFigures().remove(this);
                super.state = GameFigureState.STATE_DONE;
                Upgrade u = null;
                if(Main.gameData.getInventoryFigures().get(Main.gameData.getInventoryFigures().size() - 1) instanceof Upgrade)
                    u = (Upgrade) Main.gameData.getInventoryFigures().get(Main.gameData.getInventoryFigures().size() - 1);
                if (u != null)
                    u.setState(GameFigureState.UPGRADE_STATE_DEAD);
            }
        }      
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox() {
         return(new Rectangle2D.Float(super.x , super.y, width, height));
    }

    public int getType() {
        return type;
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

