package com.edencoding.geom;

public class Maths {

    /*
     * See https://jvm-gaming.org/t/fast-math-sin-cos-lookup-tables/36660
     */

    public static float sin(float rad)
    {
        return sin[(int) (rad * radToIndex) & SIN_MASK];
    }

    public static float cos(float rad)
    {
        return cos[(int) (rad * radToIndex) & SIN_MASK];
    }

    private static final int     SIN_BITS,SIN_MASK,SIN_COUNT;
    private static final float   radFull,radToIndex;
    private static final float   degFull,degToIndex;
    private static final float[] sin, cos;

    static
    {
        SIN_BITS  = 12;
        SIN_MASK  = ~(-1 << SIN_BITS);
        SIN_COUNT = SIN_MASK + 1;

        radFull    = (float) (Math.PI * 2.0);
        degFull    = (float) (360.0);
        radToIndex = SIN_COUNT / radFull;
        degToIndex = SIN_COUNT / degFull;

        sin = new float[SIN_COUNT];
        cos = new float[SIN_COUNT];

        for (int i = 0; i < SIN_COUNT; i++)
        {
            sin[i] = (float) Math.sin((i + 0.5f) / SIN_COUNT * radFull);
            cos[i] = (float) Math.cos((i + 0.5f) / SIN_COUNT * radFull);
        }

        // Four cardinal directions (credits: Nate)
        for (int i = 0; i < 360; i += 90)
        {
            sin[(int)(i * degToIndex) & SIN_MASK] = (float)Math.sin(i * Math.PI / 180.0);
            cos[(int)(i * degToIndex) & SIN_MASK] = (float)Math.cos(i * Math.PI / 180.0);
        }
    }

    public static float HALF_PI = (float) Math.PI / 2;

    public static float findAngle(float x, float y) {
        float theta;
        if (x == 0) {
            theta = y > 0 ? HALF_PI : 3 * HALF_PI;
        } else {
            theta = (float) Math.atan(y / x);
            if ((x < 0) && (y >= 0)) {
                theta += Math.PI;
            }
            if ((x < 0) && (y < 0)) {
                theta -= Math.PI;
            }
        }
        return theta;
    }

    public static float squareDistance(Point center, Point center1) {
        return (center.x - center1.x) * (center.x - center1.x) +
                (center.y - center1.y) * (center.y - center1.y);
    }
}
