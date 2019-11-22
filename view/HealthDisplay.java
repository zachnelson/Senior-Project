package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class HealthDisplay extends JPanel {

    // size of the canvas - determined at runtime once rendered
    public int width;
    public static int height;
    public double healthPercent;

    // off screen rendering
    private Graphics2D g2;
    private Image dbImage = null; // double buffer image

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
                g2 = (Graphics2D) dbImage.getGraphics();
            }
        }

        updateHealth();

        g2.setColor(Color.white);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.red);
        g2.fillRect(0, 0, (int)(width * healthPercent), height);

    }

    // use active rendering to put the buffered image on-screen
    public void printScreen() {
        Graphics g;
        try {
            g = this.getGraphics();
            if ((g != null) && (dbImage != null)) {
                g.drawImage(dbImage, 0, 0, null);
            }
            Toolkit.getDefaultToolkit().sync();  // sync the display on some systems
            if (g != null) {
                g.dispose();
            }
        } catch (Exception e) {
            System.out.println("Graphics error: " + e);
        }
    }

    public void updateHealth() {
        healthPercent = (double) model.GameData.shooter.getCurrentHealth() / model.GameData.shooter.getMaxHealth();
        //healthPercent = (double) model.GameData.shooter.currentHealth / model.GameData.shooter.maxHealth;
    }
}
//comment
