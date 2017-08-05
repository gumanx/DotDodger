package com.gumanx.dotdodger;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Thread that handles the game logic and rendering
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
            gameView.createObstacle();
            gameView.updateDots();
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gameView.onDraw(canvas);
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
