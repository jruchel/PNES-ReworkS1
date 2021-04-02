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
import javafx.util.Pair;
import org.ekipa.pnes.models.elements.NetObject;
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

    private Object selectedElement;

    private PTNetModel netModel;

    private Map<Object, Object> netElements;

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
                    if (selectedElement == null) {
                        showAlert("Nie wybrano elementu sieci do narysowania", "Nie wybrano elementu sieci do narysowania");
                    } else {
                        if (getElementAt(getMousePosition(event)) == null) {
                            if (selectedElement instanceof Place) {
                                createPlace(getMousePosition(event));
                            }
                            if (selectedElement instanceof Transition) {
                                drawRectangle(getMousePosition(event));
                            }
                        }
                        else {
                            if (selectedElement instanceof Delete) {
                                delete(getMousePosition(event));
                            }
                        }

                    }
                    System.out.println(netModel.getNetElements());
                    break;
                case SECONDARY:

                    break;
            }
        });

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

    private void createPlace(Pair<Double, Double> position) {
        Circle circle = new Circle(position.getKey(), position.getValue(), 25, Color.TRANSPARENT);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        
        gridPane.getChildren().add(circle);
        Place place = netModel.createPlace("", position.getKey(), position.getValue(), 0, 0);
        netElements.put(place, circle);
    }

    private void delete(Pair<Double, Double> position) {
        Object gridObject = getElementAt(position);
        Object netElement = netElements.keySet().stream().filter(netElem -> netElements.get(netElem).equals(gridObject)).findFirst().orElse(null);
        if (gridObject == null || netElement == null) return;
        netModel.deleteById(((NetObject) (netElement)).getId());
        deleteGridElement(gridObject);
    }

    private void deleteGridElement(Object element) {
        this.gridPane.getChildren().remove(element);
    }

    private void drawRectangle(Pair<Double, Double> position) {
        Transition transition = netModel.createTransition("", position.getKey(), position.getValue());
        double width = 50;
        double height = 32;
        Rectangle rectangle = new Rectangle(position.getKey() - width / 2, position.getValue() - height / 2, 50, 35);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(2);
        gridPane.getChildren().add(rectangle);
        netElements.put(transition, rectangle);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private boolean isInsideTransition(Pair<Double, Double> transitionPos, Pair<Double, Double> point) {
        double transX = transitionPos.getKey();
        double transY = transitionPos.getValue();
        double pointX = point.getKey();
        double pointY = point.getValue();
        //przed lub na x + width / 2
        if (pointX > transX + rectangleWidth) return false;
        //po x - width / 2
        if (pointX < transX - rectangleWidth) return false;
        //wyzej niz y - height / 2
        if (pointY > transY + rectangleHeight) return false;
        //nizej niz y + height / 2
        return !(pointY < transY - rectangleHeight);
    }

    private boolean isInsidePlace(Pair<Double, Double> place, Pair<Double, Double> point) {
        return distanceBetweenPoints(place, point) < circleRadius * 2;
    }

    private double distanceBetweenPoints(Pair<Double, Double> p1, Pair<Double, Double> p2) {
        return Math.sqrt(Math.pow(p2.getKey() - p1.getKey(), 2) + Math.pow(p2.getValue() - p1.getValue(), 2));
    }

    private Object getElementAt(Pair<Double, Double> position) {
        Set<Object> elements = netElements.keySet();
        for (Object element : elements) {
            if (element instanceof Place) {
                Place place = (Place) element;
                if (isInsidePlace(new Pair<>(place.getX(), place.getY()), position)) {
                    return netElements.get(place);
                }
            }
            if (element instanceof Transition) {
                Transition transition = (Transition) element;
                if (isInsideTransition(new Pair<>(transition.getX(), transition.getY()), position)) {
                    return netElements.get(transition);
                }
            }
        }
        return null;
    }


    public void selectPlace() {
        this.selectedElement = new Place<Integer>();
    }

    public void selectTransition() {
        this.selectedElement = new Transition();
    }

    public void selectArc() {

    }

    public void selectDelete() {
        this.selectedElement = new Delete();
    }
}
