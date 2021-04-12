package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.elements.Place;

@Getter
@Setter
public class GridPlace<V> extends GridNetElement {

    private GridPlace(Place netElement, Circle shape, Label label, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        super(netElement, shape, label, onCreate, onDelete);
    }

    private GridPlace(Place netElement, Circle shape, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        super(netElement, shape, onCreate, onDelete);
    }

    public GridPlace(double x, double y, V tokens, int tokenCapacity, double radius, Label label, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        super(
                new Place<>("", "", x, y, tokenCapacity, tokens),
                new Circle(x, y, radius),
                label,
                onCreate,
                onDelete
        );

    }

    public GridPlace(double x, double y, V tokens, int tokenCapacity, Label label, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        this(x, y, tokens, tokenCapacity, 25, label, onCreate, onDelete);
    }

    public void setRadius(double radius) {
        ((Circle) this.getShape()).setRadius(radius);
    }
}
