package org.ekipa.pnes.rendering.shapes;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Shape;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.elements.NetElement;


@Setter
@Getter
public abstract class GridNetElement {
    private NetElement netElement;
    private Shape shape;
    private Label label;
    @Getter(AccessLevel.NONE)
    private OnGridElementAction onDelete;
    @Getter(AccessLevel.NONE)
    private OnGridElementAction onCreate;

    public GridNetElement(NetElement netElement, Shape shape, Label label, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this.netElement = netElement;
        this.shape = shape;
        this.label = label;
        if (label != null) this.label.setLabelFor(shape);
        this.onCreate = onCreate;
        this.onDelete = onDelete;
        this.onCreate.run(this);
    }

    public GridNetElement(NetElement netElement, Shape shape, OnGridElementAction onDelete, OnGridElementAction onCreate) {
        this(netElement, shape, null, onDelete, onCreate);

    }

    public void setMouseClicked(EventHandler<MouseEvent> event) {
        this.shape.setOnMouseClicked(event);
    }

    public void setMouseEntered(EventHandler<MouseEvent> event) {
        this.shape.setOnMouseEntered(event);
    }

    public void setMouseExited(EventHandler<MouseEvent> event) {
        this.shape.setOnMouseExited(event);
    }

    public String getId() {
        return netElement.getId();
    }

    public String getName() {
        return netElement.getName();
    }

    public void setName(String name) {
        netElement.setName(name);
    }

    public void delete() {
        onDelete.run(this);
    }
}
