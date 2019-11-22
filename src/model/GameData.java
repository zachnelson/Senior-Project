package model;

import controller.Main;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import view.CharaterSheet;
import view.GamePanel;
import view.MainWindow;
import view.StatsDisplay;

public final class GameData {

    private final SettingsData settingsData;

    // private final List<GameFigure> enemyFigures;
    public static List<GameFigure> enemyFigures;
    private final List<GameFigure> friendFigures;
    //private final List<GameFigure> inventoryFigures;
    public static List<GameFigure> inventoryFigures;
    public static List<GameFigure> weaponInventory;
    // private final List<GameFigure> miscFigures;
    public static List<GameFigure> miscFigures;

    private static Shooter shooter;
    public static boolean characterPicked;

    public static Store store = new Store();
    private static GameFigure inventory;
    private static GameFigure inventory2;

    /*
    private GameFigure upgrade;
    private GameFigure upgrade2;
    private GameFigure upgrade3;
    private GameFigure upgrade4;
     */
    public static GameFigure upgrade;
    public static GameFigure upgrade2;
    public static GameFigure upgrade3;
    public static GameFigure upgrade4;

    private static GameFigure mainWeapon;
    private static GameFigure secondaryWeapon;
    private final Random rand;
    private final StatsDisplay statsDisplay;
    private final CharaterSheet characterSheet;
    private int enemiesRemaining;
    private static EnemyFactory enemyFactory;

    //game states
    private GameState gameState;

    //**********vanne 
    private int score, displayLevel;

    private static GameFigure chargerFigure;

    private boolean gameStarted = false;

    // let try 
    private Graphics2D g2;
    private static Image dbImage = null;
    private boolean win;
    private boolean running;

    public static List<GameFigure> bonusFigures;
    private static BonusFactory bonusFactory;
    private final int totalLevels;
    private boolean created;

    public GameData() {

//total levels has to be decided here so that it doesn't keep changing
        final int MIN_LEVELS = 4;
        final int MAX_LEVELS = 6;
        totalLevels = randomNumberGenerator(MIN_LEVELS, MAX_LEVELS);

        characterPicked = false;

        bonusFactory = BonusFactory.getInstance();
        bonusFigures = new CopyOnWriteArrayList<>();

        win = false;
        running = true;
        enemyFactory = EnemyFactory.getInstance();
        this.rand = new Random();
        this.score = 0;
        enemyFigures = new CopyOnWriteArrayList<>();
        friendFigures = new CopyOnWriteArrayList<>();
        inventoryFigures = new CopyOnWriteArrayList<>();
        miscFigures = new CopyOnWriteArrayList<>();
        weaponInventory = new CopyOnWriteArrayList<>();

        //gets the singleton patterned class shooter and then
        shooter = Shooter.getInstance();
        this.statsDisplay = new StatsDisplay(shooter); //binds subject and observer
        this.characterSheet = new CharaterSheet(shooter); //binds subject and observer
        System.out.println("intializes game state");
        this.gameState = new GameStatePaused(shooter);  //both determines intial state and binds subject to observer

        settingsData = new SettingsData();

        //************vanne
        displayLevel = 0;

        initGame();
    }

    public void clearGame() {
        enemyFigures.clear();
        friendFigures.clear();
        inventoryFigures.clear();
        miscFigures.clear();
        bonusFigures.clear();
        weaponInventory.clear();
        setCharacterPicked(false);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score += score;
    }

    public void initGame2() {

        friendFigures.add(0, shooter);
        shooter.setCurrentAgility(5);
        shooter.setMaxHealth(10);
        shooter.setCurrentHealth(10);
        shooter.setArmorPercent(0);
        shooter.setSpeedBuff(1);

        // Set the mainWeapon to a pistol to begin with.
        // This will be used by the WeaponFactory to create 
        // instances of the weapons.
        mainWeapon = new Pistol(0, 0, 0, 0);
//        secondaryWeapon = new Laser(0, 0, 0, 0);

        // Places icons on screen for inventory
        inventory = new Inventory(25, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190);
        inventory2 = new Inventory(80, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190);

        inventoryFigures.add(inventory);
        inventoryFigures.add(inventory2);

    }

    public void initGame() {

        // GamePanel.width, height are known when rendered. 
        // Thus, at this moment,
        // we cannot use GamePanel.width and height.
        Main.animator.setTestOpen(false);

        friendFigures.add(0, shooter);
        shooter.setCurrentAgility(5);
        shooter.setMaxHealth(10);
        shooter.setCurrentHealth(10);
        shooter.setArmorPercent(0);
        shooter.setSpeedBuff(1);

        friendFigures.add(0, shooter);

        /*
        upgrade = new Upgrade(rand.nextInt(1100) + 1, rand.nextInt(500) + 1, 1);
        upgrade2 = new Upgrade(rand.nextInt(1100) + 1, rand.nextInt(500) + 1, 2);
        upgrade3 = new Upgrade(rand.nextInt(1100) + 1, rand.nextInt(500) + 1, 3);
        upgrade4 = new Upgrade(rand.nextInt(1100) + 1, rand.nextInt(500) + 1, 4);
        miscFigures.add(upgrade);
        miscFigures.add(upgrade2);
        miscFigures.add(upgrade3);
        miscFigures.add(upgrade4);
         */
        // Set the mainWeapon to a pistol to begin with.
        // This will be used by the WeaponFactory to create 
        // instances of the weapons.
        mainWeapon = new Pistol(0, 0, 0, 0);
//        secondaryWeapon = new Laser(0, 0, 0, 0);

        // Places icons on screen for inventory
        inventory = new Inventory(25, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190);
        inventory2 = new Inventory(80, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190);

        inventoryFigures.add(inventory);
        inventoryFigures.add(inventory2);

        WeaponInventory _weaponInventory = new WeaponInventory(Main.WIN_WIDTH - 125, Main.WIN_HEIGHT - MainWindow.HEIGHT - 250);
        weaponInventory.add(_weaponInventory);
        //enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
        
     
    }

    public void initInventory() {


        if (!inventoryFigures.isEmpty()) {
            inventoryFigures.clear();
        }
        inventory = new Inventory(25, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190);
        inventory2 = new Inventory(80, Main.WIN_HEIGHT - MainWindow.HEIGHT - 190);
        inventoryFigures.add(inventory);
        inventoryFigures.add(inventory2);


    }

    public void addTreasure() {

        if (score % 3 == 0 && score > 0) {

            if (!created) {
                GameFigure treasure = new Treasure((float) Math.floor(Math.random()
                        * (Main.WIN_HEIGHT - 100)), (float) Math.floor(Math.random()
                                * (Main.WIN_HEIGHT - 100)), (int) (Math.random() * 5) + 1);
                List<GameFigure> mfigures = Main.gameData.getMiscFigures();
                mfigures.add(treasure);
                created = true;
            }
        } else if (score % 3 != 0 && score > 0) {
            created = false;
        }
    }

    public int randomNumberGenerator(int min, int max) {
        if (max >= min) {
            int random = (int) (Math.random() * max + min);
            return random;
        } else {
            System.out.println("maxRange must be greater then or = to min Range");
            return 0;
        }
    }

    public void addEnemies(int level) throws IOException {
        LevelFactory levelFactory = LevelFactory.getInstance();
        final int MAX_LEVELS_TYPES = 6;// maxium levels -1 to not include boss level
        final int FINAL_LEVEL = 100; //boss level
        final int MIN_LEVELS_TYPES = 1;// minumium total levels
        boolean levelBuilt = false; //this prevents the animator from creating
        // new random numbers constanly and causing race conditions 
        int counter = 0; //for the while loop
//the animator loops this
        if (enemyFigures.isEmpty() && !GameData.getShooter().lost()) { //verifies tthat all enemies diead and the shooter hasn't lost
            counter = GamePanel.Level;
            int currentLevel;
            if (counter == 4) {
                currentLevel = 4;
            } else {
                currentLevel = this.randomNumberGenerator(MIN_LEVELS_TYPES, MAX_LEVELS_TYPES);
                //currentLevel = 1; for testing
                while (currentLevel == 4) {
                    currentLevel = this.randomNumberGenerator(MIN_LEVELS_TYPES, MAX_LEVELS_TYPES);
                }
            }
            System.out.println("counter is: " + counter + " || total levels: " + this.totalLevels + " || current level: " + currentLevel);
            if (counter == this.totalLevels + 1) {
                levelBuilt = levelFactory.makeLevel(FINAL_LEVEL, levelBuilt); //BOSS HANDS LEVEL
            } else if (counter == this.totalLevels + 2) {
                levelBuilt = levelFactory.makeLevel(FINAL_LEVEL + 1, levelBuilt); //WIN GAME LEVEL}            
            } else {
                levelBuilt = levelFactory.makeLevel(currentLevel, levelBuilt);
            }

        }

    }

    public boolean getWin() {
        return this.win;
    }

    public void update() {

        addTreasure();

        // no enemy is removed in the program
        // since collision detection is not implemented yet.
        // However, if collision detected, simply set
        // f.state = GameFigure.STATE_DONE
        ArrayList<GameFigure> removeEnemies = new ArrayList<>();
        GameFigure f;
        for (int i = 0; i < enemyFigures.size(); i++) {
            f = enemyFigures.get(i);
            if (f.state == GameFigureState.STATE_DONE) {
                removeEnemies.add(f);
            }
        }
        enemyFigures.removeAll(removeEnemies);

        enemyFigures.forEach((g) -> {
            g.update();
        });

        //***************vanne 
        ArrayList<GameFigure> removeBonus = new ArrayList<>();
        GameFigure f1;
        for (int i = 0; i < bonusFigures.size(); i++) {
            f1 = bonusFigures.get(i);
            if (f1.state == GameFigureState.STATE_DONE) {
                removeBonus.add(f1);
            }
        }
        bonusFigures.removeAll(removeBonus);

        bonusFigures.forEach((g) -> {
            g.update();
        });

        //******************
        enemiesRemaining = enemyFigures.size();
        Main.game.setEnemyLabel();

        //***********vanne 
        displayLevel = GamePanel.Level - 1;
        Main.game.setLevelLabel();

        // missiles are removed if explosion is done
        ArrayList<GameFigure> removeFriends = new ArrayList<>();
        for (int i = 0; i < friendFigures.size(); i++) {
            f = friendFigures.get(i);
            if (f.state == GameFigureState.STATE_DONE) {
                removeFriends.add(f);
            }
        }
        friendFigures.removeAll(removeFriends);

        friendFigures.forEach((g) -> {
            g.update();
        });

        miscFigures.forEach((g) -> {
            g.update();
        });

        friendFigures.forEach((g) -> {
            g.update();
        });
        inventoryFigures.forEach((g) -> {
            g.update();
        });

        weaponInventory.forEach((g) -> {
            g.update();
        });

//                        if (enemyFigures.size() == 0 && !this.characterSheet.loseCondition()) {
//                            this.characterSheet.activateCharaterSheet();
//                        }
    }

    public static GameFigure getMainWeapon() {
        return mainWeapon;
    }

    public static void setMainWeapon(GameFigure mainWeapon) {
        GameData.mainWeapon = mainWeapon;
    }

    public static GameFigure getSecondaryWeapon() {
        return secondaryWeapon;
    }

    public static void setSecondaryWeapon(GameFigure secondaryWeapon) {
        GameData.secondaryWeapon = secondaryWeapon;
    }

    public StatsDisplay getStatDisplay() {
        return statsDisplay;
    }

    public CharaterSheet getCharaterSheet() {
        return characterSheet;
    }

    public boolean allEnemiesDead() {
        if (gameStarted) {
            return (enemyFigures.size() <= 0);
        }
        return false;
    }

    public static Shooter getShooter() {
        return shooter;
    }

    public static void setShooter(Shooter shooter) {
        GameData.shooter = shooter;
    }

    public static GameFigure getInventory() {
        return inventory;
    }

    public static void setInventory(GameFigure inventory) {
        GameData.inventory = inventory;
    }

    public static GameFigure getInventory2() {
        return inventory2;
    }

    public static void setInventory2(GameFigure inventory2) {
        GameData.inventory2 = inventory2;
    }

    public GameFigure getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(GameFigure upgrade) {
        this.upgrade = upgrade;
    }

    public GameFigure getUpgrade2() {
        return upgrade2;
    }

    public void setUpgrade2(GameFigure upgrade2) {
        this.upgrade2 = upgrade2;
    }

    public GameFigure getUpgrade3() {
        return upgrade3;
    }

    public void setUpgrade3(GameFigure upgrade3) {
        this.upgrade3 = upgrade3;
    }

    public int getEnemiesRemaining() {
        return enemyFactory.getTotalEnemyCharaters();
    }

    public static GameFigure getChargerFigure() {
        return chargerFigure;
    }

    public static void setChargerFigure(GameFigure chargerFigure) {
        GameData.chargerFigure = chargerFigure;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public SettingsData getSettingsData() {
        return settingsData;
    }

    public List<GameFigure> getEnemyFigures() {
        return enemyFigures;
    }

    public List<GameFigure> getFriendFigures() {
        return friendFigures;
    }

    public List<GameFigure> getInventoryFigures() {
        return inventoryFigures;
    }

    public List<GameFigure> getMiscFigures() {
        return miscFigures;
    }

    public List<GameFigure> getWeaponInventory() {
        return weaponInventory;
    }

    public Random getRand() {
        return rand;
    }

    public StatsDisplay getStatsDisplay() {
        return statsDisplay;
    }

    public CharaterSheet getCharacterSheet() {
        return characterSheet;
    }

    public void clearOutRoom() {
        enemyFigures.clear();
        bonusFigures.clear();
        miscFigures.clear();
    }

    public EnemyFactory getEnemyFacotry() {
        return GameData.enemyFactory;
    }

    public int getLevel() {
        return displayLevel;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean getRunning() {
        return this.running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void levelUp() {

        if (!this.characterSheet.isOpen() && !this.lost()) {
            this.characterSheet.setOpen(true);
            pause();
            boolean levelUp = true;
            this.characterSheet.activateCharaterSheet(levelUp);
        }
    }

    //if you just want ot check to see if it is paused use this
    public boolean paused() {
        return gameState.getClass() == (new GameStatePaused(Shooter.getInstance()).getClass());
    }

    //if you want it to play use this
    public void play() {
        if (paused()) {
            Shooter.getInstance().getDirections()[KeyEvent.VK_W] = true;
            Shooter.getInstance().getDirections()[KeyEvent.VK_A] = true;
            Shooter.getInstance().getDirections()[KeyEvent.VK_S] = true;
            Shooter.getInstance().getDirections()[KeyEvent.VK_D] = true;
            gameState.goNext(this);
        }
    }

    //if you want it to pause use this
    public void pause() {
        if (!paused()) {
            Shooter.getInstance().getDirections()[KeyEvent.VK_W] = false;
            Shooter.getInstance().getDirections()[KeyEvent.VK_A] = false;
            Shooter.getInstance().getDirections()[KeyEvent.VK_S] = false;
            Shooter.getInstance().getDirections()[KeyEvent.VK_D] = false;
            gameState.goNext(this);
        }
    }

    //check to see if you lost
    public boolean lost() {
        return (Main.gameData.getFriendFigures().size() == 0);
    }

    public List<GameFigure> getBonusFigures() {
        return bonusFigures;
    }

    BonusFactory getBonusFacotry() {
        return GameData.bonusFactory;
    }

    public boolean allBonusDead() {
        if (gameStarted) {
            return (bonusFigures.size() <= 0);
        }
        return false;
    }

    public boolean isCharacterPicked() {
        return characterPicked;
    }

    public void setCharacterPicked(boolean charPicked) {
        this.characterPicked = charPicked;
    }

    public static EnemyFactory getEnemyFactory() {
        return enemyFactory;
    }

    public int getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(int displayLevel) {
        this.displayLevel = displayLevel;
    }

    public static BonusFactory getBonusFactory() {
        return bonusFactory;
    }

    void setWin(boolean win) {
        this.win = win;
    }

    public Store getStore() {
        return store;
    }

    
}
