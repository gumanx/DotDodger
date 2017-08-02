package com.gumanx.dotdodger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game Screen
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final Random rand = new Random();
    private static final Paint paintText = new Paint();
    private Timer timer = new Timer();
    private GameThread gameThread;
    private int height, width, radiusDot, score, spawnDelay, obstacleScheduled;
    private Dot player;
    private ArrayList<Dot> obstacleList = new ArrayList<>();
    private ArrayList<Integer> obstaclesToRemove = new ArrayList<>();

    static {
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(72);
    }

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        gameThread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        width = getWidth();
        height = getHeight();
        radiusDot = width/12;
        score = 0;
        spawnDelay = 21;
        obstacleScheduled = 0;

        player = new Dot(width/2, 4*height/5, 0, MainActivity.dotColor, width/16);

        // Start the thread
        gameThread.setRunning(true);
        gameThread.start();

        // Decreases the time between each spawn as time goes on
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(spawnDelay > 15) {
                    spawnDelay--;
                }
            }
        }, 1000, 1500);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try{
                // Terminates the thread should the surface get destroyed
                gameThread.join();
                timer.cancel();
                retry = false;
            } catch (InterruptedException e) {
                System.out.println("Thread closed");
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Moves the player to either the left or right
            if(event.getX() > (width/2)) {
                player.setXVelocity(width/32);
            } else {
                player.setXVelocity(-width/32);
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // Stops player movement if the finger leaves the screen
            player.setXVelocity(0);
        }
        return true;
    }

    public void onDraw(Canvas canvas) {
        // Draws background to cover previous frame
        canvas.drawColor(Color.BLACK);

        // Draws the player and obstacles on the canvas
        player.draw(canvas);
        player.keepInBorders(width);
        synchronized (obstacleList) {
            for(Dot dot : obstacleList) {
                dot.draw(canvas);
            }
        }

        // Displays Score
        canvas.drawText(Integer.toString(score), 7*width/8, height/12, paintText);

        // TODO Add a menu
    }

    /**
     * Checks for collisions and if one is detected,
     * the thread is terminated and the GameOverActivity is displayed.
     * Also checks for obstacles that have reached the bottom of the screen and
     * removes those that have.
     */
    protected void collisionCheck() {
        synchronized (obstacleList) {
            for(Dot obstacle : obstacleList) {
                if (player.collisionCheck(obstacle)) {
                    // Terminates the game thread and sets the active activity to GameOverActivity
                    gameThread.setRunning(false);
                    Intent intent = new Intent().setClass(getContext(), GameOverActivity.class);
                    intent.putExtra("score", score);
                    getContext().startActivity(intent);
                }
                // Checks if the obstacle reached the bottom of the screen
                if(obstacle.reachedBottomCheck(height)) {
                    obstaclesToRemove.add(obstacleList.indexOf(obstacle));
                    score++;
                }
            }

            // Removes the obstacles marked for deletion
            for (Integer index: obstaclesToRemove) {
                obstacleList.remove(index.intValue());
            }
        }
        obstaclesToRemove.clear();
    }

    /**
     * Creates an obstacle at the specified time intervals
     */
    protected void createObstacle() {
        if (obstacleScheduled >= spawnDelay) {
            synchronized (obstacleList) {
                obstacleList.add(new Dot(rand.nextInt(width - 2*radiusDot) + radiusDot,
                        0, height/32, Color.RED, radiusDot));
            }
            obstacleScheduled = 0;
        } else {
            obstacleScheduled++;
        }
    }
}
