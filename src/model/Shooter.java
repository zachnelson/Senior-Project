package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static java.lang.System.currentTimeMillis;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class Shooter extends GameFigure implements Subject {

    private final int height;
    private int width;
    private final int MILLI_PER_SEC = 1000;
    private final double VULNERABILITY_TIME = .25;

    private final double DEFAULT_SPEED = 5.0;
    private final double ACCELERATION_VERT = 5;
    private final double ACCELERATION_HORIZ = 5;

    private boolean[] directions = new boolean[255];

    private int frameNum = 0;
    private BufferedImage transImage;
    private BufferedImage drawImage;  //image that will be rendered to the screen.
    private final double FRICTION = 0.8;
    private ArrayList<Observer> observers;
    private double maxHealth, currentHealth;
    private double speedBuff = 1;
    private double agility;
    private double velocityVert;
    private double velocityHoriz;
    private double accelerationVert;
    private double accelerationHorz;
    private double armorPercent; //so 10 points is 10% resistance

    private double friction = 0.8;
    private long lastDamaged;
    private static Shooter SHOOTER = new Shooter(Main.WIN_WIDTH / 2, Main.WIN_HEIGHT - 200);

    private boolean shieldActive = false;
    public boolean slowed = false;

    private Point2D.Float nozzleLocation;
    
    public static final int SHOOTER_COLOR_BLUE = 0;
    public static final int SHOOTER_COLOR_RED = 1;
    public static final int SHOOTER_COLOR_YELLOW = 2;
    public static final int SHOOTER_COLOR_GREEN = 3;

    
    private boolean w = false;
    private boolean a = false;
    private boolean s = false;
    private boolean d = false;
    
    public static Shooter getInstance() {
        return SHOOTER;
    }

    //singlton pattern prevents multiple shooters DO NOT CHANGE TO PUBLIC
    private Shooter(int x, int y) {
        super(x, y);
        observers = new ArrayList<>();
        agility = DEFAULT_SPEED;

        accelerationVert = ACCELERATION_VERT;
        accelerationHorz = ACCELERATION_HORIZ;
        velocityVert = 0;
        velocityHoriz = 0;

        lastDamaged = currentTimeMillis();
        friction = FRICTION;
        frozen = false;

        friction = FRICTION;
        frozen = false;

        super.damage = 0;
        super.state = GameFigureState.SHOOTER_STATE_HEALTH_LEVEL_5;
        try {
            super.mainImage = ImageIO.read(getClass().getResource("/model/assets/shooter.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open shooter.png");
            System.exit(-1);
        }
        transImage = null;
        try {
            transImage = ImageIO.read(getClass().getResource("/model/assets/transparent_box.png"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open red.png");
            System.exit(-1);
        }

        this.nozzleLocation = new Point2D.Float(x, y);   //initialize nozzle position

        this.height = mainImage.getHeight();
        this.width = mainImage.getWidth();
        this.armorPercent = 0;

    }

    @Override
    public void render(Graphics2D g) {

        //Rotate the shooter appropraitely so that he is always pointing in 
        //the direction of shoot
        int mouseX = Main.game.mouseController.x;
        int mouseY = Main.game.mouseController.y;

        float deltaX = mouseX - super.x;
        float deltaY = mouseY - super.y;

        double angle = Math.atan2(Math.abs(deltaY), Math.abs(deltaX));
        double angle_deg = Math.toDegrees(angle);  //this is offset by 90 degrees

        if (mouseX > super.x && mouseY < super.y) { // Upper Right
            angle_deg = Math.abs(angle_deg - 90);
        } else if (mouseX < super.x && mouseY < super.y) { // Upper Left
            angle_deg += 270;
        } else if (mouseX < super.x && mouseY > super.y) { // Lower Left
            angle_deg = Math.abs(angle_deg - 90) + 180;
        } else {    // Lower Right
            angle_deg += 90;
        }

        setNozzleLocation(angle, mouseX, mouseY);

        if (!frozen) {

            drawImage = rotateImage(mainImage, (int) angle_deg);

            g.drawImage(drawImage, (int) super.x, (int) super.y, width, height, null);
        } else {
            if (frameNum % 2 == 0) {
                g.drawImage(transImage, (int) super.x, (int) super.y, width, height, null);
            } else {
                g.drawImage(drawImage, (int) super.x, (int) super.y, width, height, null);

            }
        }
        frameNum++;

    }

    public void setNozzleLocation(double angle, int mouseX, int mouseY) {
        float rotateX;
        float rotateY;
        rotateX = (float) width / 2 * (float) Math.cos(angle);
        rotateY = (float) height / 2 * (float) Math.sin(angle);

        if (mouseX > super.x && mouseY < super.y) { // mouse is upper-right side
            rotateY = -rotateY;
        } else if (mouseX < super.x && mouseY < super.y) { // mouse is upper-left side
            rotateX = -rotateX;
            rotateY = -rotateY;
        } else if (mouseX < super.x && mouseY > super.y) { // mouse is lower-left side
            rotateX = -rotateX;
        } else { // mouse is lower-right side
            //dont change anything
        }

        //Rotate nozzle location around the center of the shooter image
        nozzleLocation.x = (super.x + width / 2) + rotateX;
        nozzleLocation.y = (super.y + height / 2) + rotateY;
    }

    @Override
    public void update() {
        // no periodic updateCharaterInfo is required (not animated)
        // if health level is implemented, updateCharaterInfo level
        // updateCharaterInfo is done via 'translate' when a key is pressed
        if (shieldActive) {
            // Cant be frozen if the shield is active!
            frozen = false;
            translate();
        } else {
            if (!frozen) {
                translate();
            }  
//        System.out.println("A: " + Main.gameData.getStore().getA() + " W: " + Main.gameData.getStore().getW() +
//                " S: " + Main.gameData.getStore().getS() +
//                " D: " + Main.gameData.getStore().getD());
        }

        //Rotate the shooter appropraitely so that he is always pointing in 
        //the direction of shoot
        int mouseY = MouseInfo.getPointerInfo().getLocation().y;
        int mouseX = MouseInfo.getPointerInfo().getLocation().x;
        double angle = Math.atan2(mouseY - this.y, mouseX - this.x + width / 2);
        double angle_deg = Math.toDegrees(angle) + 90;  //this is offset by 90 degrees
        drawImage = rotateImage(mainImage, (int) angle_deg);
        //nozzleLocation.x = (float)this.x + (float)(Math.sin(angle) * width/2);
        //nozzleLocation.y = (float)this.y + (float)(Math.cos(angle) * width/2);
//        nozzleLocation.x = super.x;
//        nozzleLocation.y = super.y;

    }

    public void translate() {

        if (super.y + this.height + this.velocityVert > Main.WIN_HEIGHT - 100) {
            super.y = Main.WIN_HEIGHT - 100 - this.height;
        } else if (super.y + this.velocityVert < 0) {
            super.y = 0;
        } else {
            super.y += this.velocityVert;
        }

        if (super.x + this.width + this.velocityHoriz > Main.WIN_WIDTH) {
            super.x = Main.WIN_WIDTH - this.width;
        } else if (super.x + this.velocityHoriz < 0) {
            super.x = 1;
        } else {
            super.x += this.velocityHoriz;
        }

        if (directions[KeyEvent.VK_W] && Main.gameData.getStore().getW()) {
            this.velocityVert -= ACCELERATION_VERT;
            this.velocityVert = Math.max(this.velocityVert, -(agility * speedBuff));
            w = true;
        }
        else
            w = false;
        if (directions[KeyEvent.VK_S] && Main.gameData.getStore().getS()) {
            this.velocityVert += ACCELERATION_VERT;
            this.velocityVert = Math.min(this.velocityVert, (agility * speedBuff));
            s = true;
        }
        else
            s = false;

        if (directions[KeyEvent.VK_A] && Main.gameData.getStore().getA()) {
            this.velocityHoriz -= ACCELERATION_HORIZ;
            this.velocityHoriz = Math.max(this.velocityHoriz, -(agility * speedBuff));
            a = true;
        }
        else
            a = false;

        if (directions[KeyEvent.VK_D] && Main.gameData.getStore().getD()) {
            this.velocityHoriz += ACCELERATION_HORIZ;
            this.velocityHoriz = Math.min(this.velocityHoriz, (agility * speedBuff));
            d = true;
        }
        else
            d = false;
        velocityHoriz *= friction;
        velocityVert *= friction;

        //if(this.getCollisionBox().intersects(Main.gameData.getEnemyFigures().indexOf(Barrel))){
        // }
        
    }

    //---------------------------------------
    //Rotate Image over n degrees gradians
    //Works best for square images
    //---------------------------------------
    public BufferedImage rotateImage(BufferedImage imag, int n) { //n rotation in gradians
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

    @Override
    public Rectangle2D.Float getCollisionBox() {
        return (new Rectangle2D.Float(super.x, super.y,
                width, height));
    }

    @Override

    public void takeDamage(double damage) {
        //Only take damage when shield is not active!
        //changes damage to concider armor
        //System.out.println("taking damage");
        damage *= (1 - (armorPercent / 100));
        if (!shieldActive) {
            if ((currentTimeMillis() - lastDamaged) / MILLI_PER_SEC >= VULNERABILITY_TIME) {
                currentHealth -= damage;
                this.setCurrentHealth(currentHealth);
                lastDamaged = currentTimeMillis();

            }
        }
        if (currentHealth <= 0) {
            controller.Main.gameData.clearGame();
        }
    }

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

    public boolean[] getDirections() {
        return directions;
    }

    @Override
    public void add(Observer newObserver) {
        observers.add(newObserver);  //add an observer to the list
    }

    @Override
    public void remove(Observer deleteObserver) {
        observers.remove(observers.indexOf(deleteObserver));// inner part gets returns the index so it can delete the correct observer
    }

    @Override
    public void notifyObserver() {
        for (Observer observer : observers) {
            observer.updateCharaterInfo(currentHealth, maxHealth, agility, this.armorPercent);
        }
    }

    public void setCurrentHealth(double currentHealth) {
        this.currentHealth = currentHealth;
        notifyObserver();
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
        notifyObserver();
    }

    public void setCurrentAgility(double agility) {
        this.agility = agility;
        notifyObserver();
    }

    public void setArmorPercent(double armorPercent) {
        if (armorPercent <= 50) {
            this.armorPercent = armorPercent;
        }
        notifyObserver();
    }

    public void setSpeedBuff(double speedBuff) {
        this.speedBuff = speedBuff;
    }
    
    public double getSpeedBuff() {
        return speedBuff;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    //player picked up health
    public void increaseHealth(double x) {
        currentHealth = currentHealth + x;
        notifyObserver();
    }

    //example pass in 10 to increase armor by 10%
    public void increaseArmor(double armorPercent) {
        this.armorPercent = armorPercent;
        this.notifyObserver();
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public void setShieldActive(boolean shieldActive) {
        this.shieldActive = shieldActive;
    }

    public void setX(float x) {
        super.x = x;
    }

    public void setY(float y) {
        super.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    public Point2D.Float getNozzlePosition() {
        return this.nozzleLocation;
    }

    public boolean lost() {
        return currentHealth == 0;
    }
    
    public void setImageColor(int color){
        String shooterImage = "";
        switch (color){
            case SHOOTER_COLOR_BLUE:
                shooterImage = "shooter_blue";
                break;
            case SHOOTER_COLOR_RED:
                shooterImage = "shooter_red";
                break;
            case SHOOTER_COLOR_YELLOW:
                shooterImage = "shooter_yellow";
                break;
            case SHOOTER_COLOR_GREEN:
                shooterImage = "shooter_green";
                break;
        }
        if (shooterImage != ""){
            try{
                super.mainImage = ImageIO.read(getClass().getResource("/model/assets/"+ shooterImage + ".png"));
            } catch (Exception e){
                System.out.println("Could not load " + shooterImage + ".png");
            }
        }
    }

    //Zach - so the player doesn't walk through store walls
    public void setW(boolean w) {
        this.w = w;
    }

    public void setS(boolean s) {
        this.s = s;
    }

    public void setA(boolean a) {
        this.a = a;
    }

    public void setD(boolean d) {
        this.d = d;
    }

    public boolean getW() {
        return w;
    }

    public boolean getA() {
        return a;
    }

    public boolean getS() {
        return s;
    }

    public boolean getD() {
        return d;
    }
    
    
}
