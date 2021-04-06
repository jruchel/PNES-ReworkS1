package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.ekipa.pnes.models.elements.Place;

public class GridPlace<V> extends GridNetElement {
    private GridPlace(Place netElement, Circle shape, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, label, onDelete, onCreate);
    }

    private GridPlace(Place netElement, Circle shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, onDelete, onCreate);
    }

    private GridPlace(double x, double y, V tokens, int tokenCapacity, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(
                new Place<>("", "", x, y, tokenCapacity, tokens),
                new Circle(x, y, 25, Color.TRANSPARENT),
                label,
                onDelete,
                onCreate
        );
    }
}
