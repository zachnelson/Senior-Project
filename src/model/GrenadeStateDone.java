package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GrenadeStateDone implements GrenadeState{

    @Override
    public void goNext(Grenade context) {
        
        context.setGrenadeState(null);
    }

    @Override
    public void render(Grenade context, Graphics2D g) {
        //No render needed
    }

    @Override
    public void update(Grenade context) {
        //No update needed
    }

    @Override
    public Rectangle2D.Float getCollisionBox(Grenade context) {
        return new Rectangle2D.Float(0, 0, 0, 0);//No need for collision box
    }
}
//comment a

