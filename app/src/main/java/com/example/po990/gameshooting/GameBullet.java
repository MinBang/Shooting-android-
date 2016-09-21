package com.example.po990.gameshooting;

/**
 * Created by po990 on 2015-11-27.
 */
public class GameBullet extends GameObject {

    int degree;

    public GameBullet(GameResource resource, int type, int degree) {
        super(resource, type);

        setSize(30, 30);

        this.degree = degree;
        if(type == GameResource.BULLET0){
            speed = 18;
        }else{
            speed = 8;
        }
    }

    public void move(){
        x -= (speed*Math.sin(Math.toRadians(degree)));
        y -= (speed*Math.cos(Math.toRadians(degree)));
    }
}
