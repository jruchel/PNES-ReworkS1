package org.ekipa.pnes.rendering;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.File;

import java.net.URL;

public class Rendering extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Rendering");
        URL url = new File("src/main/java/org/ekipa/pnes/rendering/resources/Main.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root,1300,1000);
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setFill(createGridPattern());
    }
    public ImagePattern createGridPattern() {
        double gridSize = 20;

        double w = gridSize;
        double h = gridSize;

        Canvas canvas = new Canvas(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.2));
        gc.fillRect(0, 0, w, h);
        gc.strokeRect(0.5, 0.5, w, h);

        Image image = canvas.snapshot(new SnapshotParameters(), null);
        ImagePattern pattern = new ImagePattern(image, 0, 0, w, h, false);

        return pattern;

    }

    public static void main(String[] args) {
        launch(args);
    }
}
