package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface LaserState {
    public void goNext(Laser context);
    
    // how to render on the canvas
    public void render(Laser context, Graphics2D g);
    
    // changes per frame
    public void update(Laser context);

    
    public Rectangle2D.Float getCollisionBox(Laser context);
}
