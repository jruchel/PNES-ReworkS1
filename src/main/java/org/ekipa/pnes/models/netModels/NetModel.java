package org.ekipa.pnes.models.netModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ekipa.pnes.models.elements.NetElement;
import java.util.List;
@Getter
@AllArgsConstructor

public abstract class NetModel {
    private List<NetElement> netElements;

    /**
     *
     * @param model  Przyjmuje model {@link org.ekipa.pnes.models.netModels}
     * Porównuje parametry sieci z innym modelem i zamienia te parametry które są możliwe do zmiany, w przeciwnym
     * razie te dane których się nie da przetłumaczyć, to stworzy dla nich odpowiedniki i ustawi im wartości
     *
     */
    public abstract void translate(NetModel model);

    /**
     *
     *@param model Przyjmuje model ({@link org.ekipa.pnes.models.netModels})
     * i zamienia ten model w inny model, jeżeli jest to możliwe, w przeciwnym razie wyrzuca wyjątek
     * o niemożliwej transfomracji
     *@throws ImpossibleTransformationException - wyjątek informujący o niemożliwej transformacji
     */
    public abstract void transform(NetModel model) throws ImpossibleTransformationException;


}
