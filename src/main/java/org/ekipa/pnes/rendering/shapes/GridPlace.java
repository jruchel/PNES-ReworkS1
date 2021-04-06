package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.elements.Place;

@Getter
@Setter
public class GridPlace<V> extends GridNetElement {

    private GridPlace(Place netElement, Circle shape, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, label, onDelete, onCreate);
    }

    private GridPlace(Place netElement, Circle shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, onDelete, onCreate);
    }

    public GridPlace(double x, double y, V tokens, int tokenCapacity, double radius, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(
                new Place<>("", "", x, y, tokenCapacity, tokens),
                new Circle(x, y, radius),
                label,
                onDelete,
                onCreate
        );

    }

    public GridPlace(double x, double y, V tokens, int tokenCapacity, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this(x, y, tokens, tokenCapacity, 25, label, onDelete, onCreate);
    }

    public void setRadius(double radius) {
        ((Circle) this.getShape()).setRadius(radius);
    }
}
