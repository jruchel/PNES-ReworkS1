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

    public NetElement edit(String actualId, String newId) {
        return editElement(actualId, newId);
    }

    @Override
    public void translate(NetModel model) {

    }

    @Override
    public void transform(NetModel model) throws ImpossibleTransformationException {

    }

    @Override
    protected boolean validateElement(String id) {
        boolean wasValidated = false;
        if (id.startsWith("A")) {
            wasValidated = true;
            Arc arc = (Arc) getElement(id);
            if (arc.getWeight() <= 0) return false;
            try {
                if (arc.getEnd().getClass().equals(arc.getStart().getClass())) return false;
            } catch (Exception e) {
                return false;
            }
        }
        if (id.startsWith("P")) {
            wasValidated = true;
            try {
                Place<Integer> place = (Place<Integer>) getElement(id);
                if (place.getTokenCapacity() < 0) return false;
                if (place.getTokens() > place.getTokenCapacity()) return false;
                if (place.getX() < 0 || place.getY() < 0) return false;
            } catch (Exception e) {
                return false;
            }
        }
        if (id.startsWith("T")) {
            wasValidated = true;
            Transition transition = (Transition) getElement(id);
            if (!transition.getState().equals(Transition.TransitionState.Unready)) return false;
            if (transition.getX() < 0 || transition.getY() < 0) return false;
        }
        return wasValidated;
    }

    @Override
    protected void addTokens(String placeId, Object tokens) {
        if (tokens instanceof Integer && (Integer) tokens > 0 && ((Place) getElement(placeId)).getTokens() instanceof Integer) {
            int placeTokens = (Integer) ((Place) getElement(placeId)).getTokens();
            int newTokens = (Integer) tokens;
            int tokenSet = placeTokens + newTokens;
            ((Place) getElement(placeId)).setTokens(tokenSet);
            if (tokenSet > ((Place) getElement(placeId)).getTokenCapacity())
                ((Place) getElement(placeId)).setTokens(((Place) getElement(placeId)).getTokenCapacity());
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
        if (!transition.getState().equals(Transition.TransitionState.Running)) {
            return false;
        }
        if (transition.getArcs().stream().noneMatch(arc -> arc.getStart().getId().equals(transition.getId()))) {
            return false;
        }
//        if ()

        List<Arc> consumeTokenArcs = transition.getArcs().stream().filter(arc -> arc.getEnd().getId().equals(transition.getId())).collect(Collectors.toList());
        consumeTokenArcs.forEach(arc -> {
            Place<Integer> place = (Place<Integer>) getNetElements().stream().filter(i -> i.getId().equals(arc.getStart().getId())).findFirst().orElse(null);
            setNetElements(getNetElements().stream().peek(i -> {
                if (i.getId().equals(place.getId())) {
                    if (place.getTokens() > 0) {
                        ((Place) i).setTokens((place.getTokens() - (int) arc.getWeight()));
                    }
                }
            }).collect(Collectors.toList()));
        });


        List<Arc> forwardTokenArcs = transition.getArcs().stream().filter(arc -> arc.getStart().getId().equals(transition.getId())).collect(Collectors.toList());
        forwardTokenArcs.forEach(arc -> {

            Place<Integer> place = (Place<Integer>) getNetElements().stream().filter(i -> i.getId().equals(arc.getEnd().getId())).findFirst().orElse(null);

            setNetElements(getNetElements().stream().peek(i -> {
                if (i.getId().equals(place.getId())) {

                    if (place.getTokenCapacity() >= (place.getTokens() + (int) arc.getWeight())) {
                        ((Place) i).setTokens(place.getTokens() + (int) arc.getWeight());
                    }
                }
            }).collect(Collectors.toList()));
        });


        return transition.setUnready();
    }

    @Override
    protected List<Transition> prepareTransitions() {

        List<NetElement> newNetElements = this.getNetElements()
                .stream()
                .peek(i -> {
                    if (i instanceof Transition) {
                        if (canTransitionBeReady(i.getId())) {
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
        return Collections.singletonList(MyRandom.getRandom(transitions.stream()
                .filter(i -> i.getState().equals(Transition.TransitionState.Ready))
                .collect(Collectors.toList())));
    }

    private boolean canTransitionBeReady(String transitionId) {
        if (((Transition) getElement(transitionId)).getArcs().isEmpty()) return false;
        Set<Arc> transitionArcs = ((Transition) getElement(transitionId)).getArcs()
                .stream()
                .filter(arc -> arc.getEnd().getId().equals(transitionId))
                .collect(Collectors.toSet());
        return transitionArcs
                .stream()
                .noneMatch(arc -> getCurrentTokens(arc.getId()) < (int) arc.getWeight());
    }

    private int getCurrentTokens(String arcId) {
        Optional<Place<Integer>> first = getNetElements().stream()
                .filter(i -> i.getId().equals( ((Arc) getElement(arcId)).getStart().getId())).map(i -> (Place<Integer>) i).findFirst();
        if (first.isPresent()) {
            return first.get().getTokens();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public String toString() {
        return "PTNetModel{" +
                ", selectedTransition=" + selectedTransition +
                '}';
    }
}
