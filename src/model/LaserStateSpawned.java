package model;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class LaserStateSpawned implements LaserState{

    private final int ANIM_PER_FRAME = 1;
    
    private int oval_size;
    private int frameCounter;
    private int duration;
    

    
    public LaserStateSpawned(){
        
        oval_size = 10;
        duration = 0;
        frameCounter = 0;

    }
    
    @Override
    public void goNext(Laser context) {
 

        context.setLaserState(new LaserStateBurning(context));

        
    }

    @Override
    public void render(Laser context, Graphics2D g) {
        g.setColor(Color.blue);
        g.fillOval((int)(context.x +  GameData.getShooter().getCollisionBox().width/2 - (oval_size/3)), 
                   (int)context.y - (oval_size/2), oval_size, oval_size);

    }

    @Override
    public void update(Laser context) {
        
        if(frameCounter == ANIM_PER_FRAME && duration >= 0 && duration < 5){
            oval_size += 3;
            duration++;
            frameCounter = 0;
        }else if(frameCounter == ANIM_PER_FRAME && duration >= 5 && duration < 10){
            oval_size -= 3;
            duration++;
            frameCounter = 0;
        }else if(duration == 10){
            this.goNext(context);
        }else{
            frameCounter++;
        }
        
       
        context.x = GameData.getShooter().x;
        context.y = GameData.getShooter().y;
    }

    @Override
    public Rectangle2D.Float getCollisionBox(Laser context) {
        return new Rectangle2D.Float(0, 0, 0, 0);     //dummie collition box(Only kill enemies when burning)
    }
}
