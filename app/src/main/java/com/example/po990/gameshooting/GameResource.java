package com.example.po990.gameshooting;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by po990 on 2015-11-27.
 */
public class GameResource {

    public final static int gDelay = 17;

    // 게임 상태 정의
    public final static int STATUS_TITLE = 0;
    public final static int STATUS_STARTGAME = 1;
    public final static int STATUS_RUNNINGAME = 2;
    public final static int STATUS_STOPGAME = 3;
    public final static int STATUS_GAMEOVER = 4;

    // 캐릭터 상태
    public final static int STATE_LOAD 	= 0;
    public final static int STATE_RUN 	= 1;
    public final static int STATE_ILL 	= 2;
    public final static int STATE_INVINCIBLE = 3;

    // 배경들
    public final static int BACKGROUND = 3;
    public final static int CLOUD1 = 4;
    public final static int CLOUD2 = 5;

    public final static int TITLE = 6;
    public final static int PUSH_TITLE = 7;
    public final static int GAMEOVER = 8;
    public final static int GAMESTART = 9;

    // player resource
    public final static int PLAYER_HOLDON = 11;
    public final static int PLAYER_FORWARD = 12;
    public final static int PLAYER_BACKWARD = 13;
    public final static int PLAYER_FIRE = 14;
    public final static int PLAYER_ILL = 15;
    public final static int PLAYER_HEART = 16;

    // bullet
    public final static int BULLET0 = 21;
    public final static int BULLET1 = 22;
    public final static int BULLET2 = 23;

    // enemy
    public final static int ENEMY_MINI = 31;
    public final static int ENEMY_MIDDLE = 32;
    public final static int ENEMY_KING = 33;
    public final static int ENEMY_BOMB = 34;
    public final static int ENEMY_ITEM1 = 35;

    // ball
    public final static int JOY_BG = 41;
    public final static int JOY_BALL = 42;

    private Map<Integer, Bitmap> imageMap;
    public int screenWidth = 0;
    public int screenHeight = 0;

    GameResource(int width, int height){
        imageMap = new HashMap<Integer, Bitmap>();

        screenWidth = width;
        screenHeight = height;
    }

    public void setImage(int type, Bitmap image){
        imageMap.put(type, image);
    }

    public Bitmap getImage(int type){
        return imageMap.get(type);
    }
}
