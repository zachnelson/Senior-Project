
package model;

import controller.Main;

public class BonusFactory {
    
    private static final BonusFactory BONUSFACTORY = new BonusFactory();
    private int totalbonusCharaters;
    
    //DO NOT CHANGE THIS TO PUBLIC use getInstance() instead
    private void addBonusCharater() {
        totalbonusCharaters++;
    }


    public BonusFactory() {

    }

    //add enemies based on which one is needed.
    public BonusFigure makeBonusFigure(int figure) {
        BonusFigure bonusFigure = null;
        
        System.out.println("what is the state :::::" + bonusFigure);
        
        switch (figure) {
            case BonusFigure.BLOCK1:      // vanne sprint5 
                bonusFigure = new BonusBlock((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addBonusCharater();
                break;
            case BonusFigure.BLOCK2:      // vanne sprint5 
                bonusFigure = new BonusBlockup((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addBonusCharater();
                break;
            case BonusFigure.BLOCK3:      // vanne sprint5 
                bonusFigure = new BonusBlockup2((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addBonusCharater();
                break;    
            case BonusFigure.BLOCK4:      // vanne sprint5 
                bonusFigure = new BonusBlockup4((int) (Math.floor(Math.random() * (Main.WIN_WIDTH - 100))), (int) (Math.floor(Math.random() * (Main.WIN_HEIGHT - 100))));
                addBonusCharater();
                break;
            default:
                break;
        }
        
        System.out.println("what is the state after :::::" + bonusFigure);
        
        return bonusFigure;
    }
   
    //use this instead of the keywork new
    public static BonusFactory getInstance() {
        return BONUSFACTORY;
    }

    public void removeBonusCharater() {
        if (totalbonusCharaters > 0) {
            totalbonusCharaters--;
        } else {
            throw new IllegalArgumentException("\n====> total enemys is = " + (totalbonusCharaters - 1) + "This is out of bounds.  Error code source: EnemyFactory.java\n");
        }
    }

    public int getTotalBonusCharaters() {
        return totalbonusCharaters;
    }

    
    
    
    
}
