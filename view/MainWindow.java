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
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import javax.swing.JPanel;

import test.TestServer;

public class MainWindow extends JFrame {

    public static MouseController mouseController;

    public static JButton startButton, quitButton, helpButton, settingButton, restartButton;

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
        c.add(options, "South");
        // just have one Component "true", the rest must be "false
        options.setFocusable(false);
        startButton.setFocusable(false);
        quitButton.setFocusable(false);
        restartButton.setFocusable(false);
        quitButton.setFocusable(false);
        helpButton.setFocusable(false);

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
                charaterSheet.setSize(400, 200);
                charaterSheet.setVisible(true);
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
}
