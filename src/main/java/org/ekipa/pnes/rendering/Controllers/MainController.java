package org.ekipa.pnes.rendering.Controllers;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.util.Pair;
import org.ekipa.pnes.models.netModels.PTNetModel;


public class MainController {

    @FXML
    public Pane gridPane;

    //Buttons
    public Button placeButton;
    public Button transitionButton;
    public Button selectArcButton;
    public Button deleteElementButton;

    private Object selectedElement;

    public void initialize() {
        gridPane.setOnMouseClicked(event -> {
            if (selectedElement == null) {
                showAlert("Nie wybrano elementu sieci do narysowania", "Nie wybrano elementu sieci do narysowania");
            }
        });
        gridPane.setBackground(new Background(
                        new BackgroundFill(createGridPattern(),
                                new CornerRadii(5),
                                new Insets(4, 4, 4, 4))
                )
        );
    }

    private Pair<Double, Double> getMousePosition(MouseEvent event) {
        return new Pair<>(event.getX(), event.getY());
    }

    public ImagePattern createGridPattern() {
        double gridSize = 20;

        double weight = gridSize;
        double height = gridSize;

        Canvas canvas = new Canvas(weight, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.2));
        gc.fillRect(0, 0, weight, height);
        gc.strokeRect(0.5, 0.5, weight, height);

        Image image = canvas.snapshot(new SnapshotParameters(), null);
        ImagePattern pattern = new ImagePattern(image, 0, 0, weight, height, false);

        return pattern;

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void selectPlace(ActionEvent actionEvent) {

    }

    public void selectTransition(ActionEvent actionEvent) {

    }

    public void selectArc(ActionEvent actionEvent) {

    }

    public void deleteElement(ActionEvent actionEvent) {

    }
}
