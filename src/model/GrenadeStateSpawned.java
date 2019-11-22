package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class GrenadeStateSpawned implements GrenadeState {
    private static int unitTravel = 3; // per frame
    private int distance = 300;  
    private int displacement = 0; 
    
    private Image image;
    private int bombRadius = 10;
    
    
    //--------------------------------
    //Projectile
    //--------------------------------
    private int theta = 45;
    private double V = 560;    //arbitrary
    private double Vy = V*Math.sin(Math.toRadians(theta));
    private int t = 0;
    private int g = 10;        //10px/s2
    
    
    
    public GrenadeStateSpawned() {
        image = null;
        
        try {
            image = ImageIO.read(getClass().getResource("/model/assets/grenade.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open grenade.png");
            System.exit(-1);
        }
    }
    
    @Override
    public void goNext(Grenade context) {
            context.setGrenadeState(new GrenadeStateExploding());
    }

    @Override
    public void render(Grenade context, Graphics2D g) {
        g.drawImage(image, (int) context.x, (int) (context.y - 2*bombRadius),
                2*bombRadius, 2*bombRadius, null);
    }

    @Override
    public void update(Grenade context) {
        context.damage = 0;
        t++;   //increment time
        double y = Vy*t - 0.5*g*Math.pow(t, 2);
        double tFlight = 2*V*Math.sin(Math.toRadians(theta))/g;
        
//        context.y -= unitTravel;        
        context.x += context.dx;
        context.y += context.dy;

        if(t > tFlight){
            this.goNext(context);
        }

        bombRadius = (int)y/775;
        
        
    }

    @Override
    public Rectangle2D.Float getCollisionBox(Grenade context) {
        return new Rectangle2D.Float(0, 0, 0, 0);      //no collision in this state
    }
}

