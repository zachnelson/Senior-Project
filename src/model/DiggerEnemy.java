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

public class DiggerEnemy extends EnemyFigure {

    public int dWidth;
    public int dHeight;
    public int hWidth;
    public int hHeight;
    public float holeX;
    public float holeY;
    private int frameCount;
    public final List<BufferedImage> explodeImages;
    private BufferedImage holeImage;
    private double speedBuff = 1;

    public DiggerEnemy(float x, float y) {
        super(x, y); // origin: upper-left corner
        super.health = 5;
        super.damage = 1;
        super.pointValue = 2;
        holeX = super.x;
        holeY = super.y;
        super.state = GameFigureState.DIGGER_ENEMY_STATE_HOLE_OPENING;
        frameCount = 0;
        setImage("/model/assets/digger.png", "/model/assets/diggerHole.png");
        this.dHeight = 0;
        this.dWidth = 0;
        this.hHeight = 0;
        this.hWidth = 0;
        explodeImages = new CopyOnWriteArrayList<>();
    }

    public void setImage(String file, String file2) {
        mainImage = null;
        holeImage = null;
        try {
            mainImage = ImageIO.read(getClass().getResource(file));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
            System.exit(-1);
        }
        try {
            holeImage = ImageIO.read(getClass().getResource(file2));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open enemy picture.");
            System.exit(-1);
        }
    }

    @Override
    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            state = GameFigureState.DIGGER_ENEMY_STATE_DAMAGED;
            frameCount = 0;
            if (!super.dead) {
                super.dead = true;
                super.IncreaseScore(super.pointValue);
                super.enemyFactory.removeEnemyCharater();
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
            //------desmond--------------------------------
            //constrain the digger enemy in the game scene
            //check if x, y will bring digger out of screen
            //---------------------------------------------
            if (super.x - mainImage.getWidth() / 2 < 0) {
                super.x = mainImage.getWidth() / 2;
            }
            if (super.x + mainImage.getWidth() / 2 > Main.WIN_WIDTH) {
                super.x = Main.WIN_WIDTH - mainImage.getWidth() / 2;
            }
            if (super.y - mainImage.getHeight() / 2 < 0) {
                super.y = mainImage.getHeight() / 2;
            }
            if (super.y + mainImage.getHeight() + 100 > Main.WIN_HEIGHT) {       
                super.y = Main.WIN_HEIGHT /2;
            }
            //---------------------------------------------
        g.drawImage(holeImage, (int) holeX, (int) holeY,
                hWidth, hHeight, null);
        g.drawImage(mainImage, (int) super.x, (int) super.y,
                dWidth, dHeight, null);
    }

    @Override
    public void update() {
        if (super.state == GameFigureState.DIGGER_ENEMY_STATE_HOLE_OPENING) {
            if (hWidth < holeImage.getWidth()) {
                hWidth += 5;
                hHeight += 5;
                holeX -= 2.5;
                holeY -= 2.5;
            } else {
                super.state = GameFigureState.DIGGER_ENEMY_STATE_HOLE_OPEN;
            }

        } else if (super.state == GameFigureState.DIGGER_ENEMY_STATE_HOLE_OPEN) {

            if (dWidth < mainImage.getWidth()) {
                dWidth += 5;
                dHeight += 5;
                this.x -= 2.5;
                this.y -= 2.5;
            } else {
                super.state = GameFigureState.DIGGER_ENEMY_STATE_APPEARED;
                DiggerShot s = new DiggerShot(this.x + dWidth / 2, this.y + dHeight / 2, 20, 20);
                Main.gameData.getEnemyFigures().add(s);
                frameCount = 0;
            }
        } else if (super.state == GameFigureState.DIGGER_ENEMY_STATE_APPEARED) {
            if (frameCount > 80) {
                if (dWidth > 5) {
                    dWidth -= 8;
                    dHeight -= 8;
                    this.x += 4;
                    this.y += 4;
                } else {
                    super.state = GameFigureState.DIGGER_ENEMY_STATE_HOLE_CLOSING;
                }
            }

        } else if (super.state == GameFigureState.DIGGER_ENEMY_STATE_HOLE_CLOSING) {
            if (hWidth > 5) {
                hWidth -= 5;
                hHeight -= 5;
                holeX += 2.5;
                holeY += 2.5;
            } else {
                super.state = GameFigureState.DIGGER_ENEMY_STATE_HOLE_CLOSED;
            }
        } else if (super.state == GameFigureState.DIGGER_ENEMY_STATE_HOLE_CLOSED) {
            super.x = (int) Math.floor(Math.random() * (Main.WIN_WIDTH - mainImage.getWidth() / 3));
            super.y = (int) Math.floor(Math.random() * (Main.WIN_HEIGHT - mainImage.getHeight() / 3));
            while (Math.abs(Main.gameData.getShooter().x - this.x) < 100 && Math.abs(Main.gameData.getShooter().y - this.y) < 100) {
                super.x = (int) Math.floor(Math.random() * (Main.WIN_WIDTH - mainImage.getWidth() / 3));
                super.y = (int) Math.floor(Math.random() * (Main.WIN_HEIGHT - mainImage.getHeight() / 3));
            }


            holeX = super.x;
            holeY = super.y;
            super.state = GameFigureState.DIGGER_ENEMY_STATE_HOLE_OPENING;
        } else if (super.state == GameFigureState.DIGGER_ENEMY_STATE_DAMAGED) {
            hHeight = 0;
            hWidth = 0;
            if (frameCount == 0) {
                loadImages();
                dWidth = 70;
                dHeight = 70;
                super.x -= 5;
                super.y -= 5;
            }
            if (frameCount < 48) {
                if (explodeImages.size() == 0) {
                    loadImages();
                }
                mainImage = explodeImages.get(frameCount / 3);
            } else {
                super.state = GameFigureState.STATE_DONE;
            }
        }

        frameCount++;

    }

    public void loadImages() {
        try {
            mainImage = ImageIO.read(getClass().getResource("/model/assets/explosion.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open explosion.png.");
            System.exit(-1);
        }

        // added by vanne 
        if (isSoundOn) //****************************sound implemented 
        {
            playMusic(explosion);
        }

        for (int i = 0; i < 16; i++) {
            explodeImages.add(mainImage.getSubimage((int) (i % 4) * 128, (int) (i / 4) * 128, 128, 128));
        }
    }

    @Override
    public Rectangle2D.Float getCollisionBox() {
        if (super.state == GameFigureState.DIGGER_ENEMY_STATE_APPEARED || super.state == GameFigureState.DIGGER_ENEMY_STATE_HOLE_OPEN) {
            return (new Rectangle2D.Float(this.x, this.y, dWidth, dHeight));
        } else {
            return (new Rectangle2D.Float(0, 0, 0, 0));
        }
    }
    // Make some modification to the game figure
    // if a collision is detected.

    @Override
    public double getWidth() {
        return dWidth;
    }

    @Override
    public double getHeight() {
        return dHeight;
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
        return this.DIGGER;
    }

    @Override
    public void setSpeedBuff(double x) {
        speedBuff = x;
    }

}
