package com.example.po990.gameshooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by po990 on 2015-11-27.
 */
public class GameItem extends GameObject {

    int reX;
    int motion = 0;
    int state = 0;

    Bitmap image;

    public GameItem(GameResource resource, int type) {
        super(resource, type);
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;

        speed = 9;

        reX = x + 300;if(resource.screenWidth - width < reX){
            reX = resource.screenHeight - width;
        }
    }


    public void changeMotion(){
        motion = (motion+1)%7;
    }

    public void move(){
        if(x < reX && state == 0){
            x += speed;
            if(reX <= x){
                state = 1;
            }
        }else{
            x -= speed;
        }
    }

    public void draw(Canvas canvas){
        int sx = (motion * width);

        image = Bitmap.createBitmap(resource.getImage(type), sx, 0, width, height);
        canvas.drawBitmap(image, x, y, null);
    }
}
