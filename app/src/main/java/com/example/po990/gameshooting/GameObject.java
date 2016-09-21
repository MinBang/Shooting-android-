package com.example.po990.gameshooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by po990 on 2015-11-27.
 */
public class GameObject {
    int x , y;
    int width, height;
    int type;
    int speed;

    GameResource resource;

    public GameObject(GameResource resource, int type){
        this.resource = resource;
        this.type = type;

        x = 0;
        y = 0;
        width = 0;
        height = 0;
        this.type = type;
        speed = 0;
    }

    public void setLocation(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;

        //Bitmap.createScaledBitmap(resource.getImage(type), width, height, true);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(resource.getImage(type), x, y, null);
    }
}
