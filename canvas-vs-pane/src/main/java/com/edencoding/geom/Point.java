package com.edencoding.geom;

import javafx.scene.paint.Color;

import java.text.DecimalFormat;

public class Point {
    public float x;
    public float y;

    DecimalFormat decimalFormat = new DecimalFormat("###");

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point at (" + decimalFormat.format(x) + ", " + decimalFormat.format(y) + ")";
    }
}
