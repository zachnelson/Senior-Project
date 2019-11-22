package model;

import controller.Main;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import view.GamePanel;

public class LaserStateBurning implements LaserState {

    private int size = 5;
    private final int MAX_EXPLOSION_SIZE = 40;
    private final int UNIT_TRAVEL_DISTANCE = 10;
    private final int WIDTH = 10;
    private final int LENGTH = 200;

    private BufferedImage image;

    private float dx; // displacement at each frame
    private float dy; // displacement at each frame
    private float ty;
    private float sy;
    private float tx;
    private float sx;

    public LaserStateBurning(Laser context) {
        sx = context.x;
        sy = context.y;
        tx = context.getTarget().x;
        ty = context.getTarget().y;
        
        // Readjust the sx and sy variables based on where image needs
        // to be drawn
        setStartingPosition(context, tx, ty);
        
        double angle = Math.atan2(ty - sy, tx - sx);
        
        dx = (float) (UNIT_TRAVEL_DISTANCE * Math.cos(angle));
        dy = (float) (UNIT_TRAVEL_DISTANCE * Math.sin(angle));

        angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));
        double angle_deg = Math.toDegrees(angle);
        if (tx > sx && ty < sy) { // Upper Right
            angle_deg = Math.abs(angle_deg - 90);
        } else if (tx < sx && ty < sy){ // Upper Left
            angle_deg += 270;
        } else if (tx < sx && ty > sy){ // Lower Left
            angle_deg = Math.abs(angle_deg - 90) + 180;
        } else {    // Lower Right
            angle_deg += 90;
        }


        
        String path = "/model/assets/laser.png";
        
            

        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open laser.png");
            System.exit(-1);
        }

        //--------------------------------------------------
        //Rotate image if successfully loaded
        //--------------------------------------------------
        //
        
        image = rotateImage(image,  (int)angle_deg);
//        setStartingPosition(context, tx, ty);        
    }
    
    public void setStartingPosition(Laser context, float tx, float ty){        
        double angle = Math.atan2(Math.abs(ty - sy), Math.abs(tx - sx));
        
        float rotateX, rotateY;
        // 100 = laser size width & height divided by 2
        rotateX = (float)100 * (float)Math.cos(angle);
        rotateY= (float)100 * (float)Math.sin(angle);
        
        if (tx > sx && ty < sy) { // Upper Right
            rotateX = 0;
            rotateY = -rotateY;
        } else if (tx < sx && ty < sy){ // Upper Left
            rotateX = -rotateX;
            rotateY = -rotateY;
        } else if (tx < sx && ty > sy){ // Lower Left
            rotateX -= 2 * rotateX;
            rotateY = 0;
        } else {    // Lower Right

        }
        
        sx += rotateX;
        sy += rotateY;
    }

    @Override
    public void goNext(Laser context) {
        context.setLaserState(new LaserStateDone());
    }

    @Override
    public void render(Laser context, Graphics2D g) {
//        g.drawImage(image, (int) (context.x),
//                (int) (context.y),
//                 null);
        g.drawImage(image, (int) (sx),
                (int) (sy),
                 null);
    }

    @Override
    public void update(Laser context) {
//        context.x += dx;
//        context.y += dy;
        sx += dx;
        sy += dy;
        context.x = sx;
        context.y = sy;
        
        if (!isInPanel(context)) {
            this.goNext(context);
        }
    }

    private boolean isInPanel(Laser context) {
        if ((context.x < Main.gamePanel.getLocation().x)
                //                || context.x + LENGTH > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
                || context.x > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
                || context.y < Main.gamePanel.getLocation().y - 150
                || context.y + WIDTH > (Main.gamePanel.getLocation().y + Main.gamePanel.getSize().height)) {
            return false;
        } else {/*System.out.println("out of panel");*/
            return true;
        }
    }

    @Override
    public Rectangle2D.Float getCollisionBox(Laser context) {
//        return new Rectangle2D.Float(context.x + GameData.getShooter().getCollisionBox().width / 2,
//                (int) (context.y - LENGTH), WIDTH, LENGTH);
        return new Rectangle2D.Float(sx, sy, image.getWidth(), image.getHeight());

    }
    
    
            //---------------------------------------
    //Rotate Image over n degrees gradians
    //Works best for square images
    //---------------------------------------
    public BufferedImage rotateImage(BufferedImage imag, double n) { //n rotation in gradians
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
    
    public BufferedImage getImage(){
        return image;
    }
}

