package model;

public class GameFigureState {

    /* common to all game figures */
    public static final int STATE_DONE = 0;
    public static final int STATE_ALIVE = 1;
    public static final int STATE_DYING = 2;

    /* shooter states */
    public static final int SHOOTER_STATE_HEALTH_LEVEL_5 = 30;
    public static final int SHOOTER_STATE_HEALTH_LEVEL_4 = 31; // not implemented yet
    public static final int SHOOTER_STATE_HEALTH_LEVEL_3 = 32; // not implemented yet
    public static final int SHOOTER_STATE_HEALTH_LEVEL_2 = 33; // not implemented yet
    public static final int SHOOTER_STATE_HEALTH_LEVEL_1 = 34; // not implemented yet
    
    
    
     public static final int BLOCKBONUS_STATE_STARTED = 45;
    public static final int BLOCKBONUS_STATE_DAMAGED = 46;
    public static final int BLOCKBONUS_STATE_HIT = 47;
    
    
    
    

    /* Flying Enemy */
    public static final int FLYING_ENEMY_STATE_MOVING = 200;
    public static final int FLYING_ENEMY_STATE_HIT = 201;
    public static final int FLYING_ENEMY_STATE_DEAD = 202;
    
     /* Charging Enemy */
    public static final int CHARGER_ENEMY_STATE_MOVING = 203;
    public static final int CHARGER_ENEMY_STATE_HIT = 204;
    public static final int CHARGER_ENEMY_STATE_DEAD = 205;
    public static final int CHARGER_ENEMY_STATE_CHARGING = 206;

    /* Laser Enemy */
    public static final int LASER_ENEMY_STATE_SHOOTING = 207;
    public static final int LASER_ENEMY_STATE_HIT = 208;
    public static final int LASER_ENEMY_STATE_DEAD = 209;
    public static final int LASER_ENEMY_STATE_NOTHING = 210;
    
    public static final int LASER_SHOT_STATE_LAUNCHED = 210;
    public static final int LASER_SHOT_STATE_EXPLODED = 211;
    
   
    
    
     /* Bouncing Enemy */
    public static final int BOUNCER_ENEMY_STATE_MOVING = 212;
    public static final int BOUNCER_ENEMY_STATE_HIT = 213;
    public static final int BOUNCER_ENEMY_STATE_DEAD = 214;
    public static final int BOUNCER_ENEMY_STATE_CHARGING = 215;
    
    /* Spiker Enemy */
     public static final int SPIKER_ENEMY_STATE_SHOOTING = 216;
    public static final int SPIKER_ENEMY_STATE_HIT = 217;
    public static final int SPIKER_ENEMY_STATE_DEAD = 218;
    public static final int SPIKER_ENEMY_STATE_NOTHING = 219;
    
     public static final int SPIKER_SHOT_STATE_LAUNCHED = 220;
    public static final int SPIKER_SHOT_STATE_EXPLODED = 221;
    

    
     /* inventory states */
    public static final int INV_EMPTY = 300;
    public static final int INV_FULL = 301;
    
    /* upgrade state */
    public static final int UPGRADE_STATE_ALIVE = 302;
    public static final int UPGRADE_STATE_DEAD = 303;
    
    public static final int TREASURE_STATE_ALIVE = 600;
    public static final int TREASURE_STATE_USED = 601;
    
    public static final int BARREL_STATE_STARTED = 700;
    public static final int BARREL_STATE_DAMAGED = 701;
    public static final int BARREL_STATE_HIT = 702;


    /* digger enemy state */ 
    public static final int DIGGER_ENEMY_STATE_APPEARED = 111;
    public static final int DIGGER_ENEMY_STATE_DAMAGED = 112;
    public static final int DIGGER_ENEMY_STATE_EXPLOSION = 113;
    public static final int DIGGER_ENEMY_STATE_HOLE_OPENING = 114;
    public static final int DIGGER_ENEMY_STATE_HOLE_OPEN  = 115;
    public static final int DIGGER_ENEMY_STATE_HOLE_CLOSING  = 116;
    public static final int DIGGER_ENEMY_STATE_HOLE_CLOSED  = 117;
    
    public static final int DIGGER_SHOT_STATE_LAUNCHED = 118;
    public static final int DIGGER_SHOT_STATE_EXPLODED = 119;
    
    public static final int FREEZER_ENEMY_STATE_STARTED = 120;
    public static final int FREEZER_ENEMY_STATE_DAMAGED = 121;
    public static final int FREEZER_ENEMY_STATE_FREEZING = 122;
    
    public static final int BIG_HANDS_BOSS_STARTED = 128;
    public static final int BIG_HANDS_BOSS_APPEARED = 123;
    public static final int BIG_HANDS_BOSS_DAMAGED = 124;
    public static final int BIG_HANDS_BOSS_LOCKED = 127;
    public static final int BIG_HANDS_BOSS_WAITING = 134;

    public static final int BIG_HAND_APPEARED = 125;
    public static final int BIG_HAND_DAMAGED = 126;
    public static final int BIG_HAND_STRETCH_OUT = 129;
    public static final int BIG_HAND_STRETCH_START = 133;
    public static final int BIG_HAND_SLAP_BACK = 130;
    public static final int BIG_HAND_SLAP_FORWARD = 135;
    public static final int BIG_HAND_SMASH_MOVE = 131;
    public static final int BIG_HAND_SMASH_UP = 137;
    public static final int BIG_HAND_SMASH_DOWN = 138;
    public static final int BIG_HAND_SMASH_WAVE = 139;
    public static final int BIG_HAND_RETURN = 136;

    public static final int SPACE_SHIELD_EXPAND = 137;
    public static final int SPACE_SHIELD_DEXPAND = 138;
    
    public static final int SPLITTER_ENEMY_CHASING = 139;
    public static final int SPLITTER_ENEMY_SPLITTING = 140;
    public static final int SPLITTER_ENEMY_SEPERATING = 141;
    public static final int SPLITTER_ENEMY_DAMAGED = 142;
    
    public static final int SPIKE_TRAP_STATE_DOWN = 143;
    public static final int SPIKE_TRAP_STATE_UP = 144;
    public static final int SPIKE_TRAP_STATE_DESTROYED = 145;
    public static final int TRAILER_ENEMY_STATE_MOVING = 146;
    public static final int TRAILER_ENEMY_STATE_DAMAGED = 147;
    
    public static final int TRAIL_STATE_DROPPED = 148;
    public static final int TRAIL_STATE_FADING = 149;
    
    public static final int EXPLOSIVE_TRAP_STATE_UNTRIGGERED = 150;
    public static final int EXPLOSIVE_TRAP_STATE_TRIGGERED = 151;
    

}

