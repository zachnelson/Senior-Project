package view;

import controller.Main;
import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

import static java.awt.image.ImageObserver.WIDTH;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.GameData;
import static model.GameData.characterPicked;
import model.GameFigure;
import model.Shooter;

public class GamePanel extends JPanel {

    // size of the canvas - determined at runtime once rendered
    public static int width;
    public static int height;
    public static boolean gameStart = false;

    // off screen rendering
    private Graphics2D g2;
    private static Image dbImage = null; // double buffer image

    // private static Image newGameImage, startedImage, helpsection, settingImage;                         // vanne
    public static Image newGameImage, startedImage, helpsection, settingImage;
    private static boolean checknewGamebkg, checkgameStartedbkg, checkhelpsectionbkg, checksettingImagebkg;

    // added by vanne sprint 3 
    public static File Laser = new File("src/audio/laser.WAV");   // modified 
    public static File Boom = new File("src/audio/boom.WAV");     // modified 
    public static File PlayerBoom = new File("src/audio/playerboom.WAV");
    public static File explosion = new File("src/audio/explosion.WAV");
    File Music = new File("src/audio/aliendream.WAV");
    File GameMusic = new File("src/audio/rocker.WAV");
    Clip clip;   // i added static to try  

    //***end**************************************************************************************************************************************
    //from input manager 
    public static boolean isMenu = true;
    public static boolean isSettings = false;
    public static boolean isMute = false;
    public static boolean isSoundMute = false;

    // variable for sound 
    private boolean isGameMusicOn = false;
    private boolean isMenuMusicOn = true;
    private boolean fromMute = false;
    private boolean fromMenu = true;
    public static boolean isSoundOn = true;
    private boolean isMusicOn = true;           // i added static to try 
    //**************************************

    // add enu for the game 
    public static enum STATE {
        MENU,
        GAME,
        HELP,
        SETTINGS,
        MUTEMUSIC,
        MUTESOUND

    };

    public static STATE State = STATE.MENU;

    public static STATE State2;

    //****************************
    private final String Gametitle;

    private static boolean newGamebkg, gameStartedbkg;
    //  public  static boolean newGamebkg, gameStartedbkg;                           // vanne sprint 4 

    //**********vanne level design  
    public static int Level = 1;                                                          // vanne sprint 4 
    public static int TotalEnemy = 1;
    public static boolean EnemyExist = true;
    GameData data = new GameData();

    public final String WIN_MESSAGE = "CONGRATULATION! | ";
    public final String LOST_MESSAGE = "YOU LOST | ";

    private Image shooter_blue, shooter_red, shooter_yellow, shooter_green;

    private boolean accessedStore = false;
    
    public GamePanel() {
        Gametitle = "Space Finger";
        newGamebkg = true;
        gameStartedbkg = false;
        checknewGamebkg = true;
        checkgameStartedbkg = false;
        checkhelpsectionbkg = false;
        checksettingImagebkg = false; // false

        State = STATE.MENU;

        String imagePath = System.getProperty("user.dir");
        String separator = System.getProperty("file.separator");

        newGameImage = null;
        startedImage = null;
        try {

            newGameImage = ImageIO.read(getClass().getResource("/model/assets/BackgroundImageMenu4.jpg"));
            startedImage = ImageIO.read(getClass().getResource("/model/assets/backgroundImageMenu1.png"));
            helpsection = ImageIO.read(getClass().getResource("/model/assets/backgroundImageMenu12.png"));
            settingImage = ImageIO.read(getClass().getResource("/model/assets/backgroundImageMenu1.png"));

            try {
                shooter_blue = ImageIO.read(getClass().getResource("/model/assets/shooter_blue.png"));
                shooter_red = ImageIO.read(getClass().getResource("/model/assets/shooter_red.png"));
                shooter_yellow = ImageIO.read(getClass().getResource("/model/assets/shooter_yellow.png"));
                shooter_green = ImageIO.read(getClass().getResource("/model/assets/shooter_green.png"));

            } catch (Exception e) {

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error: Cannot open background images.png");
            System.exit(-1);
        }

    }

    // level design vanne                                                       // vanne sprint 4 
    public void setLevel(int level) {
        this.Level = level;
    }

    public int getLevel() {

        return Level;

    }

    /**
     * getImage :- uses try catch clause to read a file and return if the file
     * is an image
     */
    public static Image getImage(String fileName) {
        Image image = null;
        try {
            image = ImageIO.read(new File(fileName));
        } catch (IOException ioe) {
            System.out.println("Error: Cannot open image:" + fileName);
            JOptionPane.showMessageDialog(null, "Error: Cannot open image:" + fileName);
        }
        return image;
    }

    public void gameRender() throws IOException {
        width = getSize().width;
        height = getSize().height;
        if (dbImage == null) {
            // Creates an off-screen drawable image to be used for double buffering
            dbImage = createImage(width, height);
            if (dbImage == null) {
                System.out.println("Critical Error GamePanel: dbImage is null");
                System.exit(1);
            } else {
                g2 = (Graphics2D) dbImage.getGraphics();
            }
        }

        g2.clearRect(0, 0, width, height);

        Font fnt0 = new Font("arial", Font.BOLD, 50);

        if (newGamebkg) {

            g2.drawImage(newGameImage, 0, 0, null);
            g2.setFont(fnt0);
            g2.setColor(Color.red);
            g2.drawString(Gametitle, GamePanel.WIDTH / 2 + 230, 100);

            whenGameStart();
            // System.out.println("the State is " + State);

        }

        if (checkgameStartedbkg) {
            g2.clearRect(0, 0, width, height);
            g2.setBackground(Color.BLACK);
            g2.drawImage(startedImage, 0, 0, null);

            // unmutemusic();
            //isMusicOn();
            // setMusicMute(false);
        }

        if (checkhelpsectionbkg) {

            g2.clearRect(0, 0, width, height);
            g2.setBackground(Color.BLACK);
            g2.drawImage(helpsection, 0, 0, null);

            Rectangle menuButton = new Rectangle(WIDTH / 2 + 1050, 20, 180, 40); // 650
            Font fnt2 = new Font("arial", Font.BOLD, 30);
            g2.setFont(fnt2);
            g2.setColor(Color.red);
            g2.drawString("   HELP", menuButton.x + 19, menuButton.y + 30);
            g2.draw(menuButton);

            // try this 
        }

        if (checksettingImagebkg) {

            g2.clearRect(0, 0, width, height);
            g2.setBackground(Color.BLACK);
            g2.drawImage(settingImage, 0, 0, null);

            //w 
            Rectangle menuButton = new Rectangle(WIDTH / 2 + 1050, 20, 180, 40);
            Font fnt5 = new Font("arial", Font.BOLD, 30);
            g2.setFont(fnt5);
            g2.setColor(Color.red);
            g2.drawString("SETTINGS", menuButton.x + 19, menuButton.y + 30);
            g2.draw(menuButton);

            //State = STATE.SETTINGS;
            //  music  
            // if (State == STATE.SETTINGS){
            Rectangle muteMusicButton = new Rectangle(125, 175, 225, 40);
            Font fnt3 = new Font("arial", Font.BOLD, 30);
            g2.setFont(fnt3);
            g2.setColor(Color.green);
            g2.drawString("MUTE MUSIC", muteMusicButton.x + 19, muteMusicButton.y + 30);
            g2.draw(muteMusicButton);

            //}
            // else if(State == STATE.MUTEMUSIC){                     
            Rectangle muteMusicButton2 = new Rectangle(450, 175, 270, 40);
            Font fnt4 = new Font("arial", Font.BOLD, 30);
            g2.setFont(fnt4);
            g2.setColor(Color.yellow);
            g2.drawString("UNMUTE MUSIC", muteMusicButton2.x + 19, muteMusicButton2.y + 30);
            g2.draw(muteMusicButton2);

            //***********sound button 
            if (State == STATE.SETTINGS || State == STATE.MUTEMUSIC) {

                //if (isSoundOn) {
                Rectangle muteSoundButton = new Rectangle(125, 255, 225, 40);
                Font fnt7 = new Font("arial", Font.BOLD, 30);
                g2.setFont(fnt3);
                g2.setColor(Color.green);
                g2.drawString("MUTE SOUND", muteSoundButton.x + 10, muteSoundButton.y + 30);
                g2.draw(muteSoundButton);
                //}

                Rectangle unMuteSoundButton = new Rectangle(450, 255, 270, 40);
                Font fnt8 = new Font("arial", Font.BOLD, 30);
                g2.setFont(fnt3);
                g2.setColor(Color.yellow);
                g2.drawString("UNMUTE SOUND", unMuteSoundButton.x + 10, unMuteSoundButton.y + 30);
                g2.draw(unMuteSoundButton);

            }

            //*******************SOUND*********************
            if (State == STATE.MUTEMUSIC) {

                clip.stop();
                isMenuMusicOn = false;
                fromMute = true;

            }

            //g2.clearRect(0, 0, width, height);
//            g2.setBackground(Color.WHITE);
        } else if (gameStartedbkg) {
            g2.clearRect(0, 0, width, height);
            g2.setBackground(Color.BLACK);
            g2.drawImage(startedImage, 0, 0, null);
        }

        if ((Main.gameData.getRunning() && gameStart
                && Main.gameData.getFriendFigures().size() != 0
                && !Main.gameData.isCharacterPicked())) {
            g2.setFont(fnt0);
            g2.setColor(Color.green);
            g2.drawString("Select Your Character", GamePanel.WIDTH / 2 + 50, 80);
            if (shooter_blue != null) {
                g2.drawImage(shooter_blue, 450, 100, 200, 200, null);
            }
            if (shooter_red != null) {
                g2.drawImage(shooter_red, 650, 100, 200, 200, null);
            }
            if (shooter_yellow != null) {
                g2.drawImage(shooter_yellow, 450, 350, 200, 200, null);
            }
            if (shooter_green != null) {
                g2.drawImage(shooter_green, 650, 350, 200, 200, null);
            }
        } else {
            if (Main.gameData.getRunning() && gameStart && Main.gameData.isCharacterPicked()) {

                if(Level == 4 && accessedStore == false){
                    //System.out.println("Accessing Store");
                    data.addEnemies(Level);
                }
                else if (Level != 4){
                    //System.out.println("Loading Level: " + Level);
                    data.addEnemies(Level);
                }

                for (GameFigure f : Main.gameData.getEnemyFigures()) {
                    f.render(g2);
                }
                for (GameFigure f : Main.gameData.getFriendFigures()) {
                    f.render(g2);
                }
                for (GameFigure f : Main.gameData.getMiscFigures()) {
                    f.render(g2);
                }
                for (GameFigure f : Main.gameData.getInventoryFigures()) {
                    f.render(g2);
                }
                
                for (GameFigure f : Main.gameData.getWeaponInventory()) {
                f.render(g2);
                }

                for (GameFigure f : Main.gameData.getBonusFigures()) {             // vanne sprint 5 
                    f.render(g2);
                }

            }

            //System.out.println("number of ennemies for background : "  + GameData.enemyFigures.size() );
            if (Main.gameData.getRunning() && gameStart && checkgameStartedbkg) {

                g2.drawImage(startedImage, 0, 0, null);

                for (GameFigure f : Main.gameData.getEnemyFigures()) {
                    f.render(g2);
                }

                for (GameFigure f : Main.gameData.getFriendFigures()) {
                    f.render(g2);
                }

                for (GameFigure f : Main.gameData.getMiscFigures()) {
                    f.render(g2);
                }

                for (GameFigure f : Main.gameData.getInventoryFigures()) {
                    f.render(g2);
                }
                
                for (GameFigure f : Main.gameData.getWeaponInventory()) {
                     f.render(g2);
                }

                for (GameFigure f : Main.gameData.getBonusFigures()) {             // vanne sprint 5 
                    f.render(g2);
                }

            }
        }

        if (Main.gameData.getWin() && GameData.enemyFigures.size() == 0) {

            g2.setFont(fnt0);
            g2.setColor(Color.red);
//<<<<<<< HEAD
            // LOST_MESSAGE = "" ;

            int score = Main.gameData.getScore();

            g2.drawString(WIN_MESSAGE + "\n your score : " + score, GamePanel.WIDTH / 2 + 230, 100);
            //

//        } else if (Main.gameData.getFriendFigures().size() == 0) {
//
////=======
//            // LOST_MESSAGE = "" ;
//            int score = Main.gameData.getScore();
//
//            g2.drawString(WIN_MESSAGE + "\n your score : " + Main.gameData.getScore(), GamePanel.WIDTH / 2 + 230, 100);

        } else if (Main.gameData.getFriendFigures().size() == 0 && !Main.gameData.getWin()) {
//<<<<<<< HEAD

//>>>>>>> 83165351647c8bb633e8e02b7e904e0ba22be4e2
//=======
            Main.gameData.pause();
            Main.gameData.getGameState().goNext(Main.gameData);
//>>>>>>> a37ebd26d20eb190354d8032d73157be1c58f529
            g2.setFont(fnt0);
            g2.setColor(Color.red);
            // WIN_MESSAGE = ""; 

            g2.drawString(LOST_MESSAGE + "\n your score : " + Main.gameData.getScore(), GamePanel.WIDTH / 2 + 230, 100);

            //Main.gameData.clearGame();
            // Main.gameData.clearGame();
            //Level =1 ; 
        }

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

    public static void StartGame() {
        checknewGamebkg = false;
        checkgameStartedbkg = true;
        gameStart = true;
        State = STATE.GAME;
        //System.out.println("the State is " + State);
        //  MainWindow.startButton.setEnabled(false);
    }

    public static void restartGame() {
        checknewGamebkg = false;
        checkgameStartedbkg = true;
        gameStart = true;
        State = STATE.GAME;
        System.out.println("the State is " + State);
        Level = 1;
        int score = 0;
        Main.gameData.setScore(score);
        Level = 1;
        Main.gameData.clearGame();
        Main.gameData.initGame();
        MainWindow.startButton.setEnabled(false);
    }

    // added button 09/22/18
    public static void HelpButton() {
        checknewGamebkg = false;
        checkgameStartedbkg = false;
        checksettingImagebkg = false;
        checkhelpsectionbkg = true;
        State = STATE.HELP;
        // System.out.println("the State is " + State);
        // gameStart = true;
        MainWindow.startButton.setEnabled(true);
        Main.gameData.pause();
    }

    public static void settingButton() {
        checknewGamebkg = false;
        checkgameStartedbkg = false;
        checksettingImagebkg = true;
        checkhelpsectionbkg = false;

        State = STATE.SETTINGS;
        //   gameStart = true;
        Main.gameData.pause();
    }

    public static void unpauseGame() {
        checknewGamebkg = false;
        checkgameStartedbkg = true;
        checksettingImagebkg = false;
        checkhelpsectionbkg = false;
        State = STATE.GAME;
        MainWindow.startButton.setEnabled(false);
        Main.gameData.play();
    }

    // let try this 
    public void whenGameStart() {

        fromMute = false;

        if (clip == null) {
            loopMusic(Music);

        }

        //System.out.println("the State is " + State);
    }

    public void beforeMute() {

        if (fromMute == true && isMenuMusicOn == false) {
            System.out.println("CASE 1");
            clip.start();
            isMenuMusicOn = true;
        }

        if (fromMute == false && isMenuMusicOn == false) {
            System.out.println("CASE 2");
            State = STATE.MUTEMUSIC;

        } else if (fromMute == false && isMenuMusicOn == true) {
            clip.start();
        }

    }

    public void nowmute() {
        clip.stop();
        isMenuMusicOn = false;
        fromMute = true;

    }

    public void newnowmute() {

        setMusicMute(true);
        clip.stop();
        isMenuMusicOn = false;
        fromMute = true;

    }

    public void unmutemusic() {

        //  setMusicMute(false);
        clip.start();

    }

    //***set music 
    public void setSoundMute(boolean mute) {
        isSoundOn = !mute;
    }

    public void setMusicMute(boolean mute) {
        isMusicOn = !mute;
        if (mute && clip != null) {
            clip.stop();
            System.out.println("from mute sound");
        } else {
            if (clip != null) {
                clip.stop();
            }
            if (State == STATE.MENU) {
                loopMusic(GameMusic);
            } else {
                loopMusic(Music);
            }
        }
    }

    public boolean isMusicOn() {
        return isMusicOn;
    }
    // *** set music end****************************************************************************************************************************

    //*** play music  changed to static 
    public static void playMusic(File Sound) {
        if (State == GamePanel.STATE.GAME) {
            try {
                Clip soundClip = AudioSystem.getClip();
                soundClip.open(AudioSystem.getAudioInputStream(Sound));
                soundClip.start();

            } catch (Exception e) {

            }
        }
    }

    public void loopMusic(File loopSound) {

        if (isMusicOn) {
            try {
                clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(loopSound));
                clip.loop(clip.LOOP_CONTINUOUSLY);
                //Thread.sleep(clip.getMicrosecondLength()/1000);

            } catch (Exception e) {

            }
        }
    }
    //***Play music end*****************************************************************************************************************************

    public void setGameStarted(boolean x) {
        checkgameStartedbkg = true;
    }

    public void setLevelBackgroundImage(String x) throws IOException {
        startedImage = ImageIO.read(getClass().getResource("/model/assets/" + x + ".png"));
    }
    
    public void setAccessedStore(boolean x){
        accessedStore = x;
    }
}
