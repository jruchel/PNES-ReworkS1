package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.utils.IdGenerator;

import java.util.List;

public class PTNetModel<TokenValue> extends NetModel {

    public PTNetModel() {
        super();
    }

    private PTNetModel(List<NetElement> netElements, List<Arc> arcList) {
        super(netElements, arcList);
    }

    public Arc createArc(NetElement start, NetElement end, int weight) throws Exception {
        return (Arc) addObject(IdGenerator.setArcId(new Arc(start, end, weight)));
    }

    public Transition createTransition(String name, double x, double y, double rotationAngle) {
        return (Transition) addObject(IdGenerator.setElementId(new Transition("", name, x, y, rotationAngle)));
    }

    public Place<TokenValue> createPlace(String name, double x, double y, int tokenCapacity, int token) {
        return (Place<TokenValue>) addObject(IdGenerator.setElementId(new Place<>("", name, x, y, tokenCapacity, token)));
    }

    public void deleteById(String id) {
        netElements.stream().filter(net -> net.getId().equals(id)).forEach(this::deleteObject);
        arcList.stream().filter(arc -> arc.getId().equals(id)).forEach(this::deleteObject);
    }

    public void deleteByName(String name) {
        netElements.stream().filter(net -> net.getName().equals(name)).forEach(this::deleteObject);
    }

    @Override
    public void translate(NetModel model) {

    }

    @Override
    public void transform(NetModel model) throws ImpossibleTransformationException {

    }
}
