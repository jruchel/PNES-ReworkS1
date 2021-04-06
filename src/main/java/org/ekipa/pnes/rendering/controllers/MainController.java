package org.ekipa.pnes.rendering.controllers;


import javafx.event.EventHandler;
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
import org.ekipa.pnes.rendering.shapes.GridNetElement;
import org.ekipa.pnes.rendering.shapes.GridPlace;
import org.ekipa.pnes.rendering.shapes.GridTransition;
import org.ekipa.pnes.rendering.shapes.OnGridElementAction;
import org.hibernate.sql.Delete;

import java.util.ArrayList;
import java.util.List;


public class MainController {

    @FXML
    public Pane gridPane;

    //Buttons
    public Button placeButton;
    public Button transitionButton;
    public Button selectArcButton;
    public Button deleteElementButton;

    private GridNetElement mouseOverElement;
    private GridNetElement selectedElement;
    private Object selectedAction;
    private PTNetModel netModel;
    private List<GridNetElement> gridNetElements;
    private final double circleRadius = 25;
    private final double rectangleWidth = 50;
    private final double rectangleHeight = 32;

    private OnGridElementAction onDelete;
    private OnGridElementAction onCreate;
    private EventHandler<MouseEvent> onHover;


    public void initialize() {
        gridNetElements = new ArrayList<>();
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
                            if (mouseOverElement == null) {
                                double x = getMousePosition(event).getKey();
                                double y = getMousePosition(event).getValue();
                                setClickHandling(new GridPlace(x, y, null, 0, null, onDelete, onCreate));
                            }
                        }
                        if (selectedAction instanceof Transition) {
                            if (mouseOverElement == null) {
                                double x = getMousePosition(event).getKey();
                                double y = getMousePosition(event).getValue();
                                setClickHandling(new GridTransition(x, y, null, onDelete, onCreate));
                            }
                        }
                    }
                    break;
                case SECONDARY:
                    break;
            }
        });
        onDelete = gridNetElement -> {
            Shape shape = gridNetElement.getShape();
            NetElement netElement = gridNetElement.getNetElement();

            gridPane.getChildren().remove(shape);
            netModel.deleteById(netElement.getId());
        };

        onCreate = gridNetElement -> {
            gridPane.getChildren().add(gridNetElement.getShape());
            netModel.addElement(gridNetElement.getNetElement());
        };


    }

    private void setClickHandling(GridNetElement element) {
        element.setMouseEntered(event1 -> mouseOverElement = element);
        element.setMouseExited(event12 -> mouseOverElement = null);
        element.setMouseClicked(event13 -> {
            selectedElement = element;
            if (selectedAction instanceof Delete) {
                element.delete();
                selectedElement = null;
            }
        });
    }

    private Pair<Double, Double> getMousePosition(MouseEvent event) {
        return new Pair<>(event.getX(), event.getY());
    }


    private void createTransition(Pair<Double, Double> position) {
        /*Transition transition = netModel.createTransition("", position.getKey(), position.getValue());

        Rectangle rectangle = new Rectangle(position.getKey() - rectangleWidth / 2, position.getValue() - rectangleHeight / 2, 50, 35);
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
        rectangle.setOnMouseExited(event -> mouseOverElement = null);*/
    }

    private void deleteGridElement(Shape element) {
        this.gridPane.getChildren().remove(element);
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

    private GridNetElement findElementById(String id) {
        return gridNetElements.stream().filter(gridNetElement -> gridNetElement.getId().equals(id)).findFirst().orElse(null);
    }

    private ImagePattern createGridPattern() {
        double gridSize = 20;

        Canvas canvas = new Canvas(gridSize, gridSize);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.setStroke(Color.GRAY);
        gc.setFill(Color.WHITE.deriveColor(1, 1, 1, 0.2));
        gc.fillRect(0, 0, gridSize, gridSize);
        gc.strokeRect(0.5, 0.5, gridSize, gridSize);

        Image image = canvas.snapshot(new SnapshotParameters(), null);
        return new ImagePattern(image, 0, 0, gridSize, gridSize, false);
    }
}
