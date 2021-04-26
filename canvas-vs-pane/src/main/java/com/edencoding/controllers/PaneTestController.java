package com.edencoding.controllers;

import com.edencoding.geom.Point;
import com.edencoding.particles.JavaFXCircleParticle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class PaneTestController extends FrameRateCalculator {
    private final float xMax = 1200;
    private final float yMax = 700;
    private final float radius = 10;
    List<JavaFXCircleParticle> particles = new ArrayList<>();
    Point mousePosition = new Point(0, 0);
    boolean attractor;
    Random r = new Random(123456);
    @FXML
    private Pane pane;

    public void initialize() {
//        movementTest(100000);
//        physicsTest(5000);
        collisionDetectionTest(31, 1000, 25);
//        mouseTrackingTest(2f);
    }

    private void creationTest(int number) {
        report(0);
        for (int i = 0; i < number; i++) {
            if (i % 10000 == 0) report(i);
            JavaFXCircleParticle particle = new JavaFXCircleParticle(
                    r.nextFloat() * xMax, r.nextFloat() * yMax,
                    r.nextFloat() * 250, r.nextFloat() * (float) Math.PI * 2,
                    radius,
                    1f,
                    new Color(
                            r.nextDouble(),
                            r.nextDouble(),
                            r.nextDouble(),
                            1.0)
            );
            particles.add(particle);
            pane.getChildren().add(particle);
        }
    }

    public void movementTest(int number) {

        for (int i = 0; i < number; i++) {
            if (i % 10000 == 0) report(i);
            JavaFXCircleParticle particle = new JavaFXCircleParticle(
                    r.nextFloat() * xMax, r.nextFloat() * yMax,
                    r.nextFloat() * 250, r.nextFloat() * (float) Math.PI * 2,
                    radius,
                    1f,
                    new Color(
                            r.nextDouble(),
                            r.nextDouble(),
                            r.nextDouble(),
                            1.0)
            );
            particles.add(particle);
            pane.getChildren().add(particle);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                update(now);

                List<JavaFXCircleParticle> temp = new ArrayList<>(particles);

                for (JavaFXCircleParticle circle : temp) {
                    circle.update(getDeltaTimeNano() / 1e9f);

                    if (circle.getCenterX() < 0 || circle.getCenterX() > pane.getWidth() - radius)
                        circle.reflectX();
                    if (circle.getCenterY() < 0 || circle.getCenterY() > pane.getHeight() - radius)
                        circle.reflectY();

                }
            }
        };
        timer.start();
    }

    public void physicsTest(int number) {

        for (int i = 0; i < number; i++) {
            JavaFXCircleParticle particle = new JavaFXCircleParticle(
                    r.nextFloat() * xMax, r.nextFloat() * yMax,
                    r.nextFloat() * 250, r.nextFloat() * (float) Math.PI * 2,
                    radius,
                    1f,
                    Color.WHITE
            );
            particles.add(particle);
            pane.getChildren().add(particle);
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
                List<JavaFXCircleParticle> temp = new ArrayList<>(particles);
                for (JavaFXCircleParticle circle : temp) {
                    circle.update(getDeltaTimeNano() / 1e9f);
                    if (circle.getCenterX() < 0 || circle.getCenterX() > pane.getWidth() - radius)
                        circle.reflectX();
                    if (circle.getCenterY() < 0 || circle.getCenterY() > pane.getHeight() - radius)
                        circle.reflectY();
                    float force = attractor ? -2.5f : 2.5f;
                    circle.applyForce(xMax / 2, yMax / 2, -force);
                }
            }
        };
        timer.start();
    }

    private void collisionDetectionTest(float backgroundSize, int number, float foregroundSize) {
        int xNumber = (int) Math.floor(xMax / backgroundSize);
        int yNumber = (int) Math.floor(yMax / backgroundSize);

        //create all background points
        List<Rectangle> rectangles = new ArrayList<>();
        for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
                rectangles.add(new Rectangle(
                        i * backgroundSize,
                        j * backgroundSize,
                        (float) (backgroundSize * 0.9),
                        (float) (backgroundSize * 0.9)));
            }
        }
        pane.getChildren().addAll(rectangles);

        //create foreground circle
        Map<Rectangle, Color> entities = new HashMap<>();
        Map<Rectangle, Point2D> movement = new HashMap<>();
        for (int i = 0; i < number; i++) {
            Rectangle entity = new Rectangle(
                    xMax / 2,
                    yMax / 2,
                    foregroundSize, foregroundSize);
            Point2D vector = new Point2D(r.nextFloat() * 20 - 10, r.nextFloat() * 20 - 10);
            Color color = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1);
            entity.setStroke(color.darker());
            entity.setStrokeWidth(2);
            entity.setFill(Color.TRANSPARENT);
            movement.put(entity, vector);
            entities.put(entity, color);
            pane.getChildren().add(entity);
        }

        AnimationTimer timer = new AnimationTimer() {
            int count;

            @Override
            public void handle(long now) {
                //update nanotime
                update(now);
                //paint background circles
                for (Rectangle rectangle : rectangles) {
                    for (Map.Entry<Rectangle, Color> entry : entities.entrySet()) {
                        Rectangle entity = entry.getKey();
                        if (entity.intersects(rectangle.getLayoutBounds())) {
                            rectangle.setFill(entry.getValue());
                            break;
                        } else {
                            rectangle.setFill(Color.WHITE);
                        }
                        count++;
                    }
                }
                for (Rectangle entity : entities.keySet()) {
                    Point2D movementVector = movement.get(entity);
                    entity.setX(entity.getX() + movementVector.getX());
                    entity.setY(entity.getY() + movementVector.getY());
                    if (entity.getX() < 0 || (entity.getX() + entity.getWidth()) > pane.getWidth())
                        movement.put(entity, new Point2D(
                                -movementVector.getX(),
                                movementVector.getY()));
                    if (entity.getY() < 0 || (entity.getY() + entity.getHeight()) > pane.getHeight()) {
                        movement.put(entity, new Point2D(
                                movementVector.getX(),
                                -movementVector.getY()));
                    }
                }
            }
        };
        timer.start();
    }

    private void mouseTrackingTest(float backgroundSize) {
        int xNumber = (int) Math.floor(xMax / backgroundSize);
        int yNumber = (int) Math.floor(yMax / backgroundSize);

        //create all background points
        for (int i = 0; i < xNumber; i++) {
            for (int j = 0; j < yNumber; j++) {
                Rectangle rectangle = new Rectangle(
                        i * backgroundSize,
                        j * backgroundSize,
                        (float) (backgroundSize * 0.9),
                        (float) (backgroundSize * 0.9));

                rectangle.setFill(Color.WHITE);
                rectangle.setOnMouseEntered((event -> {
                    rectangle.setFill(Color.RED);
                }));
                rectangle.setOnMouseExited((event -> rectangle.setFill(Color.WHITE)));
                pane.getChildren().add(rectangle);
            }
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);
            }
        };
        timer.start();
    }

    private void report(int i) {
        System.out.println(i + " at " + System.nanoTime());
    }

}
