package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class LaserStateDone implements LaserState{

    @Override
    public void goNext(Laser context) {
        
        context.setLaserState(null);
    }

    @Override
    public void render(Laser context, Graphics2D g) {
        //No render needed
    }

    @Override
    public void update(Laser context) {
        //No update needed
    }

    @Override
    public Rectangle2D.Float getCollisionBox(Laser context) {
        return new Rectangle2D.Float(0, 0, 0, 0);//No need for collision box
    }
}

