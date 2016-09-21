package com.example.po990.gameshooting;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by po990 on 2015-11-27.
 */
public class GameExplode extends GameObject {

    int motion = 0;

    Bitmap image = null;

    public GameExplode(GameResource resource, int type) {
        super(resource, type);
    }

    public void draw(Canvas canvas){
        /*int sx = x - ((motion/4) * width);
        int sy = y - ((motion%4) * height);

        gc.setClip(x, y, width , height);
        gc.drawImage(resource.getImage(type), sx, sy, scene);

        gc.setClip(0 , 0, GameResource.gScreenWidth, GameResource.gScreenHeight);*/

        int sx = (motion/4) * width;
        int sy = (motion%4) * height;

        image = Bitmap.createBitmap(resource.getImage(type), sx, sy, width, height);
        canvas.drawBitmap(image, x, y, null);
    }

    public void changeMotion(){
        if(motion < 15){
            motion++;
        }
    }
}
