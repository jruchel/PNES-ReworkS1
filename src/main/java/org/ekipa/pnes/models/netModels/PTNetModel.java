package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.utils.IdGenerator;
import org.ekipa.pnes.utils.MyRandom;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PTNetModel extends NetModel {
    private Transition selectedTransition;

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
            int tokenSet = placeTokens + newTokens;
            place.setTokens(tokenSet);
            if (tokenSet > place.getTokenCapacity()) place.setTokens(place.getTokenCapacity());
        }

    }

    private Transition selectTransitionToActivate() {
        if (selectedTransition != null && selectedTransition.getState().equals(Transition.TransitionState.Ready))
            return selectedTransition;
        return MyRandom.getRandom(getTransitionsWithState(Transition.TransitionState.Ready));
    }

    private boolean runTransition(Transition transition) {
        if (!transition.getState().equals(Transition.TransitionState.Ready)) return false;
        if (!transition.setRunning()) return false;
        List<Arc> consumeTokenArcs = transition.getArcs().stream().filter(arc -> arc.getEnd() == transition).collect(Collectors.toList());
        consumeTokenArcs.forEach(arc -> {
            Place<Integer> place = (Place<Integer>) arc.getStart();
            place.setTokens((place.getTokens() - (int) arc.getWeight()));
        });
        List<Arc> forwardTokenArcs = transition.getArcs().stream().filter(arc -> arc.getStart() == transition).collect(Collectors.toList());
        forwardTokenArcs.forEach(arc -> {
            addTokens((Place) arc.getEnd(), arc.getWeight());
        });
        return transition.setUnready();
    }

    @Override
    protected NetModel nextStep() {
        List<Transition> readyTransitions = getTransitionsWithState(Transition.TransitionState.Unready).stream().filter(this::canTransitionBeReady).collect(Collectors.toList());
        readyTransitions.forEach(Transition::setReady);
        runTransition(selectTransitionToActivate());
        return this;
    }

    private boolean canTransitionBeReady(Transition transition) {
        if (transition.getArcs().isEmpty()) return false;
        Set<Arc> transitionArcs = transition.getArcs().stream().filter(arc -> arc.getEnd().equals(transition)).collect(Collectors.toSet());
        return transitionArcs.stream().noneMatch(arc -> ((Place<Integer>) arc.getStart()).getTokens() < (int) arc.getWeight());
    }

    private List<Transition> getTransitionsWithState(Transition.TransitionState state) {
        return netElements.stream()
                .filter(element -> element instanceof Transition)
                .filter(transition -> ((Transition) transition).getState().equals(state))
                .map(netElement -> {
                    if (netElement instanceof Transition) return (Transition) netElement;
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
