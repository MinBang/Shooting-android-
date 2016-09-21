package com.example.po990.gameshooting;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by po990 on 2015-11-26.
 */
public class GameMain extends SurfaceView implements SurfaceHolder.Callback {

    GameThread gThread;
    SurfaceHolder holder;

    public GameMain(Context context) {
        super(context);

        holder = getHolder();
        holder.addCallback(this);

        gThread = new GameThread(context, holder, this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public GameMain(Context context, AttributeSet attrs) {
        super(context, attrs);

        holder = getHolder();
        holder.addCallback(this);

        gThread = new GameThread(context, holder, this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        gThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        int lx = (int)event.getX();
        int ly = (int)event.getY();

        if(gThread.gameStage == GameResource.STATUS_TITLE) {
            gThread.gameStage = GameResource.STATUS_STARTGAME;

        }else if (gThread.gameStage == GameResource.STATUS_RUNNINGAME) {
            switch(action){
                case MotionEvent.ACTION_DOWN:
                    //gThread.stick.state = 1;
                    gThread.stick.setLocation(lx, ly);

                    if(gThread.checkCollision(gThread.stickBG, gThread.stick)){
                        gThread.stick.state = GameResource.STATE_RUN;
                        gThread.stick.drawState = 1;

                        if(gThread.player.state == GameResource.STATE_RUN) {
                            gThread.player.degree = gThread.stick.getAngle();
                            gThread.player.type = GameResource.PLAYER_FIRE;
                        }
                        //Log.d("xxx : ", "들어옴");
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    //gThread.player.x = lx;
                    //gThread.player.y = ly;
                    makeBullet();

                    //gThread.stick.setLocation(lx, ly);
                    if(gThread.player.type != GameResource.PLAYER_ILL){

                        if(!gThread.checkCollision(gThread.stickBG, gThread.stick)){
                            gThread.stick.drawState = 0;
                        }else{
                            gThread.stick.drawState = 1;
                        }

                        if(gThread.player.type != GameResource.PLAYER_ILL){
                            gThread.stick.setLocation(lx, ly);
                            gThread.player.degree = gThread.stick.getAngle();
                            gThread.player.type = GameResource.PLAYER_FIRE;
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    gThread.stick.state = GameResource.STATE_LOAD;
                    gThread.player.type = GameResource.PLAYER_HOLDON;
                    gThread.stick.drawState = 0;

                    break;
                default:
                    break;
            }
        }

        return true;
        //return super.onTouchEvent(event);
    }

    public void makeBullet(){

        int bWidth = gThread.resource.getImage(GameResource.BULLET0).getWidth();
        int bHeight = gThread.resource.getImage(GameResource.BULLET0).getHeight();

        if(gThread.player.state == GameResource.STATE_RUN || gThread.player.state == GameResource.STATE_INVINCIBLE){
            if((gThread.cnt % gThread.player.bulCnt) == 0){
                GameBullet tBullet = new GameBullet(gThread.resource, GameResource.BULLET0, gThread.randomRange(245, 265));
                tBullet.setSize(bWidth, bHeight);
                tBullet.setLocation(gThread.player.x + 80, gThread.player.y + 40);

                gThread.vBullets.add(tBullet);

                tBullet = new GameBullet(gThread.resource, GameResource.BULLET0, gThread.randomRange(268, 272));
                tBullet.setSize(bWidth, bHeight);
                tBullet.setLocation(gThread.player.x + 80, gThread.player.y + 40);

                gThread.vBullets.add(tBullet);

                tBullet = new GameBullet(gThread.resource, GameResource.BULLET0, gThread.randomRange(275, 295));
                tBullet.setSize(bWidth, bHeight);
                tBullet.setLocation(gThread.player.x + 80, gThread.player.y + 40);

                gThread.vBullets.add(tBullet);

                //Log.d("xxx : ", "" + gThread.vBullets.size());
            }
        }
    }
}
