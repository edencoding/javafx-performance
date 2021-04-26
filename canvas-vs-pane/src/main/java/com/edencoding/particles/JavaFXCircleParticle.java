package com.edencoding.particles;

import com.edencoding.geom.Maths;
import com.edencoding.geom.Point;
import javafx.geometry.Point2D;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static com.edencoding.geom.Maths.findAngle;

public class JavaFXCircleParticle extends Circle {

    private final Point lastPosition;
    private final float mass;
    private float magnitude;
    private float direction;

    public JavaFXCircleParticle(float x, float y, float magnitude, float direction, float radius, float mass, Color color) {
        super(x, y, radius, color);
        this.lastPosition = new Point(x, y);
        this.magnitude = magnitude;
        this.direction = direction;
        this.mass = mass;
    }

    public void applyForce(float fx, float fy, float mass) {
        float F, mX, mY, angle;

        if ((getCenterX() - fx) * (getCenterX() - fx) + (getCenterY() - fy) * (getCenterY() - fy) != 0) {
            F = this.mass * mass;
            mX = (float) ((this.mass * getCenterX() + mass * fx) / (this.mass + mass));
            mY = (float) ((this.mass * getCenterY() + mass * fy) / (this.mass + mass));
            angle = findAngle((float) getCenterX() - mX, (float) getCenterY() - mY);

            mX = F * Maths.cos(angle);
            mY = F * Maths.sin(angle);

            mX += magnitude * Maths.cos(this.direction);
            mY += magnitude * Maths.sin(this.direction);

            magnitude = Math.min((float) Math.sqrt(mX * mX + mY * mY), 250);

            direction = findAngle(mX, mY);
        }
    }

    public void update(float delta) {
        lastPosition.x = (float) getCenterX();
        lastPosition.y = (float) getCenterY();
        setCenterX(getCenterX() + magnitude * Maths.cos(direction) * delta);
        setCenterY(getCenterY() + magnitude * Maths.sin(direction) * delta);
    }

    public void reflectX() {
        restorePreviousPosition();

        if (direction == 0) {
            direction = (float) Math.PI;
            return;
        }
        if (direction == Math.PI) {
            direction = 0;
            return;
        }

        if (direction < Math.PI / 2 || direction > Math.PI * 3 / 2) {
            direction = (float) (Math.PI - direction);
        } else {
            direction = (float) (Math.PI - direction);
        }
    }

    public void reflectY() {
        restorePreviousPosition();

        if (direction == Math.PI / 2) {
            direction = (float) (Math.PI * 3 / 2);
            return;
        }
        if (direction == (float) (Math.PI * 3 / 2)) {
            direction = (float) (Math.PI / 2);
            return;
        }

        if (direction < Math.PI / 2 || direction > Math.PI * 3 / 2) {
            direction = -direction;
        } else {
            direction = (float) (2 * Math.PI - direction);
        }

    }

    private void restorePreviousPosition() {
        setCenterX(lastPosition.x);
        setCenterY(lastPosition.y);
    }
}
