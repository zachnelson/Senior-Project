
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author Desmond
 */
public class Treasure extends GameFigure {

    private int height;
    private int width;
    private int type;
    private Shooter shooter;
    private BufferedImage image;
    private int rotationAngle;

    public Treasure(float x, float y, int z) {
        super(x, y);
        damage = 0;
        rotationAngle = 0;
        super.state = GameFigureState.TREASURE_STATE_ALIVE;
        try {
            image = ImageIO.read(getClass().getResource("/model/assets/treasure" + z + ".png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open treasure" + z + ".png");
            System.exit(-1);
        }
        this.height = 50;
        this.width = 50;
        type = z;
        shooter = (Shooter) GameData.getShooter();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(rotateImage(image, rotationAngle), (int) super.x, (int) super.y, width, height, null);
    }

    @Override
    public void takeDamage(double damage) {
    }

    @Override
    public void update() {
        rotationAngle += 5;
        rotationAngle %= 360;

        if (shooter.getCollisionBox().intersects(getCollisionBox()) && super.state == GameFigureState.TREASURE_STATE_ALIVE) {
            
                WeaponInventory weaponInventory = (WeaponInventory)Main.gameData.getWeaponInventory().get(0);
                weaponInventory.addWeapon(this.type, this.image);
                weaponInventory.setSelectedWeapon();
                //pick up treasure
                super.state = GameFigureState.STATE_DONE;
                



                // Delete treasure object
                for (int i = 0; i < Main.gameData.getMiscFigures().size(); i++) {
                    if (Main.gameData.getMiscFigures().get(i) == this) {
                        Main.gameData.getMiscFigures().remove(i);
                        break;
                    }
                }
        }
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return (new Rectangle2D.Float(super.x, super.y, width, height));
    }

    public int getType() {
        return type;
    }

    public void setState(int x) {
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

    //---------------------------------------
    //Rotate Image over n degrees gradians
    //Works best for square images
    //---------------------------------------
    public BufferedImage rotateImage(BufferedImage imag, int n) { //n rotation in gradians
        if (image.getType()==0) return imag;
        double rotationRequired = Math.toRadians(n);
        double locationX = imag.getWidth() / 2;
        double locationY = imag.getHeight() / 2;
        AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage newImage = new BufferedImage(imag.getWidth(), imag.getHeight(), imag.getType()); //20, 20 is a height and width of imag ofc
        op.filter(imag, newImage);

        //this.img = newImage;
        return (newImage);
    }
}
