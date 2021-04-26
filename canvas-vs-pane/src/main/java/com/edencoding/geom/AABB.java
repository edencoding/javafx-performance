package com.edencoding.geom;

public class AABB extends Point {
    public final float w;
    public final float h;
    public AABB(float x0, float y0, float w, float h) {
        super(x0, y0);
        this.w = w;
        this.h = h;
    }
    public boolean contains(float x, float y) {
        return x >= this.x && x <= x1() && y >= this.y && y <= y1();
    }
    public boolean intersects(AABB rect2) {
        return this.x < rect2.x1() &&
                this.x1() > rect2.x &&
                this.y < rect2.y1() &&
                this.y1() > rect2.y;
    }
    public float x1() {
        return x + w;
    }
    public float y1() {
        return y + h;
    }
}
