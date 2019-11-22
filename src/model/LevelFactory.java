package model;

import controller.Main;
import java.io.IOException;
import static model.GameData.bonusFigures;
import static model.GameData.enemyFigures;
import view.GamePanel;

/**
 *
 * @author Nathan
 */
public class LevelFactory {

    private static final LevelFactory LEVEL_FACTORY = new LevelFactory();
    private final GameData data = Main.gameData;
    private final EnemyFactory enemyFactory = Main.gameData.getEnemyFacotry();
    private final BonusFactory bonusFactory = Main.gameData.getBonusFacotry();
    private final GameData gameData = Main.gameData;

    private LevelFactory() {

    }

    public boolean makeLevel(int level, boolean levelBuilt) throws IOException {
        // checks ot see if there are no enemyFigures and verifies we haven't lost
        if (enemyFigures.isEmpty() && !GameData.getShooter().lost()) {
            System.out.println("current level is " + level);

            //so if you want to create a level add a 
            if (level == 1) {
                gameData.levelUp();

                // System.out.println("how many enemies first level " + enemyFigures.size());
                // enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.EXPLOSIVETRAP));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.FLYING));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.TRAILER));
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK2));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.EXPLOSIVETRAP));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.FLYING));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.TRAILER));

                GamePanel.Level += 1;
                gameData.setDisplayLevel(GamePanel.Level - 1);
                //System.out.println("the first level : " + GamePanel.Level);
                gameData.initInventory();
                return true;
            } else if (level == 2) {
                gameData.levelUp();
                //this.characterSheet.activateCharaterSheet();
                // System.out.println("how many enemies segond level " + enemyFigures.size());
                // System.out.println(" after clear enemeies number of enemeies :  " + enemyFigures.size());
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.FREEZER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPIKETRAP));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.EXPLOSIVETRAP));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.FREEZER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPIKETRAP));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.EXPLOSIVETRAP));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                gameData.initInventory();
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK3));
                GamePanel.Level += 1;
                //System.out.println("the segong level  : " + displayLevel);
                return true;
            } else if (level == 3) {
                gameData.levelUp();                //  System.out.println("how many enemies segond level " + enemyFigures.size());
                //  System.out.println(" after clear enemeies number of enemeies :  " + enemyFigures.size());
                //enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.FREEZER));
                gameData.initInventory();
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK4));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.CHARGER));

                GamePanel.Level += 1;

                //System.out.println("the segong level  : " + displayLevel);
                return true;
            } 
            else if(level == 4){    // Enter Store 
                gameData.clearOutRoom();
                Store store = new Store();
                store.init();
                return true;
            }
            else if (level == 5) {
                gameData.levelUp();                //   System.out.println("how many enemies segond level " + enemyFigures.size());
                gameData.initInventory();
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK1));
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK3));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.LASER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.LASER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.LASER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.LASER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.LASER));

                GamePanel.Level += 1;
                return true;
            } else if (level == 6) {
                gameData.levelUp();
                gameData.initInventory();
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK2));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.DIGGER));

                GamePanel.Level += 1;
                return true;
            } else if (level == 7) {
                gameData.levelUp();
                gameData.initInventory();
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK3));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPLITTER));

                GamePanel.Level += 1;
                return true;
            } else if (level == 100) {
                gameData.levelUp();
                gameData.initInventory();
                bonusFigures.add(bonusFactory.makeBonusFigure(BonusFigure.BLOCK4));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.BIGHANDSBOSS));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPIKER));
                enemyFigures.add(enemyFactory.makeEnemyFigure(EnemyFigure.SPIKER));
                GamePanel.Level += 1;
                return true;
            } else if (level == 101) {      //***********new added by vanne 
                gameData.setWin(true);
                gameData.pause();
                return true;
            }
        } else if (GameData.getShooter().lost()) {
            gameData.pause();
            gameData.getGameState().goNext(gameData);
            return false;
        } else {
            System.out.println("CLEARLY DIDN'T WIN OR LOOSE YOU SUCK THATS ALL");
            return false;
        }
        return false;

    }

    public static LevelFactory getInstance() {
        return LEVEL_FACTORY;
    }
}

