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
import java.util.ConcurrentModificationException;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Game Screen
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final Random rand = new Random();
    private Paint paintText = new Paint();
    private Timer timer = new Timer();
    private GameThread thread;
    private int height, width, radiusDot, spawnDelay;
    private int score = 0;
    private boolean obstacleScheduled = false;
    private Dot player;
    private ArrayList<Dot> obstacleList = new ArrayList<>();
    private ArrayList<Integer> obstaclesToRemove = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);

        thread = new GameThread(getHolder(), this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        width = getWidth();
        height = getHeight();
        radiusDot = width/12;
        spawnDelay = 300;
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(72);

        player = new Dot(width/2, 4*height/5, 0, MainActivity.dotColor, width/16);

        // Start the thread
        thread.setRunning(true);
        thread.start();

        // Set a timer creating a new obstacle at a random location every 300 ms
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                obstacleScheduled = true;
            }
        }, 1000, spawnDelay);

        // Decreases the time between each spawn as time goes on
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(spawnDelay > 200) {
                    spawnDelay = spawnDelay - 50;
                }
            }
        }, 1000, 3000);
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
                thread.join();
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

    public void onDraw(Canvas canvas) throws ConcurrentModificationException {
        // Draws background to cover previous frame
        canvas.drawColor(Color.BLACK);

        // Draws the player and obstacles on the canvas
        // Then marks down the obstacles off the screen
        player.keepInBorders(width);
        player.draw(canvas);
        for(Dot dot : obstacleList) {
            dot.draw(canvas);
        }

        // Displays Score
        canvas.drawText(Integer.toString(score), 7*width/8, height/12, paintText);

        // TODO Add a menu
    }

    /**
        Calculates the distance between each obstacle and the player.
        If the distance is less than the radius,
        the thread is terminated and the DodgeGameOverActivity is displayed
        */
    protected void collisionCheck() {
        for(Dot obstacle : obstacleList) {
            if (player.collisionCheck(obstacle)) {
                // Terminates the game thread and sets the active activity to GameOverActivity
                thread.setRunning(false);
                Intent intent = new Intent().setClass(getContext(), GameOverActivity.class);
                intent.putExtra("score", score);
                getContext().startActivity(intent);
            }
            if(obstacle.reachedBottomCheck(height)) {
                obstaclesToRemove.add(obstacleList.indexOf(obstacle));
                score++;
            }
        }

        // Removes the obstacles marked for deletion
        for (Integer index: obstaclesToRemove) {
            obstacleList.remove(index.intValue());
        }
        obstaclesToRemove.clear();
    }

    protected void createObstacle() {
        if (obstacleScheduled) {
            obstacleList.add(new Dot(rand.nextInt(width - 2*radiusDot) + radiusDot,
                    0, height/30, Color.RED, radiusDot));
            obstacleScheduled = false;
        }
    }
}
