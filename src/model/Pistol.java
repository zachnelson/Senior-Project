package model;


import controller.Main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import model.GameFigureState;

public class Pistol extends WeaponFigure {

    // pistol size
    private static final int SIZE = 15;
    private static final double baseDamage = 1;
    private static final double rateOfFire = 500; // in miliseconds
    
    public static Color color = new Color(0xe400ff);

    private static final int UNIT_TRAVEL_DISTANCE = 12; // per frame move

    private int size = SIZE;
    
    /**
     *
     * @param sx start x of the pistol
     * @param sy start y of the pistol
     * @param tx target x of the pistol
     * @param ty target y of the pistol
     */
    public Pistol(float sx, float sy, float tx, float ty) {
        super(sx, sy, tx, ty, baseDamage, rateOfFire);
        super.state = GameFigureState.STATE_ALIVE;

        double angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));
        dx = (float) (UNIT_TRAVEL_DISTANCE * Math.cos(angle));
        dy = (float) (UNIT_TRAVEL_DISTANCE * Math.sin(angle));
        
        if (tx > sx && ty < sy) { // target is upper-right side
            dy = -dy; // dx > 0, dx < 0
        } else if (tx < sx && ty < sy) { // target is upper-left side
            dx = -dx;
            dy = -dy;
        } else if (tx < sx && ty > sy) { // target is lower-left side
            dx = -dx;
        } else { // target is lower-right side
            // dx > 0 , dy > 0
        }
        try {
            mainImage = ImageIO.read(getClass().getResource("/model/assets/BlueBullet.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open bullet.png");
            System.exit(-1);
        }
    }

    @Override
    public void render(Graphics2D g) {
//        g.setColor(color);
//                
//        g.drawOval((int) (super.x - size / 2),
//                (int) (super.y - size / 2),
//                size, size);
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                (int)size, (int)size, null);
    }

    @Override
    public void takeDamage(double damage)
    {
        setTargetReached();
    }


    @Override
    public void update() {

        updateDamage();

        updateState();
        if (state == GameFigureState.STATE_ALIVE) {
            updateLocation();
        } else if (state == GameFigureState.STATE_DYING) {
            updateSize();
        }
    }

    
    private void updateDamage(){
        this.damage = baseDamage * damageMultiplier;
    }


    public void updateLocation() {
        
        super.x += dx;
        super.y += dy;
    }

    public void updateSize() {
        size += 1;
    }

    public void updateState() {
        if (state == GameFigureState.STATE_ALIVE) {
            if (isTargetReached()) {
                state = GameFigureState.STATE_DYING;
            }
        } else if (state == GameFigureState.STATE_DYING) {
            state = GameFigureState.STATE_DONE;
        }
        if (!isInPanel()){
            state = GameFigureState.STATE_DONE;
        }
    }
    
    private boolean isInPanel(){
        if((super.x < Main.gamePanel.getLocation().x) 
                || super.x + SIZE > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
                || super.y < Main.gamePanel.getLocation().y -150
                || super.y + SIZE > (Main.gamePanel.getLocation().y + Main.gamePanel.getSize().height))
            return false;
        else {/*System.out.println("out of panel");*/return true;}
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox() {
       return new Rectangle2D.Float (x - size / 2, y - size/2, size, size);
        
    }

    
    public double getDamage(){
        // When skill upgrades become available,
        // need to include the damage modifier in
        // this return value
        return this.damage;
    }


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
    public void setState(int state) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
