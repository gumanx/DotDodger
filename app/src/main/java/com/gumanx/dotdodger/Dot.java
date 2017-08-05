package com.gumanx.dotdodger;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Dot object
 */

class Dot {

    private final Paint paint = new Paint();
    private float x, y, xVelocity, yVelocity, radius;

    Dot(int x, int y, int yVelocity, int color, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.xVelocity = 0;
        this.yVelocity = yVelocity;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Sets the x velocity
     * @param xVelocity Desired velocity
     */
    void setXVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    /**
     * Moves the dot by its x and y velocities
     */
    void updatePosition() {
        x = x + xVelocity;
        y = y + yVelocity;
    }

    /**
     * Renders the dot object and moves it according to its velocity
     * @param canvas Canvas to be drawn onto
     */
    void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, paint);
    }

    /**
     * This method checks if the dot object has collided with
     * the other dot object. If so, it returns true.
     * @param o Dot to be compared against
     * @return Collision occurred
     */
    boolean collisionCheck(Dot o) {
        // Since this is only a comparison of distances, x^2 + y^2 <= minDist^2
        return ((x - o.x) * (x - o.x)) + ((y - o.y) * (y - o.y))
                <= (2 * radius) * (2 * radius);
    }

    /**
     *  This method checks if the dot is off the screen horizontally. If it is,
     *  it moves the dot back onto the screen.
     * @param width Width of screen
     */
    void keepInBorders(int width) {
        if (x + radius > width) {
            x = width - radius;
        } else if (x - radius < 0) {
            x = radius;
        }
    }

    /**
     *  This method checks if the dot is below the screen. If it is, it returns true.
     * @param height Height of screen
     * @return Reached bottom of screen
     */
    boolean reachedBottomCheck(int height) {
        return y > height;
    }

}

