package com.example.po990.gameshooting;

/**
 * Created by po990 on 2015-11-27.
 */
public class GamePlayer extends GameObject {

    int life = 0;
    int state = 0;
    int bulCnt = 0;
    int degree = 0;

    public GamePlayer(GameResource resource, int type) {
        super(resource, type);

        init();
    }

    public void init(){
        this.speed = 12;
        this.type = GameResource.PLAYER_HOLDON;

        life = 5;
        bulCnt = 15;
        state = GameResource.STATE_LOAD;

        //setSize(70, 70);
    }

    public void move(){
        x -= (speed*Math.sin(Math.toRadians(degree)));
        y -= (speed*Math.cos(Math.toRadians(degree)));
    }

    public void decreaseBulCnt(){
        if(4 < bulCnt){
            bulCnt--;
        }
    }

    public void decreaseLife(){
        if(0 < life){
            life--;
        }

        if(bulCnt < 13){
            bulCnt += 2;
        }
        state = GameResource.STATE_ILL;
        type = GameResource.PLAYER_ILL;
    }
}
