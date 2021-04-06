package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import org.ekipa.pnes.models.elements.Transition;

public class GridTransition extends GridNetElement {
    private GridTransition(Transition netElement, Rectangle shape, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, label, onDelete, onCreate);
    }

    private GridTransition(Transition netElement, Rectangle shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(netElement, shape, onDelete, onCreate);
    }

    public GridTransition(double x, double y, double width, double height, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        super(
                new Transition("", "", x, y),
                new Rectangle(x, y, width, height),
                label,
                onDelete,
                onCreate
        );

    }

    public GridTransition(double x, double y, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this(x, y, 50, 35, label, onDelete, onCreate);
    }
}
