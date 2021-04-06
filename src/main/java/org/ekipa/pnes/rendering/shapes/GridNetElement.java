package org.ekipa.pnes.rendering.shapes;

import javafx.scene.control.Label;
import javafx.scene.shape.Shape;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.elements.NetElement;

@Setter
@Getter
@AllArgsConstructor
public abstract class GridNetElement {
    private NetElement netElement;
    private Shape shape;
    private Label label;
    @Getter(AccessLevel.NONE)
    private OnGridElementAction onDelete;
    @Getter(AccessLevel.NONE)
    private OnGridElementAction onCreate;

    public GridNetElement(NetElement netElement, Shape shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this(netElement, shape, null, onDelete, onCreate);
        this.onCreate.run(this);
    }

    public void delete() {
        onDelete.run(this);
    }
}
