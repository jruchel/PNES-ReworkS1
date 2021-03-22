package org.ekipa.pnes.models.netModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import java.util.List;
@Getter
@AllArgsConstructor

public abstract class NetModel {
    protected List<NetElement> netElements;
    protected List<Arc> arcList;

    /**
     * Tworzy model obecnego typu na podstawie porównania parametry sieci z innym modelem i zamienia te parametry
     * które są możliwe do zmiany, w przeciwnym razie te dane których się nie da przetłumaczyć
     * to stworzy dla nich odpowiedniki i ustawi im wartości
     * @param model model {@link org.ekipa.pnes.models.netModels}
     */
    public abstract void translate(NetModel model);

    /**
     * Tworzy model obecnego typu na podstawie przekazanego modelu,jeżeli jest to możliwe
     * w przeciwnym razie wyrzuca wyjątek o niemożliwej transfomracji
     *@param model model {@link org.ekipa.pnes.models.netModels}
     *@throws ImpossibleTransformationException - wyjątek informujący o niemożliwej transformacji
     */
    public abstract void transform(NetModel model) throws ImpossibleTransformationException;


}
