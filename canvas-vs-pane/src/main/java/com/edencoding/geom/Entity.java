package com.edencoding.geom;

import javafx.scene.paint.Color;

public class Entity extends AABB {

    private float xMovement;
    private float yMovement;

    public float getxMovement() {
        return xMovement;
    }

    public void setxMovement(float xMovement) {
        this.xMovement = xMovement;
    }

    public float getyMovement() {
        return yMovement;
    }

    public void setyMovement(float yMovement) {
        this.yMovement = yMovement;
    }

    public Entity(float x0, float y0, float w, float h) {
        super(x0, y0, w, h);
    }

    public void update() {
        x += xMovement;
        y += yMovement;
    }

    public void reflectX() {
        xMovement = -xMovement;
    }

    public void reflectY() {
        yMovement = -yMovement;
    }
}

