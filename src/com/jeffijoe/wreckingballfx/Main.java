/*
* WreckingBallFX
* @author Jeff Hansen <jeff@jeffijoe.com>
*
* Requires JDK8 with Lambda support.
*
* Experimenting with JavaFX 3D lead me to this.
* Copyright (C) Jeff Hansen - Jeffijoe.com
*
* LICENSE:
*
* DON'T BE A DICK PUBLIC LICENSE TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
*
* Do whatever you like with the original work, just don't be a dick.
*
* Being a dick includes - but is not limited to - the following instances:
*
*  1a. Outright copyright infringement - Don't just copy this and change the name.
*  1b. Selling the unmodified original with no work done what-so-ever, that's REALLY being a dick.
*  1c. Modifying the original work to contain hidden harmful content. That would make you a PROPER dick.
*
* If you become rich through modifications, related works/services, or supporting the original work,
* share the love. Only a dick would make loads off this work and not buy the original works creator(s) a pint.
*
* Code is provided with no warranty. Using somebody else's code and bitching when it goes wrong
* makes you a DONKEY dick. Fix the problem yourself. A non-dick would submit the fix back.
*
* */
package com.jeffijoe.wreckingballfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    public static final int SCENE_HEIGHT = 400; // Initial scene height
    public static final int SCENE_WIDTH = 800; // Initial scene width
    private static final double FRAME_MILLIS = 0.1; // I actually don't know how this number works, but it does.
    public static final int MILEY_X = 440; // Initially position miley HERE..

    private Scene scene;
    private Group root;
    private Box plane;
    private PerspectiveCamera camera;
    private ImageView sky;
    private Timeline timeline;
    private KeyFrame frame;
    private Sphere ball;
    private ImageView miley;
    private Group ballGroup;

    /*
     * Starts moving the ball group.
     */
    private void startMovement(boolean goingRight) {
        // Whenever new movement is performed, we need
        // to create a new timeline, so stop the currently
        // running one if there is one.
        if(timeline != null)
            timeline.stop();

        // Movement amount.
        int i = 30;
        double amount = goingRight ? i :0-i;

        // If we are going to the right, rotate things
        // and reposition. When going left, revert.
        if(!goingRight) {
            miley.setRotate(180);
            miley.setTranslateX(MILEY_X - 80);
        } else {
            miley.setRotate(0);
            miley.setTranslateX(MILEY_X);
        }

        // Requires JDK8 with Lambda support. Rewrite if you want, I don't care.
        frame = new KeyFrame(Duration.millis(FRAME_MILLIS), e -> {
            double newPosition = ballGroup.getTranslateX() + amount;
            ballGroup.setTranslateX(newPosition);
        });

        // Create timeline and start playing.
        timeline = new Timeline(frame);
        timeline.play();
    }

    /*
     * Stops the timeline.
     */
    private void stopMovement() {
        timeline.stop();
    }

    /*
     * This is the entrypoint.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        // Plane is the "ground".
        // There are no physics involved, it just makes it
        // look better.
        plane = new Box(5000,100,1);

        // Move the plane in position
        plane.setRotationAxis(new Point3D(-1, 0, 0));
        plane.setRotate(56);
        plane.setTranslateY(SCENE_HEIGHT-50);
        plane.setTranslateX(400);

        // Create the ball and position it.
        // Honestly these are just numbers
        // I came up with to make it work.
        ball = new Sphere(65);
        ball.setTranslateX(500);
        ball.setTranslateY(300);

        // Load the Miley image, and position her on the ball.
        miley = new ImageView(getClass().getResource("ball.png").toString());
        miley.setTranslateX(MILEY_X);
        miley.setTranslateY(55);
        // She rotates around the Y axis.
        miley.setRotationAxis(new Point3D(0, 1, 0));

        // We use a group so we can move Miley and her beloved ball at once.
        ballGroup = new Group(ball,miley);
        ballGroup.setTranslateX(-650); // Position it off-screen to the left.

        // Loads a nice sky.
        sky = new ImageView(getClass().getResource("sky.png").toString());
        sky.setTranslateZ(1000);
        sky.setTranslateY(0);

        // Root group contains ALL THE THINGS!
        root = new Group(sky, plane, ballGroup);
        // The scene is the container for ALL THE THINGS GROUP.
        scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        // A camera.. Not much to say.
        camera = new PerspectiveCamera(false);
        // Make the scene use our camera.
        scene.setCamera(camera);
        // We want to use a background color for the scene.
        // This is the same color as in the sky.png image.
        scene.setFill(Paint.valueOf("#0083ff"));

        // Set up key events
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        // Set title and start things up.
        primaryStage.setTitle("Wreck your balls");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     * When either the LEFT or RIGHT key is released, stop the timeline.
     */
    public void onKeyReleased(KeyEvent e) {
        KeyCode code = e.getCode();
        if(code == KeyCode.RIGHT || code == KeyCode.LEFT)
            stopMovement();
    }

    /*
     * When either the LEFT or RIGHT key is being pressed, start movement.
     */
    public void onKeyPressed(KeyEvent e) {
        KeyCode i = e.getCode();
        if (i == KeyCode.LEFT) {
            startMovement(false);
        } else if (i == KeyCode.RIGHT) {
            startMovement(true);
        }
    }

    // Does nothing in a JavaFX application.
    public static void main(String[] args) {
        launch(args);
    }
}
