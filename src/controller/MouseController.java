package controller;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;
import model.Shooter;
import model.WeaponFactory;
import model.WeaponFigure;
import model.WeaponInventory;
import view.GamePanel;

import static view.GamePanel.Boom;
import static view.GamePanel.playMusic;

import static view.GamePanel.Laser;
import static view.GamePanel.isSoundOn;

public class MouseController extends MouseAdapter {

    public int x;
    public int y;

    GamePanel game = new GamePanel();

    @Override
    public void mousePressed(MouseEvent me) {
        x = me.getX();
        y = me.getY();

        if (!Main.gameData.isCharacterPicked()) {
            if (x >= 450 && x <= 650) {
                if (y >= 100 && y <= 300) {
                    Shooter.getInstance().setImageColor(Shooter.SHOOTER_COLOR_BLUE);
                    Main.gameData.setCharacterPicked(true);
                } else if (y >= 350 && y <= 550) {
                    Shooter.getInstance().setImageColor(Shooter.SHOOTER_COLOR_YELLOW);
                    Main.gameData.setCharacterPicked(true);
                }
            } else if (x >= 650 && x <= 850) {
                if (y >= 100 && y <= 300) {
                    Shooter.getInstance().setImageColor(Shooter.SHOOTER_COLOR_RED);
                    Main.gameData.setCharacterPicked(true);
                } else if (y >= 350 && y <= 550) {
                    Shooter.getInstance().setImageColor(Shooter.SHOOTER_COLOR_GREEN);
                    Main.gameData.setCharacterPicked(true);
                }
            }
        } else {
            if (!Main.gameData.paused()) {
                Shooter shooter = (Shooter) Main.gameData.getFriendFigures().get(0);

                WeaponFactory factory = new WeaponFactory();
                ArrayList<WeaponFigure> m = null;
                try {
                    if (SwingUtilities.isLeftMouseButton(me)) {
                        // Left mouse button, so attack with mean weapon
                        m = factory.buildMainWeapon(shooter, x, y);
                        if (m != null) {
                            if (isSoundOn) { // added by vanne 
                                playMusic(Boom);
                            }
                        }

                    } else if (SwingUtilities.isRightMouseButton(me)) {
                        // Right mouse button, so attack with secondary weapon
                        WeaponInventory weaponInventory = (WeaponInventory)Main.gameData.getWeaponInventory().get(0);
                        if (weaponInventory.getSelectedSlotState()) {
                            m = factory.buildSeconaryWeapon(shooter, x, y);
                            if (m != null) {
                                if (isSoundOn) // adde by vanne 
                                {
                                    playMusic(Laser);
                                }
                            }
                        } else {
                        }

                    }
                } catch (Exception e) {
                    //Factory could not build the weapon
                }

                if (m != null) {
                    synchronized (Main.gameData.getFriendFigures()) {
                        for (WeaponFigure f : m) {
                            Main.gameData.getFriendFigures().add(f);
                        }
                    }
                }
            } else {
                Main.gameData.getSettingsData().checkSettings(x, y);
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

}
