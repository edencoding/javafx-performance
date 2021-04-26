package com.edencoding.controllers;

import com.edencoding.geom.AABB;
import com.edencoding.geom.Entity;
import com.edencoding.geom.Point;
import com.edencoding.particles.Particle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class CanvasTestController extends FrameRateCalculator {
    @FXML
    private Canvas canvas;

    private final float xMax = 1200;
    private final float yMax = 700;
    private final float radius = 20;

    List<Particle> particles = new ArrayList<>();

    Point mousePosition = new Point(0, 0);
    boolean mouseActive = true;
    boolean attractor;

    Random r = new Random(123456);

    public void initialize() {
//        movementTest(100000);
        collisionDetectionTest(12.5f, 1000, 25);
//        physicsTest(316228);
//        mouseTrackingTestGridded(2f);
    }

    private void creationTest(int number) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        report(0);

        for (int i = 0; i < number; i++) {
            if (i % 10000 == 0) report(i);
            gc.fillOval(r.nextDouble() * xMax, r.nextDouble() * yMax, radius, radius);
        }

    }

    public void movementTest(int number) {

        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (int i = 0; i < number; i++) {
            if (i % 10000 == 0) report(i);
            particles.add(new Particle(
                    r.nextFloat() * xMax - radius, r.nextFloat() * yMax - radius,
                    r.nextFloat() * 250, r.nextFloat() * (float) Math.PI * 2,
                    1f,
                    new Color(
                            r.nextDouble(),
                            r.nextDouble(),
                            r.nextDouble(),
                            1.0
                    )));

        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                update(now);

                gc.setFill(Color.PAPAYAWHIP);
                gc.fillRect(0, 0, xMax, yMax);

                List<Particle> temp = new ArrayList<>(particles);

                for (Particle particle : temp) {

                    particle.update(getDeltaTimeNano() / 1e9f);

                    //calculate coordinates
                    Point position = particle.getPosition();

                    if (position.x < 0 || position.x > canvas.getWidth() - radius)
                        particle.reflectX();
                    if (position.y < 0 || position.y > canvas.getHeight() - radius)
                        particle.reflectY();

                    //paint
                    gc.setFill(particle.getColor());
                    gc.fillOval(position.x, position.y, radius, radius);
                }
            }
        };
        timer.start();
    }

    public void physicsTest(int number) {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < number; i++) {
            particles.add(new Particle(
                    r.nextFloat() * xMax - radius, r.nextFloat() * yMax - radius,
                    r.nextFloat() * 250, r.nextFloat() * (float) Math.PI * 2,
                    1f,
                    Color.WHITE));
        }

        AnimationTimer timer = new AnimationTimer() {
            int count;
            @Override
            public void handle(long now) {
                //reset canvas
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, xMax, yMax);

                //update nanotime
                update(now);

                count++;
                if(count > 120 && count <= 600){
                    System.out.println(getFrameRateHertz());
                }else if (count > 600){
                    Platform.exit();
                }

                List<Particle> temp = new ArrayList<>(particles);
                for (Particle particle : temp) {
                    particle.update(getDeltaTimeNano() / 1e9f);
                    Point position = particle.getPosition();

                    if (position.x < 0 || position.x > canvas.getWidth() - radius)
                        particle.reflectX();
                    if (position.y < 0 || position.y > canvas.getHeight() - radius)
                        particle.reflectY();

                    float force = attractor ? -2.5f : 2.5f;
                    particle.applyForce(xMax / 2, yMax / 2, -force);

                    gc.setFill(particle.getColor());
                    gc.fillOval(position.x, position.y, radius, radius);

                }
            }
        };
        timer.start();
    }

    private void collisionDetectionTest(float backgroundSize, int number, float foregroundSize) {
        int xNumber = (int) Math.floor(xMax / backgroundSize);
        int yNumber = (int) Math.floor(yMax / backgroundSize);

        //create all background points
        int points = 0;
        List<AABB> rectangles = new ArrayList<>();
        for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
                points++;
                rectangles.add(new AABB(
                        i * backgroundSize,
                        j * backgroundSize,
                        (float) (backgroundSize * 0.9),
                        (float) (backgroundSize * 0.9)));
            }
        }
        System.out.println(points);

        //create foreground circle
        Map<Entity, Color> entities = new HashMap<>();
        for (int i = 0; i < number; i++) {
            Entity entity = new Entity(
                    xMax / 2,
                    yMax / 2,
                    foregroundSize, foregroundSize);
            entity.setxMovement(r.nextFloat() * 20 - 10);
            entity.setyMovement(r.nextFloat() * 20 - 10);
            entities.put(entity,
                    new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1));
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            int count;
            @Override
            public void handle(long now) {
                //update nanotime
                update(now);
                //reset canvas
                gc.setFill(Color.GREY);
                gc.fillRect(0, 0, xMax, yMax);
                //paint background circles
                for (AABB rectangle : rectangles) {
                    for (Map.Entry<Entity, Color> entry : entities.entrySet()) {
                        if (entry.getKey().intersects(rectangle)) {
                            gc.setFill(entry.getValue());
                            break;
                        } else {
                            gc.setFill(Color.WHITE);
                        }
                    }
                    gc.fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h);
                }
                for (Map.Entry<Entity, Color> entry : entities.entrySet()) {
                    Entity entity = entry.getKey();
                    //paint foreground circle
                    gc.setStroke(entry.getValue().darker());
                    gc.setLineWidth(2);
                    gc.strokeRect(
                            entity.x, entity.y,
                            entity.w, entity.h);
                    entity.update();
                    if (entity.x < 0 || entity.x1() > canvas.getWidth())
                        entity.reflectX();
                    if (entity.y < 0 || entity.y1() > canvas.getHeight())
                        entity.reflectY();
                }
            }
        };
        timer.start();
    }

    private void mouseTrackingTest(float backgroundSize) {
        int xNumber = (int) Math.floor(xMax / backgroundSize);
        int yNumber = (int) Math.floor(yMax / backgroundSize);

        canvas.setOnMouseMoved((event) -> {
            mousePosition.x = (float) event.getX();
            mousePosition.y = (float) event.getY();
        });
        canvas.setOnMouseExited((event -> mouseActive = false));
        canvas.setOnMouseEntered((event -> mouseActive = true));

        //create all background points
        List<AABB> rectangles = new ArrayList<>();
        for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
                rectangles.add(new AABB(
                        i * backgroundSize,
                        j * backgroundSize,
                        (float) (backgroundSize * 0.9),
                        (float) (backgroundSize * 0.9)));
            }
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                //update nanotime
                update(now);

                //reset canvas
                gc.setFill(Color.GREY);
                gc.fillRect(0, 0, xMax, yMax);

                //paint background circles
                for (AABB rectangle : rectangles) {
                    if (mouseActive && rectangle.contains(mousePosition.x, mousePosition.y)) {
                        gc.setFill(Color.RED);
                    } else {
                        gc.setFill(Color.WHITE);
                    }
                    gc.fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h);

                }
            }
        };
        timer.start();


    }

    private void mouseTrackingTestGridded(float backgroundSize) {
        int xNumber = (int) Math.floor(xMax / backgroundSize);
        int yNumber = (int) Math.floor(yMax / backgroundSize);

        canvas.setOnMouseMoved((event) -> {
            mousePosition.x = (float) event.getX();
            mousePosition.y = (float) event.getY();
        });
        canvas.setOnMouseExited((event -> mouseActive = false));
        canvas.setOnMouseEntered((event -> mouseActive = true));

        //create all background points
        List<AABB> rectanglesTL = new ArrayList<>();
        List<AABB> rectanglesBL = new ArrayList<>();
        List<AABB> rectanglesTR = new ArrayList<>();
        List<AABB> rectanglesBR = new ArrayList<>();
        int quadrant;
        for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
                List<AABB> listreference;
                if (i <= xNumber / 2) {         //LEFT
                    if (j <= yNumber / 2) {      //TOP
                        listreference = rectanglesTL;
                    } else {                    //BOTTOM
                        listreference = rectanglesBL;
                    }
                } else {                        //RIGHT
                    if (j <= yNumber / 2) {      //TOP
                        listreference = rectanglesTR;
                    } else {                    //BOTTOM
                        listreference = rectanglesBR;
                    }
                }

                listreference.add(new AABB(
                        i * backgroundSize,
                        j * backgroundSize,
                        (float) (backgroundSize * 0.9),
                        (float) (backgroundSize * 0.9)));
            }
        }

        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (List<AABB> list : Arrays.asList(rectanglesTL, rectanglesBL, rectanglesTR, rectanglesBR)) {
            for (AABB rectangle : list) {
                gc.setFill(Color.WHITE);
                gc.fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h);
            }
        }

        AnimationTimer timer = new AnimationTimer() {
            int lastQuadrant;

            void resetCanvas() {
                gc.setFill(Color.GREY.brighter());
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                for (List<AABB> list : Arrays.asList(rectanglesTL, rectanglesBL, rectanglesTR, rectanglesBR)) {
                    for (AABB rectangle : list) {
                        gc.setFill(Color.WHITE);
                        gc.fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h);
                    }
                }

            }

            @Override
            public void handle(long now) {
                //update nanotime
                update(now);

                int currentQuadrant;
                if (mousePosition.x <= xMax / 2) {
                    if (mousePosition.y <= yMax / 2) {
                        currentQuadrant = 1;
                    } else {
                        currentQuadrant = 3;
                    }
                } else {
                    if (mousePosition.y <= yMax / 2) {
                        currentQuadrant = 2;
                    } else {
                        currentQuadrant = 4;
                    }
                }
                if (currentQuadrant != lastQuadrant) resetCanvas();
                lastQuadrant = currentQuadrant;

                List<AABB> listreference;
                Point TL;
                if (mousePosition.x <= xMax / 2) {
                    if (mousePosition.y <= yMax / 2) {
                        listreference = rectanglesTL;
                        TL = new Point(0, 0);
                    } else {
                        listreference = rectanglesBL;
                        TL = new Point(0, yMax / 2 + 2);
                    }
                } else {
                    if (mousePosition.y <= yMax / 2) {
                        listreference = rectanglesTR;
                        TL = new Point(xMax / 2 + 2, 0);

                    } else {
                        listreference = rectanglesBR;
                        TL = new Point(xMax / 2 + 2, yMax / 2 + 2);

                    }
                }

                //reset canvas
                gc.setFill(Color.GREY);
                gc.fillRect(TL.x, TL.y, xMax / 2 + 2, yMax / 2 + 2);

                //paint background circles
                for (AABB rectangle : listreference) {
                    if (mouseActive && rectangle.contains(mousePosition.x, mousePosition.y)) {
                        gc.setFill(Color.RED);
                    } else {
                        gc.setFill(Color.WHITE);
                    }
                    gc.fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h);
                }
            }
        };
        timer.start();
    }

    private void report(int i) {
        System.out.println(i + " at " + System.nanoTime());
    }

}
