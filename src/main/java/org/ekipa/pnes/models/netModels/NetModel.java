package org.ekipa.pnes.models.netModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ekipa.pnes.models.elements.NetElement;
import java.util.List;
@Getter
@AllArgsConstructor

public abstract class NetModel {
    private List<NetElement> netElements;


    public abstract void translate(NetModel model);

    public abstract void transform(NetModel model);


}
