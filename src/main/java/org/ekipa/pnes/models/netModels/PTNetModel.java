package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.models.elements.token.IntegerTokenValue;
import org.ekipa.pnes.models.elements.token.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PTNetModel extends NetModel {

    public PTNetModel(List<NetElement> netElements, List<Arc> arcList) {
        super(netElements, arcList);
    }

    private void createArc(String id, NetElement start, NetElement end, int weight) throws Exception {
        arcList.add(new Arc(id, start, end, weight));
    }

    private void createTransition(String id, String name, double x, double y, double rotationAngle) {
        netElements.add(new Transition(id, name, x, y, rotationAngle));
    }

    private void createPlace(String id, String name, double x, double y, int tokenCapacity, List<Token<IntegerTokenValue>> tokens) {
        netElements.add(new Place<>(id, name, x, y, tokenCapacity, tokens));
    }

    public void deleteById(String id) {
        netElements.stream().filter(net -> net.getId().equals(id)).forEach(this::deleteObject);
        arcList.stream().filter(arc -> arc.getId().equals(id)).forEach(this::deleteObject);
    }

    public void deleteByName(String name) {
        netElements.stream().filter(net -> net.getName().equals(name)).forEach(this::deleteObject);
    }

    private void deleteObject(Object object) {
        if (object instanceof Arc) {
            arcList = arcList.stream().filter(e -> !e.equals(object)).collect(Collectors.toList());
        }
        if (object instanceof NetElement) {
            netElements = netElements.stream().filter(net -> !net.equals(object)).collect(Collectors.toList());
        }
    }



    @Override
    public void translate(NetModel model) {

    }

    @Override
    public void transform(NetModel model) throws ImpossibleTransformationException {

    }
}
