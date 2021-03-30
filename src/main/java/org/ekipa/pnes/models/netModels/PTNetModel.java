package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.utils.IdGenerator;

import java.util.List;

public class PTNetModel extends NetModel {

    public PTNetModel() {
        super();
    }

    private PTNetModel(List<NetElement> netElements, List<Arc> arcList) {
        super(netElements, arcList);
    }

    public Arc createArc(NetElement start, NetElement end, int weight) throws Exception {
        return (Arc) addObject(IdGenerator.setArcId(new Arc(start, end, weight)));
    }

    public Transition createTransition(String name, double x, double y) {
        return (Transition) addObject(IdGenerator.setElementId(new Transition("", name, x, y)));
    }

    public Place<Integer> createPlace(String name, double x, double y, int tokenCapacity, int token) {
        return (Place<Integer>) addObject(IdGenerator.setElementId(new Place<>("", name, x, y, tokenCapacity, token)));
    }

    public void deleteById(String id) {
        netElements.stream().filter(net -> net.getId().equals(id)).forEach(this::deleteObject);
        arcList.stream().filter(arc -> arc.getId().equals(id)).forEach(this::deleteObject);
    }

    public void deleteByName(String name) {
        netElements.stream().filter(net -> net.getName().equals(name)).forEach(this::deleteObject);
    }

    public Object edit(Object actualObject, Object newObject) {
        return editObject(actualObject, newObject);
    }

    @Override
    public void translate(NetModel model) {

    }

    @Override
    public void transform(NetModel model) throws ImpossibleTransformationException {

    }

    @Override
    protected boolean validateObject(Object o) {
        boolean wasValidated = false;
        if (o instanceof Arc) {
            wasValidated = true;
            Arc arc = (Arc) o;
            if (arc.getWeight() <= 0) return false;
            try {
                if (arc.getEnd().getClass().equals(arc.getStart().getClass())) return false;
            } catch (Exception e) {
                return false;
            }
        }
        if (o instanceof Place) {
            wasValidated = true;
            try {
                Place<Integer> place = (Place<Integer>) o;
                if (place.getTokenCapacity() < 0) return false;
                if (place.getTokens() > place.getTokenCapacity()) return false;
            } catch (Exception e) {
                return false;
            }
        }
        if (o instanceof Transition) {
            wasValidated = true;
            Transition transition = (Transition) o;
            if (!transition.getState().equals(Transition.TransitionState.Unready)) return false;
        }
        if (o instanceof NetElement) {
            NetElement netElement = (NetElement) o;
            if (netElement.getX() < 0 || netElement.getY() < 0) return false;
        }
        return wasValidated;
    }

    @Override
    protected void addTokens(Place place, Object tokens) {
        if (tokens instanceof Integer && (Integer) tokens > 0 && place.getTokens() instanceof Integer) {
            int placeTokens = (Integer) place.getTokens();
            int newTokens = (Integer) tokens;
            if (placeTokens + newTokens <= place.getTokenCapacity()) place.setTokens(placeTokens + newTokens);
        }
    }
}
