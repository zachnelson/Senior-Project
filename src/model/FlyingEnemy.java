package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import static view.GamePanel.explosion;
import static view.GamePanel.isSoundOn;
import static view.GamePanel.playMusic;

enum DIR {
    DIR_NORTH_EAST, DIR_NORTH_WEST, DIR_SOUTH_EAST, DIR_SOUTH_WEST;
}

public class FlyingEnemy extends EnemyFigure {

    private int height;
    private int width;
    private double velocityVert = 0;
    private double velocityHoriz = 0;
    public boolean[] directions = new boolean[255];
    private double friction = 0.8;
    //private int direction = 1;
    private final int FLYING_UNIT_TRAVEL = 5; // per frame for FlyingEnemy
    private final int WIDTH = 25;
    private final int HEIGHT = 25;
    private double speedBuff = 1;
    public final List<BufferedImage> explodeImages;
    private int frameCount; 

    DIR direction = DIR.DIR_NORTH_EAST;

    public FlyingEnemy(int x, int y) {
        super(x, y);
        super.health = 5;
        super.damage = 1;
        super.pointValue = 1;
        super.state = GameFigureState.FLYING_ENEMY_STATE_MOVING;
        try {
            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
            System.exit(-1);
        }
        frameCount = 0;
        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
        //System.out.println("Flying Enemy Reporting for Duty.");
        explodeImages = new CopyOnWriteArrayList<>(); 
    }

    @Override
    public void render(Graphics2D g) {

        g.drawImage(mainImage, (int) super.x, (int) super.y, width, height, null);
    }

    @Override
    public void takeDamage(double damage) {
        //damage = 1;
//        System.out.println("Damage: " + damage);
        health -= damage;
//         System.out.println(health + damage);

        if (super.health <= 0) {
            frameCount = 0;
            state = GameFigureState.FLYING_ENEMY_STATE_DEAD;
            if (!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
                super.enemyFactory.removeEnemyCharater();
            }
        }
    }

    @Override
    public void update() {
        if (super.state == GameFigureState.FLYING_ENEMY_STATE_DEAD) {

            if (frameCount == 0)
            {
                loadImages();
                width = 80;
                height = 80;
            }
            if (frameCount < 48)
            { 
                if (explodeImages.size() == 0) loadImages();
                mainImage = explodeImages.get(frameCount / 3);
            }
            else
            {
                super.state = GameFigureState.STATE_DONE;
            }
            frameCount++;
        }

        translate();
    }

    public void translate() {
        if (super.state == GameFigureState.FLYING_ENEMY_STATE_MOVING) {

            if (direction == DIR.DIR_NORTH_WEST) {
                // moving to the NORTH WEST
                super.x -= (FLYING_UNIT_TRAVEL * speedBuff);
                super.y -= (FLYING_UNIT_TRAVEL * speedBuff);

                if (super.x < 0) {
                    if (super.y < 0) {
                        direction = DIR.DIR_SOUTH_EAST;
                    } else {
                        if ((int) (Math.random() * 2) == 0) {
                            direction = DIR.DIR_SOUTH_EAST;
                        } else {
                            direction = DIR.DIR_NORTH_EAST;
                        }
                    }
                    try {
                        super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemyRight.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                        System.exit(-1);
                    }
                } else if (super.y < 0) {
                    direction = DIR.values()[(int) (Math.random() * 2) + 2];
                    if (direction == DIR.DIR_SOUTH_EAST) {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemyRight.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    } else {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    }
                }

            } else if (direction == DIR.DIR_NORTH_EAST) {
                // moving to the NORTH EAST
                super.x += (FLYING_UNIT_TRAVEL * speedBuff);
                super.y -= (FLYING_UNIT_TRAVEL * speedBuff);

                if (super.x + width > Main.WIN_WIDTH - 0) {
                    if (super.y < 0) {
                        direction = DIR.DIR_SOUTH_WEST;
                    } else {
                        if ((int) (Math.random() * 2) == 0) {
                            direction = DIR.DIR_SOUTH_WEST;
                        } else {
                            direction = DIR.DIR_NORTH_WEST;
                        }
                    }
                    try {
                        super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                        System.exit(-1);
                    }
                } else if (super.y < 0) {
                    direction = DIR.values()[(int) (Math.random() * 2) + 2];
                    if (direction == DIR.DIR_SOUTH_EAST) {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemyRight.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    } else {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    }
                }
            } else if (direction == DIR.DIR_SOUTH_EAST) {
                // moving to the SOUTH EAST
                super.x += (FLYING_UNIT_TRAVEL * speedBuff);
                super.y += (FLYING_UNIT_TRAVEL * speedBuff);

                if (super.x + width > Main.WIN_WIDTH - 0) {
                    if (super.y + height > Main.WIN_HEIGHT - 55) {
                        direction = DIR.DIR_NORTH_WEST;
                    } else {
                        if ((int) (Math.random() * 2) == 0) {
                            direction = DIR.DIR_NORTH_WEST;
                        } else {
                            direction = DIR.DIR_SOUTH_WEST;
                        }
                    }
                    try {
                        super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                        System.exit(-1);
                    }
                } else if (super.y + height > Main.WIN_HEIGHT - 55) {
                    direction = DIR.values()[(int) (Math.random() * 2)];
                    if (direction == DIR.DIR_NORTH_EAST) {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemyRight.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    } else {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    }
                }
            } else if (direction == DIR.DIR_SOUTH_WEST) {
                // moving to the SOUTH WEST
                super.x -= (FLYING_UNIT_TRAVEL * speedBuff);
                super.y += (FLYING_UNIT_TRAVEL * speedBuff);

                if (super.x < 0) {
                    if (super.y + height > Main.WIN_HEIGHT - 55) {
                        direction = DIR.DIR_NORTH_EAST;
                    } else {
                        if ((int) (Math.random() * 2) == 0) {
                            direction = DIR.DIR_NORTH_EAST;
                        } else {
                            direction = DIR.DIR_SOUTH_EAST;
                        }
                    }
                    try {
                        super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemyRight.png"));
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                        System.exit(-1);
                    }
                } else if (super.y + height > Main.WIN_HEIGHT - 50) {
                    direction = DIR.values()[(int) (Math.random() * 2)];
                    if (direction == DIR.DIR_NORTH_EAST) {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemyRight.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    } else {
                        try {
                            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/FlyingEnemy.png"));
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error: Cannot open FlyingEnemy.png");
                            System.exit(-1);
                        }
                    }
                }
            }
        }
        /*  if(super.state == GameFigureState.FLYING_ENEMY_STATE_HIT){
        if (direction > 0) {
            // moving to the right
            super.x += (FLYING_UNIT_TRAVEL * speedBuff);
            if (super.x + WIDTH/2 > GamePanel.width -50) {
                direction = 1;
                
            }
        }else {
            // moving to the left
            super.x += (FLYING_UNIT_TRAVEL * speedBuff);
            if (super.x - ((WIDTH/2)-25) <= 0) {
                direction = 1;
            }
        }
        
        } */

    }

//    private boolean isInPanel(){
//        if((super.x < Main.gamePanel.getLocation().x) 
//                || super.x + WIDTH > (Main.gamePanel.getLocation().x + Main.gamePanel.getSize().width)
//                || super.y < Main.gamePanel.getLocation().y -150
//                || super.y + HEIGHT > (Main.gamePanel.getLocation().y + Main.gamePanel.getSize().height))
//            return false;
//        else {return true;}
//    }
    
    public void loadImages()
    {
        try {
                    mainImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error: Cannot open explosion.png.");
                    System.exit(-1);
                }
        
        
       // added by vanne 
        if (isSoundOn) //****************************sound implemented 
	         playMusic(explosion); 
        
        
        for (int i = 0; i < 16; i++)
        {
            explodeImages.add(mainImage.getSubimage((int)(i % 4) * 128, (int)(i / 4) * 128, 128, 128));
        }
    }
    
    @Override
    public Rectangle2D.Float getCollisionBox() {

        return (new Rectangle2D.Float(this.x, this.y, width, height));

    }

    // Make some modification to the game figure
    // if a collision is detected.
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
        return 0; //TODO add state
    }

    @Override
    public void setState(int state) {
        final int TODO_ADD_STATE = 777;
        state = TODO_ADD_STATE;
    }

    @Override
    public int getFigureType() {
        return this.FLYING;
    }

    @Override
    public void setSpeedBuff(double x) {
        speedBuff = x;
    }

}
//asdf
