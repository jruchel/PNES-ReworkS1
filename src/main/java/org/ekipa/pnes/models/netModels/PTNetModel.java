package org.ekipa.pnes.models.netModels;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.ekipa.pnes.models.elements.*;
import org.ekipa.pnes.models.exceptions.ImpossibleTransformationException;
import org.ekipa.pnes.models.exceptions.ProhibitedConnectionException;
import org.ekipa.pnes.utils.IdGenerator;
import org.ekipa.pnes.utils.MyRandom;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonDeserialize(using = PTNetModelDeserializer.class)
public class PTNetModel extends NetModel {

    private Transition selectedTransition;

    public PTNetModel() {
        super();
    }

    public PTNetModel(List<NetElement> netElements) {
        super(netElements);
    }

    public Arc createArc(NetObject start, NetObject end, int weight) throws ProhibitedConnectionException {
        return (Arc) addElement(IdGenerator.setElementId(new Arc(start, end, weight)));
    }

    public Transition createTransition(String name, double x, double y) {
        return (Transition) addElement(IdGenerator.setElementId(new Transition("", name, x, y)));
    }

    public Place<Integer> createPlace(String name, double x, double y, int tokenCapacity, int token) {
        return (Place<Integer>) addElement(IdGenerator.setElementId(new Place<>("", name, x, y, tokenCapacity, token)));
    }

    public NetElement edit(NetElement actualObject, NetElement newObject) {
        return editElement(actualObject, newObject);
    }

    @Override
    public void translate(NetModel model) {

    }

    @Override
    public void transform(NetModel model) throws ImpossibleTransformationException {

    }

    @Override
    protected boolean validateElement(NetElement o) {
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
        if (o instanceof NetObject) {
            NetObject netObject = (NetObject) o;
            if (netObject.getX() < 0 || netObject.getY() < 0) return false;
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

    @Override
    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    @Override
    public NetModel deserialize(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, this.getClass());
    }


    @Override
    protected boolean runTransition(Transition transition) {
        if (!transition.getState().equals(Transition.TransitionState.Ready)) {
            return false;
        }
        if (transition.getArcs().stream().noneMatch(arc -> arc.getStart().getId().equals(transition.getId()))) {
            return false;
        }
        if (!transition.setRunning()) {
            return false;
        }
        List<Arc> consumeTokenArcs = transition.getArcs().stream().filter(arc -> arc.getEnd().getId().equals(transition.getId())).collect(Collectors.toList());
        consumeTokenArcs.forEach(arc -> {
            Place<Integer> place = (Place<Integer>) arc.getStart();
            place.setTokens((place.getTokens() - (int) arc.getWeight()));
        });
        List<Arc> forwardTokenArcs = transition.getArcs().stream().filter(arc -> arc.getStart().getId().equals(transition.getId())).collect(Collectors.toList());
        forwardTokenArcs.forEach(arc -> {
            addTokens((Place) arc.getEnd(), arc.getWeight());
        });
        return transition.setUnready();
    }

    @Override
    protected List<Transition> prepareTransitions() {

        List<NetElement> newNetElements = this.getNetElements()
                .stream()
                .peek(i -> {
                    if (i instanceof Transition) {
                        if (canTransitionBeReady((Transition) i)) {
                            ((Transition) i).setReady();
                        }
                    }
                }).collect(Collectors.toList());
        this.setNetElements(newNetElements);


        return newNetElements.stream().filter(i -> i instanceof Transition).map(i -> ((Transition) i)).collect(Collectors.toList());
    }

    @Override
    protected List<Transition> selectTransitionsToRun(List<Transition> transitions) {
        if (selectedTransition != null && selectedTransition.getState().equals(Transition.TransitionState.Ready))
            return Collections.singletonList(selectedTransition);
        return Collections.singletonList(MyRandom.getRandom(transitions));
    }

    private boolean canTransitionBeReady(Transition transition) {
        if (transition.getArcs().isEmpty()) return false;
        Set<Arc> transitionArcs = transition.getArcs().stream().filter(arc -> arc.getEnd().equals(transition)).collect(Collectors.toSet());
        return transitionArcs.stream().noneMatch(arc -> ((Place<Integer>) arc.getStart()).getTokens() < (int) arc.getWeight());
    }

    @Override
    public String toString() {
        return "PTNetModel{" +
                ", selectedTransition=" + selectedTransition +
                '}';
    }
}
