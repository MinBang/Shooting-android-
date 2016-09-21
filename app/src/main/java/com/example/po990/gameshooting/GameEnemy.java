package com.example.po990.gameshooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by po990 on 2015-11-27.
 */
public class GameEnemy extends GameObject {

    int degree = 0;
    int motion = 0;
    int state = 0;
    int life = 0;
    int stage = 0;
    int mode = 0;

    Bitmap image;

    public GameEnemy(GameResource resource, int type, int degree, int stage) {
        super(resource, type);

        this.degree = degree;
        this.stage = stage;

        state = GameResource.STATE_LOAD;

        switch(type){
            case GameResource.ENEMY_MINI:
                speed = 6;
                life = 3 + (stage * 2);

                break;
            case GameResource.ENEMY_MIDDLE:
                speed = 9;
                life = 5 + (stage * 8);

                break;
            case GameResource.ENEMY_KING:
                speed = 6;
                life = 100 + (stage * 50);

                break;
         }
    }

    public void move(){
        if(GameResource.ENEMY_KING == type){
            if(0 == mode){
                if(x < 800){
                    mode = 1;
                }

                x -= speed;
            }else if(1 == mode){
                if(y < 30){
                    mode = 2;
                }
                y -= speed;
            }else if(2 == mode) {
                if(resource.screenHeight - 30 < y + height){
                    mode = 1;
                }

                y += speed;
            }
        }else{
            if(0 == mode){
                x -= (speed*Math.sin(Math.toRadians(degree)));
            }
            else{
                x += (speed*Math.sin(Math.toRadians(degree)));
            }
            y -= (speed*Math.cos(Math.toRadians(degree)));
        }
    }

    public void draw(Canvas canvas){

        if(type != GameResource.ENEMY_KING){
            int sx = (motion * width);

            image = Bitmap.createBitmap(resource.getImage(type), sx, 0, width, height);
            canvas.drawBitmap(image, x, y, null);
        }else{
            canvas.drawBitmap(resource.getImage(type), x, y, null);
        }
    }

    public void changeMotion(){
        motion = (motion+1)%7;
    }

    public void decreaseLife(){
        if(0 < life){
            life--;
        }
    }
}
