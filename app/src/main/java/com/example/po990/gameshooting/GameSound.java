package com.example.po990.gameshooting;

import android.content.Context;
import android.media.MediaActionSound;
import android.media.MediaPlayer;

/**
 * Created by po990 on 2015-11-30.
 */
public class GameSound extends Thread {

    MediaPlayer mp;

    public GameSound(MediaPlayer mp){
        this.mp = mp;
    }

    public void run(){
        mp.start();
    }

    public void stopSound(){
        mp.stop();
    }

    public void runSound(){
        mp.start();
    }
}
