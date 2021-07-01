package org.ekipa.pnes.models.netModels;

import io.swagger.models.auth.In;
import org.ekipa.pnes.models.elements.*;
import org.ekipa.pnes.utils.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PTNetModelTest {
    private PTNetModel sut;

    @BeforeEach
    public void initialize() {
        IdGenerator.resetElements();
    }

    public PTNetModel createDefaultNet() throws Exception {
        sut = new PTNetModel();
        sut.createPlace("Wojciech", 300, 600, 131, 25);
        sut.createPlace("Sebastian420", 742, 641, 101, 46);
        sut.createPlace("Mirek", 5, 7, 10, 2);
        sut.createTransition("Kuba", 5, 1);
        sut.createTransition("Kacper", 3, 2);
        sut.createTransition("Adrian", 91, 5000);

        sut.createArc(sut.getObject("P1"), sut.getObject("T3"), 1);
        sut.createArc(sut.getObject("P2"), sut.getObject("T2"), 1);
        sut.createArc(sut.getObject("P3"), sut.getObject("T1"), 1);

        return sut;
    }

//    @Test
//    public void doesEditObjectChangeFieldsForPlace() throws Exception {
//        ptNetModel = createDefaultNet();
//
//        ptNetModel.edit("P1", "P2");
//
//        String expectedID = ptNetModel.getObject("P1").getId();
//        String actualID = ptNetModel.getObject("P2").getId();
//        assertNotEquals(expectedID, actualID);
//
//        String expectedName = ptNetModel.getObject("P1").getName();
//        String actualName = ptNetModel.getObject("P2").getName();
//        assertEquals(expectedName, actualName);
//
//        double expectedX = ptNetModel.getObject("P1").getX();
//        double actualX = ptNetModel.getObject("P2").getX();
//        assertEquals(expectedX, actualX);
//
//        double expectedY = ptNetModel.getObject("P1").getY();
//        double actualY = ptNetModel.getObject("P2").getY();
//        assertEquals(expectedY, actualY);
//
//        int expectedTokens = ((Place<Integer>) (ptNetModel.getObject("P1"))).getTokens();
//        int actualTokens = ((Place<Integer>) (ptNetModel.getObject("P2"))).getTokens();
//        assertEquals(expectedTokens, actualTokens);
//
//        int expectedTokenCapacity = ((Place<Integer>) (ptNetModel.getObject("P1"))).getTokenCapacity();
//        int actualTokenCapacity = ((Place<Integer>) (ptNetModel.getObject("P2"))).getTokenCapacity();
//        assertEquals(expectedTokenCapacity, actualTokenCapacity);
//    }

//    @Test
//    public void doesEditObjectChangeFieldsForTransition() throws Exception {
//        ptNetModel = createDefaultNet();
//
//       ptNetModel.editElement("T1", "T2");
//
//        String expectedID = ptNetModel.getObject("T1").getId();
//        String actualID = ptNetModel.getObject("T2").getId();
//        assertNotEquals(expectedID, actualID);
//
//        String expectedName = ptNetModel.getObject("T1").getName();
//        String actualName = ptNetModel.getObject("T2").getName();
//        assertEquals(expectedName, actualName);
//
//        double expectedX = ptNetModel.getObject("T1").getX();
//        double actualX = ptNetModel.getObject("T2").getX();
//        assertEquals(expectedX, actualX);
//
//        double expectedY = ptNetModel.getObject("T1").getY();
//        double actualY = ptNetModel.getObject("T2").getY();
//        assertEquals(expectedY, actualY);
//
//    }
//
//    @Test
//    public void EditObjectBehaviorForObjectsOfDifferentClass() throws Exception {
//        ptNetModel = createDefaultNet();
//
//        ptNetModel.edit("P1", "T3");
//
//        String expectedID = ptNetModel.getObject("P1").getId();
//        String actualID = ptNetModel.getObject("T3").getId();
//        assertNotEquals(expectedID, actualID);
//
//        String expectedName = ptNetModel.getObject("P1").getName();
//        String actualName = ptNetModel.getObject("T3").getName();
//        assertNotEquals(expectedName, actualName);
//
//        double expectedX = ptNetModel.getObject("P1").getX();
//        double actualX = ptNetModel.getObject("T3").getX();
//        assertNotEquals(expectedX, actualX);
//
//        double expectedY = ptNetModel.getObject("P1").getY();
//        double actualY = ptNetModel.getObject("T3").getY();
//        assertNotEquals(expectedY, actualY);
//    }
//
//    @Test
//    public void doesEditObjectForTheSameObjects() throws Exception {
//        ptNetModel = createDefaultNet();
//
//        ptNetModel.editElement("P1", "P1");
//        assertEquals(ptNetModel.getObject("P1"), ptNetModel.getObject("P1"));
//    }
//
//    @Test
//    public void doesEditObjectChangeFieldsForArcs() throws Exception {
//        ptNetModel = createDefaultNet();
//
//        ptNetModel.editElement("A1", "A2");
//
//        String expectedID = ptNetModel.getElement("A1").getId();
//        String actualID = ptNetModel.getElement("A2").getId();
//        assertNotEquals(expectedID, actualID);
//
//        NetObject expectedStart = ((Arc) (ptNetModel.getElement("A1"))).getStart();
//        NetObject actualStart = ((Arc) (ptNetModel.getElement("A2"))).getStart();
//        assertNotEquals(expectedStart, actualStart);
//
//        NetObject expectedEnd = ((Arc) (ptNetModel.getElement("A1"))).getEnd();
//        NetObject actualEnd = ((Arc) (ptNetModel.getElement("A2"))).getEnd();
//        assertNotEquals(expectedEnd, actualEnd);
//
//        double expectedWeight = ((Arc) (ptNetModel.getElement("A1"))).getWeight();
//        double actualWeight = ((Arc) (ptNetModel.getElement("A2"))).getWeight();
//        assertEquals(expectedWeight, actualWeight);
//    }
//
//
//    @Test
//    public void doesEditObjectChangeStartAndEndInArc() throws Exception {
//        ptNetModel = createDefaultNet();
//        ptNetModel.edit("P1", ptNetModel.createArc(ptNetModel.getObject("P2"), ptNetModel.getObject("T1"), 3).getId());
//
//        Object expectedStart = ptNetModel.getObject("P1");
//        Object actualStart = ptNetModel.getObject("P2");
//        assertNotEquals(expectedStart, actualStart);
//
//        Object expectedEnd = ptNetModel.getObject("T3");
//        Object actualEnd = ptNetModel.getObject("T1");
//        assertNotEquals(expectedEnd, actualEnd);
//
//    }
//
//    @Test
//    public void validationForNegativeValues() throws Exception {
//        ptNetModel = createDefaultNet();
//
//        Place<Integer> newPlace = new Place<>("P10", "name", 3, 5, -30, -4);
//        ptNetModel.edit("P1", newPlace.getId());
//
//
//        double expectedX = 300;
//        double actualX = ptNetModel.getObject("P1").getX();
//
//        assertEquals(expectedX, actualX);
//
//        double expectedY = 600;
//        double actualY = ptNetModel.getObject("P1").getY();
//
//        assertEquals(expectedY, actualY);
//
//        int expected = 131;
//        int actual = ((Place) ptNetModel.getObject("P1")).getTokenCapacity();
//
//        assertEquals(expected, actual);
//        if (ptNetModel.getObject("P1").getClass().equals(new Place<Integer>().getClass())) {
//            expected = 25;
//            actual = ((Place<Integer>) ptNetModel.getObject("P1")).getTokens();
//        } else {
//            assertThrows(Exception.class, () -> ((Place<Integer>) ptNetModel.getObject("P1")).getTokens());
//        }
//        assertEquals(expected, actual);
//    }

    @Test
    public void validationForAddingTokens() throws Exception {
        sut = createDefaultNet();

        Place<Integer> place = sut.createPlace("P20", 2.5, 2, 20, 2);
        sut.addTokens(place.getId(), 35);
        int expected = 20;
        int actual = place.getTokens();
        assertEquals(expected, actual);

    }

    @Test
    public void validationForSetTransitionReady() {

        Transition transition = new Transition("", "Flood", 10, 20);
        Transition.TransitionState actual = transition.getState();
        Transition.TransitionState expected = Transition.TransitionState.Ready;
        assertNotEquals(expected, actual);
    }

    @Test
    public void validationOfPrepareTransitions() throws Exception {
        sut = createDefaultNet();
        List<Transition> expected = sut.getTransitionsWithState(Transition.TransitionState.Unready);
        List<List<NetModel>> example = NetModel.simulate(sut, 1);
        List<Transition> actual = example.get(0).get(0).prepareTransitions();
        for (int i = 0; i < expected.size(); i++) {
            expected.get(i).setUnready();
        }
        for (int i = 0; i < expected.size(); i++) {
            expected.get(i).setReady();
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testForRunTransition() throws Exception {
        sut = createDefaultNet();
        sut.createTransition("Magnus", 1, 2).setReady();
        ((Transition) (sut.getObject("T4"))).setRunning();
        try {
            sut.createArc(sut.getObject("T4"), sut.getObject("P1"), 1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        sut.runTransition((Transition) sut.getObject("T4"));
        int expected = 26;
        int actual = (int) ((Place) (sut.getObject("P1"))).getTokens();
        assertEquals(expected, actual);
    }


    @Test
    public void checkTheValuesForTokens() throws Exception {
        PTNetModel ptNetModel1 = new PTNetModel();
        ptNetModel1.createTransition("Arek", 3, 5);
        ptNetModel1.createPlace("Kacper", 4, 2, 15, 2);
        ptNetModel1.createTransition("Krystian", 2, 3);
        ptNetModel1.createPlace("Kamil", 3, 2, 15, 1);
        ptNetModel1.createArc(ptNetModel1.getObject("P1"), ptNetModel1.getObject("T1"), 1);
        ptNetModel1.createArc(ptNetModel1.getObject("T1"), ptNetModel1.getObject("P2"), 1);
        ptNetModel1.createArc(ptNetModel1.getObject("P2"), ptNetModel1.getObject("T2"), 1);
        ptNetModel1.createArc(ptNetModel1.getObject("T2"), ptNetModel1.getObject("P1"), 1);
        NetModel.simulate(ptNetModel1, 3);

        if (((int) ((Place) ptNetModel1.getObject("P1")).getTokens() == 1)) {
            int actual = ((int) ((Place) ptNetModel1.getObject("P2")).getTokens());
            int expected = 2;
            assertEquals(expected, actual);
        }
        if (((int) ((Place) ptNetModel1.getObject("P1")).getTokens() == 2)) {
            int actual = ((int) ((Place) ptNetModel1.getObject("P2")).getTokens());
            int expected = 1;
            assertEquals(expected, actual);
        }
        if (((int) ((Place) ptNetModel1.getObject("P1")).getTokens() == 0)) {
            int actual = ((int) ((Place) ptNetModel1.getObject("P2")).getTokens());
            int expected = 3;
            assertEquals(expected, actual);
        }
        if (((int) ((Place) ptNetModel1.getObject("P1")).getTokens() == 3)) {
            int actual = ((int) ((Place) ptNetModel1.getObject("P2")).getTokens());
            int expected = 0;
            assertEquals(expected, actual);
        }

    }

    @Test
    public void validationOfUnreadyTransitionStatesAfterSimulation() throws Exception {
        sut = createDefaultNet();
        List<List<NetModel>> example = NetModel.simulate(sut, 1);
        List<Transition> actual = example.get(0).get(3).getTransitionsWithState(Transition.TransitionState.Unready);
        List<Transition> expected = sut.getTransitionsWithState(Transition.TransitionState.Unready);
        assertEquals(expected, actual);
    }

    @Test
    public void checkTransitionCanBeReadyUsingNextStepWithoutArc() {

        Transition transition = new Transition("", "Flood", 10, 20);

        Transition.TransitionState actual = transition.getState();
        Transition.TransitionState expected = Transition.TransitionState.Unready;

        assertEquals(expected, actual);

    }


    public PTNetModel preparinginfiniteGeneratingTransitionNet() throws Exception {
        sut = new PTNetModel();
        sut.createTransition("Generator", 420, 699);
        sut.createPlace("Odbiorca", 430, 700, 100, 0);
        sut.createArc(sut.getObject("T1"), sut.getObject("P1"), 1);

        return sut;
    }

    @Test
    public void validationForNetWithGeneratingTransition() throws Exception {
        sut = preparinginfiniteGeneratingTransitionNet();

        List<List<NetModel>> example = NetModel.simulate(sut, 6);

        List<NetModel> lastListOfSimulation = example.get(example.size()-1);

        NetModel lastElement = lastListOfSimulation.get(lastListOfSimulation.size()-1);



        int expected = 2;


    }

    private PTNetModel preparingInfinitelyGeneratingTransitionWithOnePlaceNoLimit() throws Exception {
        sut = new PTNetModel();
        sut.createPlace("Dawca", 10, 20, 0, 20);
        sut.createTransition("Pochlaniacz", 20, 20);
        sut.createArc(sut.getObject("T1"), sut.getObject("P1"), 1);

        return sut;
    }



    @Test
    public void shouldGenerateTokensIndefinitely() throws Exception {
       sut = preparingInfinitelyGeneratingTransitionWithOnePlaceNoLimit();
       PTNetModel lastStep = (PTNetModel)(getLastSimulationStep(NetModel.simulate(sut,3)));
       int expected = 23;
       int actual = (((Place<Integer>)(lastStep.getElement("P1"))).getTokens());

       assertEquals(expected,actual);

    }

    private PTNetModel preparingInfinitelyGeneratingTransitionWithOnePlaceWithLimit() throws Exception {
        sut = new PTNetModel();
        sut.createPlace("Dawca", 10, 20, 5, 20);
        sut.createTransition("Pochlaniacz", 20, 20);
        sut.createArc(sut.getObject("T1"), sut.getObject("P1"), 1);

        return sut;
    }



    @Test
    public void tokensOverLimit() throws Exception {
        sut = preparingInfinitelyGeneratingTransitionWithOnePlaceWithLimit();
        PTNetModel lastStep = (PTNetModel)(getLastSimulationStep(NetModel.simulate(sut,3)));
        int expected = 5;
        int actual = (((Place<Integer>)(lastStep.getElement("P1"))).getTokens());

        assertEquals(expected,actual);

    }

    private NetModel getLastSimulationStep(List<List<NetModel>> list) {
        List<NetModel> lastCycle = list.get(list.size()-1);
        return lastCycle.get(lastCycle.size()-1);
    }
}