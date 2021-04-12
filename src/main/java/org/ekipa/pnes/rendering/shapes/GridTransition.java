package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import org.ekipa.pnes.models.elements.Transition;

public class GridTransition extends GridNetElement {
    private GridTransition(Transition netElement, Rectangle shape, Label label, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        super(netElement, shape, label, onCreate, onDelete);
    }

    private GridTransition(Transition netElement, Rectangle shape, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        super(netElement, shape, onCreate, onDelete);
    }

    public GridTransition(double x, double y, double width, double height, Label label, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        super(
                new Transition("", "", x, y),
                new Rectangle(x - width / 2, y - height / 2, width, height),
                label,
                onCreate,
                onDelete
        );

    }

    public GridTransition(double x, double y, Label label, OnGridElementAction onCreate, OnGridElementAction onDelete) {
        this(x, y, 50, 35, label, onCreate, onDelete);
    }
}
