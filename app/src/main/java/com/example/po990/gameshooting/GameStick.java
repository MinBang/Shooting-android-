package com.example.po990.gameshooting;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by po990 on 2015-11-30.
 */
public class GameStick extends GameObject {

    int state = GameResource.STATE_LOAD;
    int drawState = 0;
    Paint p;

    public GameStick(GameResource resource, int type) {
        super(resource, type);

        p = new Paint();
        p.setAlpha(150);
    }

    public void draw(Canvas canvas){


        if(drawState == 1){

            canvas.drawBitmap(resource.getImage(type), x - (width/2), y - (height/2), p);
        }
    }

    public int getAngle(){
        int mx = x - 50;
        int my = y - 400;

        mx -= 150;
        my -= 150;

        double rad=Math.atan2(mx,my);
        int degree=(int)((rad*180)/Math.PI);

        return (degree+180);
    }
}
