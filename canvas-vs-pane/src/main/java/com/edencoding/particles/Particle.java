package com.edencoding.particles;

import com.edencoding.geom.Maths;
import com.edencoding.geom.Point;
import javafx.scene.paint.Color;
import static com.edencoding.geom.Maths.findAngle;

public class Particle {

    private final Point position;
    private final Point lastPosition;
    private final float mass;
    private final Color color;
    private float magnitude;
    private float direction;

    public Particle(float x, float y, float magnitude, float direction, float mass, Color color) {
        this.position = new Point(x, y);
        this.lastPosition = new Point(x, y);
        this.magnitude = magnitude;
        this.direction = direction;
        this.color = color;
        this.mass = mass;
    }

    public Color getColor() {
        return color;
    }

    public Point getPosition() {
        return position;
    }

    public void applyForce(float fx, float fy, float mass) {
        float F, mX, mY, angle;

        if ((position.x - fx) * (position.x - fx) + (position.y - fy) * (position.y - fy) != 0) {
            F = this.mass * mass;
            mX = (this.mass * position.x + mass * fx) / (this.mass + mass);
            mY = (this.mass * position.y + mass * fy) / (this.mass + mass);
            angle = findAngle(position.x - mX, position.y - mY);

            mX = F * Maths.cos(angle);
            mY = F * Maths.sin(angle);

            mX += magnitude * Maths.cos(this.direction);
            mY += magnitude * Maths.sin(this.direction);

            magnitude = Math.min((float) Math.sqrt(mX * mX + mY * mY), 250);

            direction = findAngle(mX, mY);
        }
    }

    public void update(float delta) {
        lastPosition.x = position.x;
        lastPosition.y = position.y;
        position.x += magnitude * Maths.cos(direction) * delta;
        position.y += magnitude * Maths.sin(direction) * delta;
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
        position.x = lastPosition.x;
        position.y = lastPosition.y;
    }
}
