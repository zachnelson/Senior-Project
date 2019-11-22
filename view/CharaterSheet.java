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

    private JLabel increaseAgilityLabel;
    private JButton increaseAgilityButton;
    private double currentHealth;
    private double maxHealth;
    private double agility;
    //private final Subject shooterSubject;
    private final Shooter shooterObject;
    private final double observerID;
    private static int observerIDTracker = 0;

    public CharaterSheet(Shooter shooter) {
        super();

        this.setTitle("Charater Sheet");
        //this.shooterSubject = shooter;
        this.shooterObject = shooter;
        this.observerID = ++observerIDTracker;
        System.out.println("New Observer " + this.observerID);
        this.agility = 5;
        shooter.add(this);
        createComponents();
        layoutComponents();
    }

    private void createComponents() {
        increaseAgilityLabel = new JLabel("Agility: " + agility);
        this.increaseAgilityButton = new JButton(" + 1 ");
        this.increaseAgilityButton.setEnabled(false);
        this.increaseAgilityButton.addActionListener((ActionEvent ae) -> {
            agility++;
            shooterObject.setCurrentAgility(agility);
            increaseAgilityLabel.setText(String.valueOf("Agility: " + agility));
            Main.gameData.update();
            this.increaseAgilityButton.setEnabled(false);
        });

    }

    public JButton getIncreaseAgilityButton() {
        return increaseAgilityButton;
    }

    private void layoutComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;

        gbc.gridwidth = 1;
        panel.add(increaseAgilityLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(increaseAgilityButton, gbc);

        Container c = getContentPane();
        c.add(panel, "North");

    }

    @Override
    public void update(double currentHealth, double maxHealth, double agility) {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.agility = agility;
        System.out.println("this is agility = " + agility);
    }

}
