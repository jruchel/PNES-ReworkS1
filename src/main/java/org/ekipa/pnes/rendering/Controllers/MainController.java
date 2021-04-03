package org.ekipa.pnes.rendering.Controllers;


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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Pair;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.models.netModels.PTNetModel;
import org.hibernate.sql.Delete;

import java.util.*;


public class MainController {

    @FXML
    public Pane gridPane;

    //Buttons
    public Button placeButton;
    public Button transitionButton;
    public Button selectArcButton;
    public Button deleteElementButton;

    private Shape mouseOverElement;
    private Shape selectedElement;
    private Object selectedAction;
    private PTNetModel netModel;
    private Map<NetElement, Shape> netElements;
    private final double circleRadius = 25;
    private final double rectangleWidth = 50;
    private final double rectangleHeight = 32;


    public void initialize() {
        netElements = new HashMap<>();
        netModel = new PTNetModel();
        gridPane.setBackground(new Background(
                        new BackgroundFill(createGridPattern(),
                                new CornerRadii(5),
                                new Insets(4, 4, 4, 4))
                )
        );
        gridPane.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY:
                    if (selectedAction == null) {
                        showAlert("Nie wybrano elementu sieci do narysowania", "Nie wybrano elementu sieci do narysowania");
                    } else {
                        if (selectedAction instanceof Place) {
                            if (mouseOverElement == null)
                            createPlace(getMousePosition(event));
                        }
                        if (selectedAction instanceof Transition) {
                            if (mouseOverElement == null)
                            createTransition(getMousePosition(event));
                        }
                    }
                    break;
                case SECONDARY:
                    break;
            }
        });

    }

    private Pair<Double, Double> getMousePosition(MouseEvent event) {
        return new Pair<>(event.getX(), event.getY());
    }

    private void createPlace(Pair<Double, Double> position) {
        Circle circle = new Circle(position.getKey(), position.getValue(), 25, Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.setOnMouseClicked(event -> {
            selectedElement = circle;
            if (selectedAction instanceof Delete) {
                delete(circle);
                selectedElement = null;
            }
        });
        circle.setOnMouseEntered(event -> mouseOverElement = circle);
        circle.setOnMouseExited(event -> mouseOverElement = null);
        gridPane.getChildren().add(circle);
        Place place = netModel.createPlace("", position.getKey(), position.getValue(), 0, 0);
        netElements.put(place, circle);
    }

    private boolean isInsidePlace(Pair<Double, Double> place, Pair<Double, Double> point) {
        return distanceBetweenPoints(place, point) < circleRadius * 2;
    }

    private void createTransition(Pair<Double, Double> position) {
        Transition transition = netModel.createTransition("", position.getKey(), position.getValue());
        double width = 50;
        double height = 32;
        Rectangle rectangle = new Rectangle(position.getKey() - width / 2, position.getValue() - height / 2, 50, 35);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        rectangle.setOnMouseClicked(event -> {
            selectedElement = rectangle;
            if (selectedAction instanceof Delete) {
                delete(rectangle);
                selectedElement = null;
            }
        });
        rectangle.setOnMouseEntered(event -> mouseOverElement = rectangle);
        rectangle.setOnMouseExited(event -> mouseOverElement = null);
        gridPane.getChildren().add(rectangle);
        netElements.put(transition, rectangle);
    }

    private boolean isInsideTransition(Pair<Double, Double> transitionPos, Pair<Double, Double> point) {
        double transX = transitionPos.getKey();
        double transY = transitionPos.getValue();
        double pointX = point.getKey();
        double pointY = point.getValue();
        if (pointX > transX + rectangleWidth) return false;
        if (pointX < transX - rectangleWidth) return false;
        if (pointY > transY + rectangleHeight) return false;
        return !(pointY < transY - rectangleHeight);
    }

    private void delete(Shape shape) {
        NetElement netElement = netElements.keySet().stream().filter(netElem -> netElements.get(netElem).equals(shape)).findFirst().orElse(null);
        if (shape == null || netElement == null)
            return;
        netModel.deleteById(((netElement)).getId());
        deleteGridElement(shape);
        netElements.remove(netElement);
    }

    private void deleteGridElement(Shape element) {
        this.gridPane.getChildren().remove(element);
    }

    private double distanceBetweenPoints(Pair<Double, Double> p1, Pair<Double, Double> p2) {
        return Math.sqrt(Math.pow(p2.getKey() - p1.getKey(), 2) + Math.pow(p2.getValue() - p1.getValue(), 2));
    }


    public void selectPlace() {
        this.selectedAction = new Place<Integer>();
    }

    public void selectTransition() {
        this.selectedAction = new Transition();
    }

    public void selectArc() {

    }

    public void selectDelete() {
        this.selectedAction = new Delete();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private ImagePattern createGridPattern() {
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
}
