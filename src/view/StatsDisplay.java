package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;
import model.Observer;
import model.Subject;

public class StatsDisplay extends JPanel implements Observer {

    // size of the canvas - determined at runtime once rendered
    private int width;
    private static int height;
    private double healthPercent;

    // off screen rendering
    private Graphics2D healthGraphic;
    private Image dbImage = null; // double buffer image
    private double currentHealth;
    private double maxHealth;
    private final Subject shooter;
    private final int observerID;
    private static int observerIDTracker = 0;
    private double agility;
    private final double MAX_AGILITY;
    private Graphics2D agilityGraphic;
    private double agilityPercent;
    private double AGILITY_MAX = 15;
    private double armorPercent;

    public StatsDisplay(Subject shooter) {
        this.MAX_AGILITY = 30;
        this.shooter = shooter;
        this.observerID = ++observerIDTracker;
        System.out.println("New Observer " + this.observerID);
        shooter.add(this);
    }

    public void gameRender() {
        width = getSize().width;
        height = getSize().height;
        healthPercent = 1.0;
        if (dbImage == null) {
            // Creates an off-screen drawable image to be used for double buffering
            dbImage = createImage((int) (width * healthPercent), height);
            if (dbImage == null) {
                System.out.println("Critical Error: dbImage is null");
                System.exit(1);
            } else {
                healthGraphic = (Graphics2D) dbImage.getGraphics();
                agilityGraphic = (Graphics2D) dbImage.getGraphics();

            }
        }

        updateHealth();
        updateAgility();

        healthGraphic.setColor(Color.black);
        healthGraphic.fillRect(0, 0, width / 2, height);
        healthGraphic.setColor(Color.red);
        healthGraphic.fillRect(0, 0, (int) (width / 2 * healthPercent), height);

        agilityGraphic.setColor(Color.black);
        agilityGraphic.fillRect(width / 2, 0, width, height);
        agilityGraphic.setColor(Color.green);
        agilityGraphic.fillRect(width / 2, 0, (int) (width / 2 * agilityPercent), height);

    }

    // use active rendering to put the buffered image on-screen
    public void printScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync();  // sync the wdisplay on some systems
            if (g != null) {
                g.dispose();
            }
        } catch (Exception e) {
            System.out.println("Graphics error: " + e);
        }
    }

    public void updateHealth() {
        healthPercent = currentHealth / maxHealth;
    }

    public void updateAgility() {
        agilityPercent = agility / AGILITY_MAX;
    }

    @Override
    public void updateCharaterInfo(double currentHealth, double maxHealth, double agility, double armorPercent) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.agility = agility;
        this.armorPercent = armorPercent;
    }
    
}
//comment 5
