package com.example.po990.gameshooting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;

import java.util.Vector;

/**
 * Created by po990 on 2015-11-26.
 */
public class GameThread extends Thread {

    Context context = null;
    SurfaceHolder holder = null;
    Display display = null;
    GameMain gameMain = null;
    GameResource resource = null;

    GameStick stick;
    GameStickBG stickBG;

    MediaPlayer mp;
    GameSound sound;

    // 화면크기
    int screenWidth = 0;
    int screenHeight = 0;

    // 게임상태들
    public int gameStage = 0;
    public int soundStage = 0;
    public int gameLevel = 0;
    boolean gameRun = true;
    int cnt = 0;
    int gCnt = 0;

    public int enemyCnt = 250;
    public int enemyFire = 120;
    public int enemySize = 60;

    // 게임자료구조들
    GameObject bg = null;
    GamePlayer player = null;
    Vector<GameObject> vTitles = null;
    Vector<GameCloud> vClouds = null;
    Vector<GameBullet> vBullets = null;
    Vector<GameEnemy> vEnemys = null;
    Vector<GameItem> vItems = null;
    Vector<GameExplode> vExplodes = null;

    Bitmap test = null;
    Bitmap bgg = null;

    Vibrator vibrator;

    public GameThread(Context context, SurfaceHolder holder, GameMain gameMain) {
        this.context = context;
        this.holder = holder;
        this.gameMain = gameMain;

        display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();

        screenWidth = display.getWidth();
        screenHeight = display.getHeight();

        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
        mp = MediaPlayer.create(context, R.raw.bgm);
        sound = new GameSound(mp);

        sound.start();



        initObjects();
        initImage();
        initGameState();


        stick = new GameStick(resource, GameResource.JOY_BALL);
        stick.setSize(100, 100);
        stick.setLocation(0, 0);
        stickBG = new GameStickBG(resource, GameResource.JOY_BG);
        stickBG.setSize(300, 300);
        stickBG.setLocation(50, 400);
        //test = BitmapFactory.decodeResource(context.getResources(), R.drawable.my_00);
        //test = Bitmap.createScaledBitmap(test, 70, 70, true);

        //bgg = BitmapFactory.decodeResource(context.getResources(), R.drawable.backcloud);
        //bgg = Bitmap.createScaledBitmap(bgg, screenWidth, screenHeight, true);
    }

    public void initObjects(){
        resource = new GameResource(screenWidth, screenHeight);
        bg = new GameObject(resource, GameResource.BACKGROUND);
        player = new GamePlayer(resource, GameResource.PLAYER_HOLDON);

        vTitles = new Vector<GameObject>();
        vClouds = new Vector<GameCloud>();
        vBullets = new Vector<GameBullet>();
        vEnemys = new Vector<GameEnemy>();
        vItems = new Vector<GameItem>();
        vExplodes = new Vector<GameExplode>();
    }

    public void initImage(){

        Bitmap image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.backcloud), screenWidth, screenHeight, true);

        resource.setImage(GameResource.BACKGROUND, image);
        resource.setImage(GameResource.PLAYER_HOLDON, BitmapFactory.decodeResource(context.getResources(), R.drawable.my_00));
        resource.setImage(GameResource.PLAYER_FIRE, BitmapFactory.decodeResource(context.getResources(), R.drawable.my_06));
        resource.setImage(GameResource.PLAYER_ILL, BitmapFactory.decodeResource(context.getResources(), R.drawable.my_08));
        resource.setImage(GameResource.CLOUD1, BitmapFactory.decodeResource(context.getResources(), R.drawable.cloud0));
        resource.setImage(GameResource.CLOUD2, BitmapFactory.decodeResource(context.getResources(), R.drawable.bg_f));
        resource.setImage(GameResource.BULLET0, BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_0));
        resource.setImage(GameResource.BULLET2, BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_2));

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy0), enemySize * 7, enemySize, true);
        resource.setImage(GameResource.ENEMY_MINI, image);
        resource.setImage(GameResource.ENEMY_MIDDLE, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.item0), enemySize * 7, enemySize, true);
        resource.setImage(GameResource.ENEMY_ITEM1, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy1), 350, 350, true);
        resource.setImage(GameResource.ENEMY_KING, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.explode), (enemySize+40) * 4, (enemySize+40) * 4, true);
        resource.setImage(GameResource.ENEMY_BOMB, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.heart), 40, 40, true);
        resource.setImage(GameResource.PLAYER_HEART, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.image_button_bg), 300, 300, true);
        resource.setImage(GameResource.JOY_BG, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.image_button), 100, 100, true);
        resource.setImage(GameResource.JOY_BALL, image);

        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.title1), screenWidth, screenHeight, true);
        resource.setImage(GameResource.TITLE, image);
        resource.setImage(GameResource.PUSH_TITLE, BitmapFactory.decodeResource(context.getResources(), R.drawable.pushspace));
        resource.setImage(GameResource.GAMESTART, BitmapFactory.decodeResource(context.getResources(), R.drawable.start));
        resource.setImage(GameResource.GAMEOVER, BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover));
    }

    public void initGameState(){
        gameStage = 0;
        gameLevel = 0;
        cnt = 0;

        // objects
        GameObject tObject = new GameObject(resource, GameResource.TITLE);
        tObject.setSize(screenWidth, screenHeight);

        vTitles.add(tObject);

        tObject = new GameObject(resource, GameResource.PUSH_TITLE);
        tObject.setSize(resource.getImage(GameResource.PUSH_TITLE).getWidth(), resource.getImage(GameResource.PUSH_TITLE).getHeight());
        tObject.setLocation(250, 410);

        vTitles.add(tObject);

        bg.setSize(screenWidth, screenHeight);

        // players
        player.setLocation(0, 270);
        player.setSize(resource.getImage(GameResource.PLAYER_HOLDON).getWidth(), resource.getImage(GameResource.PLAYER_HOLDON).getHeight());

        // CLOUDS
        for(int i=1;i<4;i++){

            for(int j=0;j<8;j++){
                GameCloud tCloud = new GameCloud(resource, GameResource.CLOUD1);
                tCloud.speed = i * 9;
                tCloud.setSize(resource.getImage(GameResource.CLOUD1).getWidth(), resource.getImage(GameResource.CLOUD1).getHeight());
                tCloud.setLocation(screenWidth/6 * j, 400 + (i*60));

                vClouds.add(tCloud);
            }

            if(i == 2){
                for(int j=0;j<2;j++){
                    GameCloud tCloud = new GameCloud(resource, GameResource.CLOUD2);
                    tCloud.speed = 13;
                    tCloud.setSize(resource.getImage(GameResource.CLOUD2).getWidth(), resource.getImage(GameResource.CLOUD2).getHeight());
                    tCloud.setLocation(tCloud.width * j, 510);

                    vClouds.add(tCloud);
                }
            }
        }
    }

    @Override
    public void run() {

        Canvas canvas = null;

        while(gameRun){

            cnt++;
            if(cnt == 6001){
                cnt = 0;
            }
            canvas = holder.lockCanvas();

            try {
                synchronized (holder){

                    processObject();
                    drawObjects(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    /**
     *  angle
     */

    public static int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }

    public int getAngle(int sx, int sy, int dx, int dy){
        int vx=dx-sx;
        int vy=dy-sy;
        double rad=Math.atan2(vx,vy);
        int degree=(int)((rad*180)/Math.PI);

        return (degree+180);
    }

    public boolean checkMapRange(GameObject object){

        int range = player.width/2;

        if(screenWidth < object.x + 10 || (object.x + object.width) - range < 0){
            return false;
        }else if(screenHeight < object.y + 10 || (object.y + object.height) - range < 0){
            return false;
        }

        return true;
    }

    public boolean checkCollision(GameObject object1, GameObject object2){

        if(object1.x < object2.x && object2.x < (object1.x + object1.width)
                && object1.y < object2.y && object2.y < (object1.y + object1.height)){
            return true;
        }

        if(object1.x < (object2.x + object2.width) && (object2.x + object2.width) < (object1.x + object1.width)
                && object1.y < object2.y && object2.y < (object1.y + object1.height)){
            return true;
        }

        if (object1.x < object2.x && object2.x < (object1.x + object1.width)
                && object1.y < (object2.y + object2.height) && (object2.y + object2.height) < (object1.y + object1.height)) {
            return true;
        }

        if (object1.x < (object2.x + object2.width) && (object2.x + object2.width) < (object1.x + object1.width)
                && object1.y < (object2.y + object2.height) && (object2.y + object2.height) < (object1.y + object1.height)) {
            return true;
        }

        return false;
    }

    /**
     *  process
     */

    public void processObject(){

        switch(gameStage){
            case GameResource.STATUS_TITLE:

                break;
            case GameResource.STATUS_STARTGAME:
                processPlyaer();
                processCloud();
                break;
            case GameResource.STATUS_RUNNINGAME:
                processPlyaer();
                processCloud();
                processBullet();
                processEnemy();
                processItem();
                processCollision();
                processExplode();
                break;
            case GameResource.STATUS_STOPGAME:
                break;
            case GameResource.STATUS_GAMEOVER:
                processCloud();
                processBullet();
                processEnemy();
                processItem();
                processCollision();
                processExplode();
                break;
            default:
                break;
        }
    }

    public void processPlyaer(){

        //player.x += player.speed;
        if(gameStage == GameResource.STATUS_STARTGAME){
            player.setLocation(player.x + player.speed, player.y);

            if(450 <= player.x){
                gameStage = GameResource.STATUS_RUNNINGAME;
                player.state = GameResource.STATE_RUN;
                cnt = 0;

                return;
            }
        }else if(gameStage == GameResource.STATUS_RUNNINGAME){
            if(player.state == GameResource.STATE_ILL){

                if(checkMapRange(player)){
                    player.setLocation(player.x - 1, player.y);
                }

                if(cnt == (gCnt + 50)%6001) {
                    gCnt = cnt;
                    player.state = GameResource.STATE_INVINCIBLE;
                    player.type = GameResource.PLAYER_HOLDON;
                }
            }else if(player.state == GameResource.STATE_INVINCIBLE){
                if(cnt == (gCnt + 120)%6001){
                    player.state = GameResource.STATE_RUN;
                }
            }

            if(player.life <= 0){
                gameStage = GameResource.STATUS_GAMEOVER;
            }

            if(player.type == GameResource.PLAYER_FIRE){
                player.move();
            }
        }
    }

    public void processCloud(){
        for(int i=0;i<vClouds.size();i++){
            GameCloud tCloud = vClouds.get(i);

            tCloud.setLocation(tCloud.x - tCloud.speed, tCloud.y);
            if(tCloud.x + tCloud.width <= -10){
                tCloud.setLocation(screenWidth, tCloud.y);
            }
        }
    }

    public void processBullet(){

        int bWidth = resource.getImage(GameResource.BULLET0).getWidth();
        int bHeight = resource.getImage(GameResource.BULLET0).getHeight();

        for(int i=0;i<vBullets.size();i++){
            GameBullet tBullet = vBullets.get(i);

            tBullet.move();

            if(!checkMapRange(tBullet)){
                vBullets.remove(i);
            }
        }

        // 적군 유도탄 발사
        if(0 == (cnt % enemyFire) || 0 == (cnt % (enemyFire + 3))){
            for (int i = 0; i < vEnemys.size(); i++) {
                GameEnemy tEnemy = vEnemys.get(i);
                GameBullet tBullet = null;

                if (tEnemy.state == GameResource.STATE_RUN && tEnemy.type == GameResource.ENEMY_MINI) {

                    tBullet = new GameBullet(resource, GameResource.BULLET2, getAngle(tEnemy.x, tEnemy.y, player.x + player.width, player.y + player.height));
                    tBullet.setSize(bWidth, bHeight);
                    tBullet.setLocation(tEnemy.x, tEnemy.y + 20);

                    vBullets.add(tBullet);
                }
            }
        }

        if(0 == (cnt % enemyFire) || 0 == (cnt % (enemyFire + 3)) || 0 == (cnt % (enemyFire + 6 )) || 0 == (cnt % (enemyFire + 9))){
            for (int i = 0; i < vEnemys.size(); i++) {
                GameEnemy tEnemy = vEnemys.get(i);
                GameBullet tBullet = null;

                if (tEnemy.state == GameResource.STATE_RUN && tEnemy.type == GameResource.ENEMY_MIDDLE) {

                    tBullet = new GameBullet(resource, GameResource.BULLET2, getAngle(tEnemy.x, tEnemy.y, player.x + player.width, player.y + player.height));
                    tBullet.setSize(bWidth, bHeight);
                    tBullet.setLocation(tEnemy.x, tEnemy.y + 20);

                    vBullets.add(tBullet);
                }
            }
        }

        if(0 == (cnt % enemyFire) || 0 == (cnt % (enemyFire + 3))
                || 0 == (cnt % (enemyFire + 6 )) || 0 == (cnt % (enemyFire + 9))
                || 0 == (cnt % (enemyFire + 12 )) || 0 == (cnt % (enemyFire + 15))
                || 0 == (cnt % (enemyFire + 18 )) || 0 == (cnt % (enemyFire + 21))
                || 0 == (cnt % (enemyFire + 24 )) || 0 == (cnt % (enemyFire + 27))){


            for (int i = 0; i < vEnemys.size(); i++) {
                GameEnemy tEnemy = vEnemys.get(i);
                GameBullet tBullet = null;

                if (tEnemy.state == GameResource.STATE_RUN && tEnemy.type == GameResource.ENEMY_KING) {

                    for(int j=0;j<2;j++){
                        tBullet = new GameBullet(resource, GameResource.BULLET2, ((cnt%enemyFire) + 10) * 2 + (j * 5));
                        tBullet.setSize(bWidth, bHeight);
                        tBullet.setLocation(tEnemy.x, tEnemy.y + tEnemy.height / 2);

                        vBullets.add(tBullet);
                    }
                }
            }
        }
    }

    public void processEnemy(){
        int changeIndex = 400;

        if(0 == (cnt % (enemyCnt * 8))){
            if(gameLevel < 4){
                gameLevel++;
            }

            GameEnemy tEnemy = new GameEnemy(resource, GameResource.ENEMY_KING, 0, gameLevel);
            tEnemy.setSize(350, 350);
            tEnemy.setLocation(screenWidth + 150, screenHeight/2 - tEnemy.height/2);

            vEnemys.add(tEnemy);
        }

        // 적군 생성
        if(0 == (cnt % enemyCnt)){
            GameEnemy tEnemy = new GameEnemy(resource,  GameResource.ENEMY_MINI, randomRange(40, 60), gameLevel);
            tEnemy.setSize(enemySize, enemySize);
            tEnemy.setLocation(screenWidth - 50, screenHeight + tEnemy.height);

            vEnemys.add(tEnemy);

            tEnemy = new GameEnemy(resource, GameResource.ENEMY_MIDDLE, randomRange(60, 120), gameLevel);
            tEnemy.setSize(enemySize, enemySize);
            tEnemy.setLocation(screenWidth + 50, screenHeight / 2 - tEnemy.height);

            vEnemys.add(tEnemy);

            tEnemy = new GameEnemy(resource, GameResource.ENEMY_MINI, randomRange(120, 140), gameLevel);
            tEnemy.setSize(enemySize, enemySize);
            tEnemy.setLocation(screenWidth - 50, 0 - tEnemy.height);

            vEnemys.add(tEnemy);

            for(int i=0;i<gameLevel;i++){
                tEnemy = new GameEnemy(resource, GameResource.ENEMY_MINI, randomRange(120, 140), gameLevel);
                tEnemy.setSize(enemySize, enemySize);
                tEnemy.setLocation(screenWidth - 70 + (i*10), 0 - tEnemy.height);

                vEnemys.add(tEnemy);
            }
        }

        if(0 == (cnt % (enemyCnt * 3))){

            int t = randomRange(0, 2);
            GameEnemy tEnemy = null;

            switch(t){
                case 0:
                    tEnemy = new GameEnemy(resource, GameResource.ENEMY_MIDDLE, randomRange(40, 60), gameLevel);
                    tEnemy.setSize(enemySize, enemySize);
                    tEnemy.setLocation(screenWidth - 150, screenHeight + tEnemy.height);

                    break;
                case 1:
                    tEnemy = new GameEnemy(resource, GameResource.ENEMY_MIDDLE, randomRange(60, 120), gameLevel);
                    tEnemy.setSize(enemySize, enemySize);
                    tEnemy.setLocation(screenWidth + 150, screenHeight / 2 - tEnemy.height);

                    break;
                case 2:
                    tEnemy = new GameEnemy(resource, GameResource.ENEMY_MIDDLE, randomRange(120, 140), gameLevel);
                    tEnemy.setSize(enemySize, enemySize);
                    tEnemy.setLocation(screenWidth - 150, 0 - tEnemy.height);

                    break;
                default:
                    break;
            }

            vEnemys.add(tEnemy);
        }

        // 적군 움직이고 죽을 것들은 죽임
        for(int i=0;i<vEnemys.size();i++){
            GameEnemy tEnemy = vEnemys.get(i);

            tEnemy.move();

            // 적군이 화면안에 등장하면 상태 바꿔주기
            if(GameResource.STATE_LOAD == tEnemy.state){
                if(checkMapRange(tEnemy)){
                    tEnemy.state = GameResource.STATE_RUN;
                }
            }else{
                if(!checkMapRange(tEnemy)){
                    vEnemys.remove(i);
                }

                if(0 == tEnemy.life){

                    int t = randomRange(0, 1);

                    if(t == 1){
                        GameItem tItem = new GameItem(resource, GameResource.ENEMY_ITEM1);
                        tItem.setSize(enemySize, enemySize);
                        tItem.setLocation(tEnemy.x, tEnemy.y);

                        vItems.add(tItem);
                    }

                    vEnemys.remove(i);
                }
            }

            // 반대로 튕겨져 나가기
            if(tEnemy.mode == 0 && tEnemy.x < changeIndex){
                tEnemy.mode++;
            }
        }

        if(0 == (cnt % 5)){
            for(int i=0;i<vEnemys.size();i++){
                vEnemys.get(i).changeMotion();
            }
        }

        //Log.d("xxx : ", "" + vEnemys.size());
    }

    public void processItem(){

        for(int i=0;i<vItems.size();i++){
            GameItem tItem = vItems.get(i);

            tItem.move();

            if(!checkMapRange(tItem)){
                vItems.remove(i);
            }
        }

        if(0 == (cnt % 5)){
            for(int i=0;i<vItems.size();i++){
                vItems.get(i).changeMotion();
            }
        }
    }

    public void processCollision() {
        for (int i = 0; i < vBullets.size(); i++) {
            GameBullet tBullet = vBullets.get(i);

            if (tBullet.type == GameResource.BULLET0) {
                //내 총알이면
                for (int j = 0; j < vEnemys.size(); j++) {
                    GameEnemy tEnemy = vEnemys.get(j);

                    if (tEnemy.state == GameResource.STATE_RUN && checkCollision(tEnemy, tBullet)) {
                        GameExplode tExplode = new GameExplode(resource, GameResource.ENEMY_BOMB);
                        tExplode.setSize(enemySize + 40, enemySize + 40);
                        tExplode.setLocation(tBullet.x + tExplode.width / 3, tBullet.y + tExplode.height / 3);

                        vExplodes.add(tExplode);

                        vBullets.remove(i);
                        tEnemy.decreaseLife();
                    }
                }

            } else if (tBullet.type == GameResource.BULLET2) {
                if (player.state == GameResource.STATE_RUN && checkCollision(player, tBullet)) {

                    vBullets.remove(i);
                    player.decreaseLife();

                    vibrator.vibrate(500);

                    gCnt = cnt;
                }
            }
        }

        for (int i = 0; i < vItems.size(); i++) {
            GameItem tItem = vItems.get(i);

            if (player.state == GameResource.STATE_RUN || player.state == GameResource.STATE_INVINCIBLE) {
                if (checkCollision(player, tItem)) {

                    vItems.remove(i);
                    player.decreaseBulCnt();
                }
            }
        }
    }

    public void processExplode(){
        for(int i=0;i<vExplodes.size();i++){
            GameExplode tExplode =  vExplodes.get(i);

            if(tExplode.motion == 15){

                vExplodes.remove(i);
            }
        }

        if(0 == (cnt % 1)){
            for(int i=0;i<vExplodes.size();i++){
                vExplodes.get(i).changeMotion();
            }
        }
    }

    /**
     * draw
     */

    public void drawObjects(Canvas canvas){
        switch(gameStage){
            case GameResource.STATUS_TITLE:
                drawTitle(canvas);
                break;
            case GameResource.STATUS_STARTGAME:
                drawBG(canvas);
                drawPlayer(canvas);
                drawCloud(canvas);
                drawStartAction(canvas);
                break;
            case GameResource.STATUS_RUNNINGAME:
                drawBG(canvas);
                drawCloud(canvas);
                drawJoyBG(canvas);
                drawBullet(canvas);
                drawEnemy(canvas);
                drawItem(canvas);
                drawExplode(canvas);
                drawHeart(canvas);
                drawPlayer(canvas);
                break;
            case GameResource.STATUS_STOPGAME:
                break;
            case GameResource.STATUS_GAMEOVER:
                drawBG(canvas);
                drawCloud(canvas);
                drawBullet(canvas);
                drawEnemy(canvas);
                drawItem(canvas);
                drawExplode(canvas);
                drawOver(canvas);
                break;
            default:
                break;
        }
    }

    public void drawTitle(Canvas canvas){

        for(int i=0;i<vTitles.size();i++){
            GameObject tTitle = vTitles.get(i);

            if(tTitle.type == GameResource.PUSH_TITLE){
                if(cnt % 30 < 15){
                    tTitle.draw(canvas);
                }
            }else{
                tTitle.draw(canvas);
            }
        }
    }

    public void drawStartAction(Canvas canvas){
        canvas.drawBitmap(resource.getImage(GameResource.GAMESTART), 0, 250, null);
    }

    public void drawBG(Canvas canvas){
       bg.draw(canvas);
    }

    public void drawPlayer(Canvas canvas){
        if(gameStage == GameResource.STATUS_STARTGAME){

            if((cnt % 30) < 22){
                player.draw(canvas);
            }
        }else if (gameStage == GameResource.STATUS_RUNNINGAME) {

            if(player.state == GameResource.STATE_INVINCIBLE){

                if((cnt % 30) < 22){
                    player.draw(canvas);
                }
            }else{
                player.draw(canvas);
            }
        }
    }

    public void drawCloud(Canvas canvas){
        for(int i=0;i<vClouds.size();i++){
            GameCloud tCloud = vClouds.get(i);

            tCloud.draw(canvas);
        }
    }

    public void drawBullet(Canvas canvas){
        for(int i=0;i<vBullets.size();i++){
            GameBullet tBullet = vBullets.get(i);

            tBullet.draw(canvas);
        }
    }

    public void drawEnemy(Canvas canvas){
        for (int i = 0; i < vEnemys.size();i++){
            GameEnemy tEnemy = vEnemys.get(i);

            tEnemy.draw(canvas);
        }
    }

    public void drawItem(Canvas canvas){
        for(int i=0;i<vItems.size();i++){
            GameItem tItem = vItems.get(i);

            tItem.draw(canvas);
        }
    }

    public void drawExplode(Canvas canvas){
        for(int i=0;i<vExplodes.size();i++){
            GameExplode tExplode = vExplodes.get(i);

            tExplode.draw(canvas);
        }
    }

    public void drawOver(Canvas canvas){
        canvas.drawBitmap(resource.getImage(GameResource.GAMEOVER), 320, 250, null);
    }

    public void drawHeart(Canvas canvas){

        Paint p = new Paint();
        p.setAlpha(200);

        int sx = 30;
        int sy = 30;

        for(int i=0;i<player.life;i++){
            canvas.drawBitmap(resource.getImage(GameResource.PLAYER_HEART), sx + (i * 55), sy, null);
        }
    }

   public void drawJoyBG(Canvas canvas){

       stickBG.draw(canvas);
       stick.draw(canvas);
       //canvas.drawBitmap(resource.getImage(GameResource.JOY_BALL), 100, 100, null);
   }
}
