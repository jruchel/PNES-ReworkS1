package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.token.IntegerTokenValue;
import org.ekipa.pnes.models.elements.token.Token;

import java.util.ArrayList;
import java.util.List;

public class PTNet extends NetModel {
    public PTNet(List<NetElement> netElements) {
        super(netElements);

    }

    @Override
    public void translate(NetModel model) {

    }

    @Override
    public void transform(NetModel model) throws ImpossibleTransformationException {

    }

    public Arc createArc(NetElement start, NetElement end, int weight) throws Exception {
        return new Arc(start, end, weight);
    }


    public Place createPlace(String id, String name, double x, double y, List<Arc> arcs, int tokenCapacity) {
        return new Place(id, name, x, y, arcs, tokenCapacity);
    }

    public Place createPlace(String id, String name, double x, double y, int tokenCapacity) {
        return new Place(id, name, x, y, tokenCapacity);
    }

    public Place createPlace(String id, String name, double x, double y, List<Arc> arcs, int tokenCapacity, List<Token<IntegerTokenValue>> tokens) {
        return new Place(id, name, x, y, arcs, tokenCapacity, tokens);
    }

    public Place createPlace(String id, String name, double x, double y, int tokenCapacity, List<Token<IntegerTokenValue>> tokens) {
        return new Place(id, name, x, y, tokenCapacity, tokens);
    }
}
