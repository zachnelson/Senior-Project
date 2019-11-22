package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class GrenadeStateExploding implements GrenadeState {
    
    private Image image;
    private int size = 90;
    
    public GrenadeStateExploding() {
        image = null;
        
        try {
            image = ImageIO.read(getClass().getResource("/model/assets/grenadeExplosion.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open Grenade Explosion.png");
            System.exit(-1);
        }
    }
    
    @Override
    public void goNext(Grenade context) {
        context.setGrenadeState(new GrenadeStateDone());
    }

    @Override
    public void render(Grenade context, Graphics2D g) {
        g.drawImage(image, (int) context.x - size/2, (int) context.y - size, size, size, null);
    }

    @Override
    public void update(Grenade context) {
        if (context.damage == 0){
            context.damage = 3 * WeaponFigure.damageMultiplier;
        }
        if(size < 120) {
            size += 1;
        }
        else 
            goNext(context);
    }

    @Override
    public Rectangle2D.Float getCollisionBox(Grenade context) {
        return new Rectangle2D.Float(context.x, context.y, size, size);//Dummy collision box
    }
}

