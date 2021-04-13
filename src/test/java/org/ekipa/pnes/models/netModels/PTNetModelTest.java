package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.NetObject;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PTNetModelTest {
    private PTNetModel ptNetModel;

    @BeforeEach
    public void initialize() {
        IdGenerator.resetElements();
        ptNetModel = new PTNetModel();


        ptNetModel.createPlace("Wojciech", 300, 600, 131, 25);
        ptNetModel.createPlace("Wojciech", 300, 600, 131, 25);
        ptNetModel.createPlace("Sebastian420", 742, 641, 101, 46);
        ptNetModel.createPlace("Mirek", 5, 7, 10, 2);
        ptNetModel.createTransition("Kuba", 5, 1);
        ptNetModel.createTransition("Kacper", 3, 2);
        ptNetModel.createTransition("Adrian", 91, 5000);

        try {
            ptNetModel.createArc(ptNetModel.getObject("P1"), ptNetModel.getObject("T3"), 1);
            ptNetModel.createArc(ptNetModel.getObject("P2"), ptNetModel.getObject("T2"), 1);
            ptNetModel.createArc(ptNetModel.getObject("P3"), ptNetModel.getObject("T1"), 1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //findObject Tests

    @Test
    public void checkNumberOfFoundObjects() {

        Place<Integer> place = new Place<>("", "Kuba", 3, 5, 13);

        int expected = 3;
        int actual = ptNetModel.findObjects(place).size();

        assertEquals(expected, actual);

    }

    @Test
    public void checkForNothingToFind() {

        Transition transition = new Transition("", "Bartosz", 0, 0);

        transition.setReady();

        int expected = 0;
        int actual = ptNetModel.findObjects(transition).size();

        assertEquals(expected, actual);
    }

    //editObject Tests

    @Test
    public void doesEditObjectChangeFieldsForPlace() {

        ptNetModel.edit(ptNetModel.getObject("P1"), ptNetModel.getObject("P2"));

        String expectedID = ptNetModel.getObject("P1").getId();
        String actualID = ptNetModel.getObject("P2").getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getObject("P1").getName();
        String actualName = ptNetModel.getObject("P2").getName();
        assertEquals(expectedName, actualName);

        double expectedX = ptNetModel.getObject("P1").getX();
        double actualX = ptNetModel.getObject("P2").getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getObject("P1").getY();
        double actualY = ptNetModel.getObject("P2").getY();
        assertEquals(expectedY, actualY);
    }

    @Test
    public void doesEditObjectChangeFieldsForTransition() {

        ptNetModel.editElement(ptNetModel.getObject("T1"), ptNetModel.getObject("T2"));

        String expectedID = ptNetModel.getObject("T1").getId();
        String actualID = ptNetModel.getObject("T2").getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getObject("T1").getName();
        String actualName = ptNetModel.getObject("T2").getName();
        assertEquals(expectedName, actualName);

        double expectedX = ptNetModel.getObject("T1").getX();
        double actualX = ptNetModel.getObject("T2").getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getObject("T1").getY();
        double actualY = ptNetModel.getObject("T2").getY();
        assertEquals(expectedY, actualY);

    }

    @Test
    public void EditObjectBehaveiorForObjectsOfDifferentClass() {

        ptNetModel.edit(ptNetModel.getObject("P1"), ptNetModel.getObject("T3"));

        String expectedID = ptNetModel.getObject("P1").getId();
        String actualID = ptNetModel.getObject("T3").getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getObject("P1").getName();
        String actualName = ptNetModel.getObject("T3").getName();
        assertNotEquals(expectedName, actualName);

        double expectedX = ptNetModel.getObject("P1").getX();
        double actualX = ptNetModel.getObject("T3").getX();
        assertNotEquals(expectedX, actualX);

        double expectedY = ptNetModel.getObject("P1").getY();
        double actualY = ptNetModel.getObject("T3").getY();
        assertNotEquals(expectedY, actualY);
    }

    @Test
    public void doesEditObjectForTheSameObjects() {

        ptNetModel.editElement(ptNetModel.getObject("P1"), ptNetModel.getObject("P1"));
        assertEquals(ptNetModel.getObject("P1"), ptNetModel.getObject("P1"));
    }

    @Test
    public void doesEditObjectChangeFieldsForArcs() {

        ptNetModel.editElement(ptNetModel.getElement("A1"), ptNetModel.getElement("A2"));

        String expectedID = ptNetModel.getElement("A1").getId();
        String actualID = ptNetModel.getElement("A2").getId();
        assertNotEquals(expectedID, actualID);

        NetObject expectedStart = ((Arc) (ptNetModel.getElement("A1"))).getStart();
        NetObject actualStart = ((Arc) (ptNetModel.getElement("A2"))).getStart();
        assertNotEquals(expectedStart, actualStart);

        NetObject expectedEnd = ((Arc) (ptNetModel.getElement("A1"))).getEnd();
        NetObject actualEnd = ((Arc) (ptNetModel.getElement("A2"))).getEnd();
        assertNotEquals(expectedEnd, actualEnd);

        double expectedWeight = ((Arc) (ptNetModel.getElement("A1"))).getWeight();
        double actualWeight = ((Arc) (ptNetModel.getElement("A2"))).getWeight();
        assertEquals(expectedWeight, actualWeight);
    }


    @Test
    public void doesEditObjectTurnsStartAndEndInArc() throws Exception {

        ptNetModel.edit(ptNetModel.getElement("P1"), ptNetModel.createArc(ptNetModel.getObject("P2"), ptNetModel.getObject("T1"), 3));

        Object expectedStart = ptNetModel.getObject("P1");
        Object actualStart = ptNetModel.getObject("P2");
        assertNotEquals(expectedStart, actualStart);

        Object expectedEnd = ptNetModel.getObject("T3");
        Object actualEnd = ptNetModel.getObject("T1");
        assertNotEquals(expectedEnd, actualEnd);

    }

    @Test
    public void validationForNegativeValues() {

        Place<Integer> newplace = new Place<>("", "name", 3, 5, -30, -4);
        ptNetModel.edit(ptNetModel.getObject("P1"), newplace);


        double expectedX = 300;
        double actualX = ptNetModel.getObject("P1").getX();

        assertEquals(expectedX, actualX);

        double expectedY = 600;
        double actualY = ptNetModel.getObject("P1").getY();

        assertEquals(expectedY, actualY);

        int expected = 131;
        int actual = ((Place) ptNetModel.getObject("P1")).getTokenCapacity();

        assertEquals(expected, actual);
        if (ptNetModel.getObject("P1").getClass().equals(new Place<Integer>().getClass())) {
            expected = 25;
            actual = ((Place<Integer>) ptNetModel.getObject("P1")).getTokens();
        } else {
            assertThrows(Exception.class, () -> ((Place<Integer>) ptNetModel.getObject("P1")).getTokens());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void validationForAddingTokens() {

        Place<Integer> place = ptNetModel.createPlace("", 2.5, 2, 20, 2);
        ptNetModel.addTokens(place, 35);
        int expected = 20;
        int actual = place.getTokens();
        assertEquals(expected, actual);

    }

    @Test
    public void validationForSetTransitionReady() {

        Transition transition = ptNetModel.createTransition("Fludu", 10, 20);
        Transition.TransitionState actual = transition.getState();
        Transition.TransitionState expected = Transition.TransitionState.Ready;
        assertNotEquals(expected, actual);
    }

    @Test
    public void validationOfPrepareTransitions(){
        List<Transition> expected = ptNetModel.getTransitionsWithState(Transition.TransitionState.Unready);
        List<List<NetModel>> example = NetModel.simulate(ptNetModel, 1);
        List<Transition> actual = example.get(0).get(0).prepareTransitions();
        for(int i = 0; i < expected.size(); i++){
            expected.get(i).setReady();
        }
        assertEquals(expected, actual);
    }

        @Test
    public void testForRunTransition(){
        ptNetModel.createTransition("Magnus", 1, 2).setReady();
        try {
            ptNetModel.createArc(ptNetModel.getObject("T4"), ptNetModel.getObject("P1"), 1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        ptNetModel.runTransition((Transition) ptNetModel.getObject("T4"));
        int expected = 26;
        int actual = (int) ((Place)(ptNetModel.getObject("P1"))).getTokens();
        assertEquals(expected, actual);

    }


    @Test
    public void checkTheValuesForTokens() throws Exception {
        IdGenerator.resetElements();
        PTNetModel ptNetModel1 = new PTNetModel();
        ptNetModel1.createTransition("Arek", 3, 5);
        ptNetModel1.createPlace("Kacper", 4, 2, 15, 2);
        ptNetModel1.createTransition("Krystian", 2, 3);
        ptNetModel1.createPlace("Kamil", 3, 2, 15, 1);
        ptNetModel1.createArc(ptNetModel1.getObject("P1"), ptNetModel1.getObject("T1"), 1);
        ptNetModel1.createArc(ptNetModel1.getObject("T1"), ptNetModel1.getObject("P2"), 1);
        ptNetModel1.createArc(ptNetModel1.getObject("P2"), ptNetModel1.getObject("T2"), 1);
        ptNetModel1.createArc(ptNetModel1.getObject("T2"), ptNetModel1.getObject("P1"), 1);
        List<List<NetModel>> example = NetModel.simulate(ptNetModel1, 3);
        System.out.println(((Place)ptNetModel1.getObject("P1")).getTokens());
        System.out.println(((Place)ptNetModel1.getObject("P2")).getTokens());
        if(((int)((Place)ptNetModel1.getObject("P1")).getTokens() == 1)){
            int actual = ((int)((Place)ptNetModel1.getObject("P2")).getTokens());
            int expected = 2;
            assertEquals(expected, actual);
        }
        if(((int)((Place)ptNetModel1.getObject("P1")).getTokens() == 2)){
            int actual = ((int)((Place)ptNetModel1.getObject("P2")).getTokens());
            int expected = 1;
            assertEquals(expected, actual);
        }
        if(((int)((Place)ptNetModel1.getObject("P1")).getTokens() == 0)){
            int actual = ((int)((Place)ptNetModel1.getObject("P2")).getTokens());
            int expected = 3;
            assertEquals(expected, actual);
        }
        if(((int)((Place)ptNetModel1.getObject("P1")).getTokens() == 3)){
            int actual = ((int)((Place)ptNetModel1.getObject("P2")).getTokens());
            int expected = 0;
            assertEquals(expected, actual);
        }

    }

    @Test
    public void validationOfUnreadyTransitionStatesAfterSimulation() {
        List<List<NetModel>> example = NetModel.simulate(ptNetModel, 1);
        List<Transition> actual = example.get(0).get(0).getTransitionsWithState(Transition.TransitionState.Unready);
        List<Transition> expected = ptNetModel.getTransitionsWithState(Transition.TransitionState.Unready);
        assertEquals(expected, actual);
    }

    //robić testy na simulation, wholeStep

    @Test
    public void checkTransitionCanBeReadyUsingNextStepWithoutArc() {

        Transition transition = ptNetModel.createTransition("Fludu", 10, 20);

        System.out.println(transition.getId());


        Transition.TransitionState actual = transition.getState();
        Transition.TransitionState expected = Transition.TransitionState.Unready;

        assertEquals(expected, actual);

    }


}