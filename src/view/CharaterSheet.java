/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Main;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Observer;
import model.Shooter;

/**
 *
 * @author Nathan
 */
public class CharaterSheet extends JFrame implements Observer {

    private JLabel agilityLabel;
    private JButton agilityButton;
    private boolean open;

    private JLabel maxHealthLabel;
    private JButton maxHealthButton;

    private JLabel armorPercentLabel;
    private JButton armorPercentButton;

    private double currentHealth;
    private double maxHealth;
    private double agility;
    private double armorPercent;

    private final Shooter shooterSubject;
    private final double observerID;
    private static int observerIDTracker = 0;

    public CharaterSheet(Shooter shooter) {
        super();
        this.open = false;
        this.setTitle("Charater Sheet");
        this.shooterSubject = shooter;
        this.observerID = ++observerIDTracker;
        shooter.add(this);
        createComponents();
        layoutComponents();
    }

    private void createComponents() {
        //setting the label text
        this.maxHealthLabel = new JLabel("Max Health: " + maxHealth);
        this.armorPercentLabel = new JLabel("Armor: " + this.armorPercent + "%");
        this.agilityLabel = new JLabel("Agility: " + agility);
        //setting th button text
        this.maxHealthButton = new JButton("+ 3");
        this.armorPercentButton = new JButton("+ 10%");
        this.agilityButton = new JButton(" + 1 ");
        //setting them to not be intially active
        disableAllButtons();

        //setting the listeners
        this.maxHealthButton.addActionListener((ActionEvent ae) -> {
            maxHealth += 3;
            shooterSubject.setMaxHealth(maxHealth);
            updateLabels();
            standardButtonActions();
        });
        this.armorPercentButton.addActionListener((ActionEvent ae) -> {
            armorPercent += 10;
            if (this.armorPercent <= 50) {
                shooterSubject.setArmorPercent(armorPercent);
                updateLabels();
                standardButtonActions();
            } else {
                armorPercentButton.setEnabled(false);
                this.armorPercentButton.setText("MAX");
            }
        });
        this.agilityButton.addActionListener((ActionEvent ae) -> {
            agility++;
            shooterSubject.setCurrentAgility(agility);
            updateLabels();
            standardButtonActions();
        });

    }

    private void updateLabels() {
        maxHealthLabel.setText(String.valueOf("Max Health: " + maxHealth));
        armorPercentLabel.setText(String.valueOf("Armor: " + armorPercent + "%"));
        agilityLabel.setText(String.valueOf("Agility: " + agility));
    }

    private void standardButtonActions() {
        Main.gameData.update();
        disableAllButtons();
        shooterSubject.setCurrentHealth(maxHealth);//heals shooter
    }

    public void disableAllButtons() {
        maxHealthButton.setEnabled(false);
        agilityButton.setEnabled(false);
        armorPercentButton.setEnabled(false);
    }

    public JButton getIncreaseMaxHealthButton() {
        return maxHealthButton;
    }

    public JButton getIncreaseArmorPercentButton() {
        return armorPercentButton;
    }

    public JButton getIncreaseAgilityButton() {
        return agilityButton;
    }

//actually adds the objects to the panel so that they can be seen
    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridwidth = 1;

        panel.add(this.maxHealthLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(this.maxHealthButton, gbc);

        panel.add(this.armorPercentLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(this.armorPercentButton, gbc);

        panel.add(agilityLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(agilityButton, gbc);

        Container c = getContentPane();
        c.add(panel, "North");
    }

    @Override
    public void updateCharaterInfo(double currentHealth, double maxHealth, double agility, double armorPercent) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.agility = agility;
        this.armorPercent = armorPercent;
    }

    public boolean loseCondition() {
        return this.currentHealth <= 0.0;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return open;
    }

//this function will allow the user updateCharaterInfo their charatersheet.  pass in true if this is a level up
    public void activateCharaterSheet(boolean levelup) {

        updateLabels();
        setSize(400, 200);
        setVisible(true);
        agilityButton.setEnabled(levelup);
        armorPercentButton.setEnabled(levelup);
        maxHealthButton.setEnabled(levelup);
        open = true;
        Main.gameData.pause();
        super.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Main.gameData.play();
                open = false;
            }
        });
    }

    public void updateShooterSubject() {
        this.shooterSubject.notifyObserver();

    }
}
