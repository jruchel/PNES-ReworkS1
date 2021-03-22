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
     * @param model Pobiera model ({@link org.ekipa.pnes.models.netModels})
     */

    public abstract void translate(NetModel model);

    /**
     *
     *@param model Pobiera model ({@link org.ekipa.pnes.models.netModels})
     *@throws ImpossibleTransformationException - wyrzuca komunikat ze jeden model nie moze byc transformowany w inny
     */

    public abstract void transform(NetModel model) throws ImpossibleTransformationException;


}
