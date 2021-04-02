package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.NetObject;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PTNetModelTest {
    private PTNetModel ptNetModel;

    @BeforeEach
    public void initialize() {
        ptNetModel = new PTNetModel();
        ptNetModel.createPlace("Wojciech", 300, 600, 131, 25);
        ptNetModel.createPlace("Sebastian420", 742, 641, 101, 46);
        ptNetModel.createPlace("Mirek", 5, 7, 10, 2);
        ptNetModel.createTransition("Kuba", 5, 1).setUnready();
        ptNetModel.createTransition("Kacper", 3, 2).setUnready();
        ptNetModel.createTransition("Adrian", 91, 5000).setUnready();

        try {
            ptNetModel.createArc(ptNetModel.getObject(0), ptNetModel.getObject(5), 5);
            ptNetModel.createArc(ptNetModel.getObject(1), ptNetModel.getObject(4), 7);
            ptNetModel.createArc(ptNetModel.getObject(2), ptNetModel.getObject(3), 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //findObject Tests

    @Test
    public void checkNumberOfFoundObjects() {
        Place<Integer> place = new Place<>("", "Kuba", 3, 5, 13);

        int expected = 2;
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
        ptNetModel.edit(ptNetModel.getObject(0), ptNetModel.getObject(1));

        String expectedID = ptNetModel.getObject(0).getId();
        String actualID = ptNetModel.getObject(1).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getObject(0).getName();
        String actualName = ptNetModel.getObject(1).getName();
        assertEquals(expectedName, actualName);

        double expectedX = ptNetModel.getObject(0).getX();
        double actualX = ptNetModel.getObject(1).getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getObject(0).getY();
        double actualY = ptNetModel.getObject(1).getY();
        assertEquals(expectedY, actualY);
    }

    @Test
    public void doesEditObjectChangeFieldsForTransition() {
        ptNetModel.editElement(ptNetModel.getObject(3), ptNetModel.getObject(4));

        String expectedID = ptNetModel.getObject(3).getId();
        String actualID = ptNetModel.getObject(4).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getObject(3).getName();
        String actualName = ptNetModel.getObject(4).getName();
        assertEquals(expectedName, actualName);

        double expectedX = ptNetModel.getObject(3).getX();
        double actualX = ptNetModel.getObject(4).getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getObject(3).getY();
        double actualY = ptNetModel.getObject(4).getY();
        assertEquals(expectedY, actualY);

    }

    @Test
    public void EditObjectBehaveiorForObjectsOfDifferentClass() {
        ptNetModel.edit(ptNetModel.getObject(0), ptNetModel.getObject(5));

        String expectedID = ptNetModel.getObject(0).getId();
        String actualID = ptNetModel.getObject(5).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getObject(0).getName();
        String actualName = ptNetModel.getObject(5).getName();
        assertNotEquals(expectedName, actualName);

        double expectedX = ptNetModel.getObject(0).getX();
        double actualX = ptNetModel.getObject(5).getX();
        assertNotEquals(expectedX, actualX);

        double expectedY = ptNetModel.getObject(0).getY();
        double actualY = ptNetModel.getObject(5).getY();
        assertNotEquals(expectedY, actualY);
    }

    @Test
    public void doesEditObjectForTheSameObjects() {
        ptNetModel.editElement(ptNetModel.getObject(0), ptNetModel.getObject(0));
        assertEquals(ptNetModel.getObject(0), ptNetModel.getObject(0));
    }

    @Test
    public void doesEditObjectChangeFieldsForArcs() {
        ptNetModel.editElement(ptNetModel.getElement(0), ptNetModel.getElement(1));

        String expectedID = ptNetModel.getElement(0).getId();
        String actualID = ptNetModel.getElement(1).getId();
        assertNotEquals(expectedID, actualID);

        NetObject expectedStart = ptNetModel.getElement(0).getStart();
        NetObject actualStart = ptNetModel.getElement(1).getStart();
        assertNotEquals(expectedStart, actualStart);

        NetObject expectedEnd = ptNetModel.getElement(0).getEnd();
        NetObject actualEnd = ptNetModel.getElement(1).getEnd();
        assertNotEquals(expectedEnd, actualEnd);

        double expectedWeight = ptNetModel.getElement(0).getWeight();
        double actualWeight = ptNetModel.getElement(1).getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void changingArcWeightForNegativeValue() throws Exception {

        ptNetModel.edit(ptNetModel.getElement(), ptNetModel.createArc(ptNetModel.getObject(1), ptNetModel.getObject(3), -5));

        int expected = 5;

        assertEquals(expected, ptNetModel.getElement(3).getWeight());
    }

    @Test
    public void doesEditObjectTurnsStartAndEndInArc() throws Exception {
        ptNetModel.edit(ptNetModel.getElement(0), ptNetModel.createArc(ptNetModel.getObject(1), ptNetModel.getObject(3), 3));

        Object expectedStart = ptNetModel.getObject(0);
        Object actualStart = ptNetModel.getObject(1);
        assertNotEquals(expectedStart, actualStart);

        Object expectedEnd = ptNetModel.getObject(5);
        Object actualEnd = ptNetModel.getObject(3);
        assertNotEquals(expectedEnd, actualEnd);

    }

    @Test
    public void validationForNegativeValues() {
        Place<Integer> newplace = new Place<>("", "name", 3, 5, -30, -4);
        ptNetModel.edit(ptNetModel.getObject(0), newplace);


        double expectedX = 300;
        double actualX = ptNetModel.getObject(0).getX();

        assertEquals(expectedX, actualX);

        double expectedY = 600;
        double actualY = ptNetModel.getObject(0).getY();

        assertEquals(expectedY, actualY);

        int expected = 131;
        int actual = ((Place) ptNetModel.getObject(0)).getTokenCapacity();

        assertEquals(expected, actual);
        if (ptNetModel.getObject(0).getClass().equals(new Place<Integer>().getClass())) {
            expected = 25;
            actual = ((Place<Integer>) ptNetModel.getObject(0)).getTokens();
        } else {
            assertThrows(Exception.class, () -> ((Place<Integer>) ptNetModel.getObject(0)).getTokens());
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
        transition.setUnready();
        assertNotEquals(expected, actual);
        ptNetModel.nextStep();
        actual = ((Transition) ptNetModel.getObject(3)).getState();
        assertEquals(expected, actual);

    }
}