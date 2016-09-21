package com.example.po990.gameshooting;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by po990 on 2015-11-30.
 */
public class GameStickBG extends GameObject {

    public GameStickBG(GameResource resource, int type) {
        super(resource, type);
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setAlpha(150);

        canvas.drawBitmap(resource.getImage(GameResource.JOY_BG), 50, 400, p);
    }



}
