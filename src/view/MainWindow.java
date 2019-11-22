package view;

import controller.ButtonListener;
import controller.KeyController;
import controller.Main;
import controller.MouseController;
import java.awt.Container;

import java.awt.Dimension;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JPanel;
import model.GameData;

import test.TestServer;

public class MainWindow extends JFrame {

    public static MouseController mouseController;

    public static JButton startButton, quitButton, helpButton, settingButton, restartButton;

    private String mainWeapon, secondaryWeapon;
    private int score, enemies, test;  //***************vanne
    private JLabel mainWeaponLabel, secondaryWeaponLabel, scoreLabel, enemyLabel, levelLabel; //*********vanne 

    GamePanel game = new GamePanel();
    GameData data = Main.gameData;

    public MainWindow() {
        Container c = getContentPane();

        c.add(Main.gamePanel, "Center");

        c.add(Main.gameData.getStatDisplay(), "North");
        Main.barPanel.setPreferredSize(new Dimension(0, 50));
        c.add(Main.barPanel, "South");

        mouseController = new MouseController();
        Main.gamePanel.addMouseListener(mouseController);
        Main.gamePanel.addMouseMotionListener(mouseController);

        KeyController keyListener = new KeyController();
        Main.gamePanel.addKeyListener(keyListener);
        Main.gamePanel.setFocusable(true);

        startButton = new JButton("Start Game");

        helpButton = new JButton("Help");
        settingButton = new JButton("Setting");

        quitButton = new JButton("Quit Game");
        restartButton = new JButton("Restart Game");

        mainWeaponLabel = new JLabel("Main Weapon: " + mainWeapon);
        secondaryWeaponLabel = new JLabel("Secondary Weapon: " + secondaryWeapon);
        scoreLabel = new JLabel("Score: " + score);
        enemyLabel = new JLabel("Enemies Remaining: " + enemies);
        levelLabel = new JLabel("LEVEL : " + test);                        // vanne sprint 4 

        setWeaponLabels();
        setScore();
        setEnemyLabel();

        setLevelLabel();

        ButtonListener listener = new ButtonListener();
        startButton.addActionListener(listener);

        helpButton.addActionListener(listener);
        settingButton.addActionListener(listener);
        quitButton.addActionListener(listener);
        restartButton.addActionListener(listener);

        JPanel options = new JPanel();
        options.add(startButton);
        options.add(helpButton);
        options.add(settingButton);
        options.add(quitButton);
        options.add(restartButton);
        options.add(mainWeaponLabel);
        options.add(secondaryWeaponLabel);
        options.add(scoreLabel);
        options.add(enemyLabel);
        options.add(levelLabel);
        c.add(options, "South");
        // just have one Component "true", the rest must be "false
        options.setFocusable(false);
        startButton.setFocusable(false);
        quitButton.setFocusable(false);
        restartButton.setFocusable(false);
        quitButton.setFocusable(false);
        helpButton.setFocusable(false);
        settingButton.setFocusable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenuItem testItem = new JMenuItem("Test Server");
        testItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                TestServer testServer = new TestServer();
                testServer.setSize(400, 200);
                testServer.setVisible(true);
            }
        });

        menuBar.add(testItem);
        this.setJMenuBar(menuBar);

        JMenuItem charaterSheet = new JMenuItem("Charater Sheet");
        charaterSheet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CharaterSheet charaterSheet = Main.gameData.getCharaterSheet();
                
                boolean levelUp = false;
                charaterSheet.activateCharaterSheet(levelUp);
            }
        });
        menuBar.add(charaterSheet);
        menuBar.add(testItem);
        menuBar.add(startButton);
        menuBar.add(restartButton);
        menuBar.add(quitButton);
        this.setJMenuBar(menuBar);
        restartButton.setFocusable(false);
    }

    public void setWeaponLabels() {
        mainWeapon = Main.gameData.getMainWeapon().toString();
        mainWeapon = mainWeapon.substring(mainWeapon.indexOf(".") + 1);
        mainWeapon = mainWeapon.substring(0, mainWeapon.indexOf("@"));
        if (Main.gameData.getSecondaryWeapon() != null) {
            secondaryWeapon = Main.gameData.getSecondaryWeapon().toString();
            secondaryWeapon = secondaryWeapon.substring(secondaryWeapon.indexOf(".") + 1);
            secondaryWeapon = secondaryWeapon.substring(0, secondaryWeapon.indexOf("@"));
        } else {
            secondaryWeapon = "Empty";
        }
        mainWeaponLabel.setText("Main Weapon: " + mainWeapon + "  | ");
        secondaryWeaponLabel.setText("Secondary Weapon: " + secondaryWeapon + "  | ");
    }

    public void setScore() {
        score = Main.gameData.getScore();
        scoreLabel.setText("Score: " + score + "  | ");
    }

    public void setEnemyLabel() {
        enemies = Main.gameData.getEnemiesRemaining();
        enemyLabel.setText("Enemies Remaining: " + enemies + "  | ");
    }

    public void setLevelLabel() {                                                // vanne sprint 4 
        test = Main.gameData.getLevel();
        levelLabel.setText("LEVEL : " + test);
    }

}
