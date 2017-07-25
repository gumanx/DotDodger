package com.gumanx.dotdodger;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Dot object
 */

class Dot {

    private int x, y, radius;
    private int xVelocity = 0;
    private int yVelocity;
    private Paint paint = new Paint();

    Dot(int x, int y, int yVelocity, int color, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.yVelocity = yVelocity;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
        x = x + xVelocity;
        y = y + yVelocity;
    }

    boolean collisionCheck(Dot other) {
        double distance = Math.sqrt(((x - other.x) * (x - other.x))
                + ((y - other.y) * (y - other.y)));
        return distance <= (2 * radius);
    }

    void keepInBorders(int width) {
        if (x + radius > width) {
            x = width - radius;
        } else if (x - radius < 0) {
            x = radius;
        }
    }

    /**
     *
     * @param height of screen
     * @return reached bottom of screen
     */
    boolean reachedBottomCheck(int height) {
        return y > height;
    }


}

