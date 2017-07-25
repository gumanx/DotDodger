package com.gumanx.dotdodger;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by gumanx on 4/13/2017.
 */

class GameThread extends Thread {

    private final SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean isRunning;

    GameThread(SurfaceHolder surfaceHolderIn, GameView gameViewIn) {
        super();
        surfaceHolder = surfaceHolderIn;
        gameView = gameViewIn;
    }

    void setRunning(boolean state) {
        isRunning = state;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        while(isRunning) {
            canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.createObstacle();
                    gameView.onDraw(canvas);
                    gameView.collisionCheck();
                }
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
