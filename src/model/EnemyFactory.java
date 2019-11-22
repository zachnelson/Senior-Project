package model;

import controller.Main;

/*
The factory pattern here allows the creation of reugual enemies if you want to add a new enemy first 
add a new enemy int type to enemyfigures and then add them in the case-switch  statement in makeEnemyFigure
if it is an actual enemy charater then give
\when you add one enemy you make one call to addEnemy in enemy factory.  if for
example you add a barrel you do call this, nor do you call remove enemy when it 
is destroyed.  I made them separate functions so that we could control that 
number manually.
 */
/**
 *
 * @author cardy
 */
public class EnemyFactory {

    private static final EnemyFactory ENEMYFACTORY = new EnemyFactory();
    private int totalEnemyCharaters;

    //DO NOT CHANGE THIS TO PUBLIC use getInstance() instead
    public void addEnemyCharater() {
        totalEnemyCharaters++;
    }

    private EnemyFactory() {

    }

    //add enemies based on which one is needed.
    public EnemyFigure makeEnemyFigure(int figure) {
        EnemyFigure enemyFigure = null;
        switch (figure) {
            case EnemyFigure.CHARGER:
                enemyFigure = new ChargerEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.DIGGER:
                enemyFigure = new DiggerEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.FLYING:
                enemyFigure = new FlyingEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.FREEZER:
                enemyFigure = new FreezerEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.SPLITTER:
                enemyFigure = new SplitterEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))), 0, 0);
                addEnemyCharater();
                break;
            case EnemyFigure.BIGHANDSBOSS:
                enemyFigure = new BigHandsBoss((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 300))), -200);
                addEnemyCharater();
                break;
            case EnemyFigure.LASER:
                enemyFigure = new LaserEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 150))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 200))));
                addEnemyCharater();
                break;
             case EnemyFigure.SPIKER:
                enemyFigure = new SpikerEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
         
            case EnemyFigure.BOUNCER:
                enemyFigure = new BouncerEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.BARREL:
                enemyFigure = new Barrel((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.SPIKETRAP:
                enemyFigure = new SpikeTrap((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 150))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 150))));
                addEnemyCharater();
                break;
            case EnemyFigure.TRAILER:
                enemyFigure = new TrailerEnemy((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addEnemyCharater();
                break;
            case EnemyFigure.EXPLOSIVETRAP:
                enemyFigure = new ExplosiveTrap((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 150))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 150))));
                addEnemyCharater();
                break;

            default:
                break;
        }
        return enemyFigure;
    }

    //use this instead of the keywork new
    public static EnemyFactory getInstance() {
        return ENEMYFACTORY;
    }

    public void removeEnemyCharater() {
        if (totalEnemyCharaters > 0) {
            totalEnemyCharaters--;
        } else {
            throw new IllegalArgumentException("\n====> total enemys is = " + (totalEnemyCharaters - 1) + "This is out of bounds.  Error code source: EnemyFactory.java\n");
        }
    }

    public int getTotalEnemyCharaters() {
        return totalEnemyCharaters;
    }

}
