
package model;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public interface GrenadeState {
    public void goNext(Grenade context);
    
    // how to render on the canvas
    public void render(Grenade context, Graphics2D g);
    
    // changes per frame
    public void update(Grenade context);
    
    public Rectangle2D.Float getCollisionBox(Grenade context);
}
