package com.edencoding.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class FrameRateCalculator {

    long lastFrameTime = -1L;
    long deltaTimeNano;
    IntegerProperty frameRate = new SimpleIntegerProperty(0);

    public IntegerProperty frameRateProperty() {
        return frameRate;
    }

    protected void update(long now){
        updateFrameTime(now);
        updateFrameRate();
    }

    protected void updateFrameTime(long currentTimeNano) {
        deltaTimeNano = currentTimeNano - lastFrameTime;
        lastFrameTime = currentTimeNano;
    }

    protected void updateFrameRate() {
        frameRate.set((int) Math.round(getFrameRateHertz()));
    }

    public long getDeltaTimeNano() {
        return deltaTimeNano;
    }

    public double getFrameRateHertz() {
        double frameRate = 1d / deltaTimeNano;
        return frameRate * 1e9;
    }


}
