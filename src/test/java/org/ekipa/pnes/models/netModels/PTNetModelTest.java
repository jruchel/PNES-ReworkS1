package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PTNetModelTest {
    private PTNetModel<Integer> ptNetModel;

    @BeforeEach
    public void initialize() {
        ptNetModel = new PTNetModel<>();
        ptNetModel.createPlace("Wojciech", 300, 600, 131, 25);
        ptNetModel.createPlace("Sebastian0", 742, 641, 101, 46);
        ptNetModel.createPlace("Mirek", 5, 7, 10, 2);
        ptNetModel.createTransition("Kuba", 5, 1);
        ptNetModel.createTransition("Kacper", 3, 2);
        ptNetModel.createTransition("Adrian", 91, 5000);

        try {
            ptNetModel.createArc(ptNetModel.getElement(0), ptNetModel.getElement(5), 5);
            ptNetModel.createArc(ptNetModel.getElement(1), ptNetModel.getElement(4), 7);
            ptNetModel.createArc(ptNetModel.getElement(2), ptNetModel.getElement(3), 1);
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
    public void checkEditObjectChangeGoodFieldsForPlace() {
        ptNetModel.edit(ptNetModel.getElement(0), ptNetModel.getElement(1));

        String expectedID = ptNetModel.getElement(0).getId();
        String actualID = ptNetModel.getElement(1).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getElement(0).getName();
        String actualName = ptNetModel.getElement(1).getName();
        assertEquals(expectedName,actualName);

        double expectedX = ptNetModel.getElement(0).getX();
        double actualX = ptNetModel.getElement(1).getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getElement(0).getY();
        double actualY = ptNetModel.getElement(1).getY();
        assertEquals(expectedY,actualY);
    }

    @Test
    public void checkEditObjectChangeGoodFieldsForTransition() {
        ptNetModel.editObject(ptNetModel.getElement(3), ptNetModel.getElement(4));

        String expectedID = ptNetModel.getElement(3).getId();
        String actualID = ptNetModel.getElement(4).getId();
        assertNotEquals(expectedID,actualID);

        String expectedName = ptNetModel.getElement(3).getName();
        String actualName = ptNetModel.getElement(4).getName();
        assertEquals(expectedName,actualName);

        double expectedX = ptNetModel.getElement(3).getX();
        double actualX = ptNetModel.getElement(4).getX();
        assertEquals(expectedX,actualX);

        double expectedY = ptNetModel.getElement(3).getY();
        double actualY = ptNetModel.getElement(4).getY();
        assertEquals(expectedY,actualY);

    }

    @Test
    public void checkHowEditObjectWillBehaveForObjectsOfDifferentClass() {
        ptNetModel.edit(ptNetModel.getElement(0), ptNetModel.getElement(5));

        String expectedID = ptNetModel.getElement(0).getId();
        String actualID = ptNetModel.getElement(5).getId();
        assertNotEquals(expectedID,actualID);

        String expectedName = ptNetModel.getElement(0).getName();
        String actualName = ptNetModel.getElement(5).getName();
        assertNotEquals(expectedName,actualName);

        double expectedX = ptNetModel.getElement(0).getX();
        double actualX = ptNetModel.getElement(5).getX();
        assertNotEquals(expectedX,actualX);

        double expectedY = ptNetModel.getElement(0).getY();
        double actualY = ptNetModel.getElement(5).getY();
        assertNotEquals(expectedY,actualY);

    }

    @Test
    public void checkEditObjectForTheSameObjects() {
        ptNetModel.editObject(ptNetModel.getElement(0), ptNetModel.getElement(0));
        assertEquals(ptNetModel.getElement(0), ptNetModel.getElement(0));
    }

    @Test
    public void checkEditObjectChangeGoodFieldsForArcs() {
        ptNetModel.editObject(ptNetModel.getArc(0), ptNetModel.getArc(1));

        String expectedID = ptNetModel.getArc(0).getId();
        String actualID = ptNetModel.getArc(1).getId();
        assertNotEquals(expectedID,actualID);

        NetElement expectedStart = ptNetModel.getArc(0).getStart();
        NetElement actualStart = ptNetModel.getArc(1).getStart();
        assertNotEquals(expectedStart,actualStart);

        NetElement expectedEnd = ptNetModel.getArc(0).getEnd();
        NetElement actualEnd = ptNetModel.getArc(1).getEnd();
        assertNotEquals(expectedEnd,actualEnd);

        double expectedWeight = ptNetModel.getArc(0).getWeight();
        double actualWeight = ptNetModel.getArc(1).getWeight();
        assertEquals(expectedWeight,actualWeight);
    }

    @Test
    public void checkIfItIsPossibleToChangeTheTypeOfNetworkModelTokenOnAnIncorrect() {

        PTNetModel<Integer> expected = ptNetModel;

        PTNetModel<Double> ptNetModel1 = new PTNetModel<>();
        ptNetModel.editObject(ptNetModel, ptNetModel1);

        PTNetModel actual = ptNetModel;

        assertNotEquals(expected, actual);
    }

    @Test
    public void checkIfArcCannotChangeWeight() {
        try {
            ptNetModel.edit(ptNetModel.getArc(0), ptNetModel.createArc(ptNetModel.getElement(4), ptNetModel.getElement(0), -5));
        } catch (Exception e) {
            e.printStackTrace();
        }

        int expected = 5;

        assertEquals(expected, ptNetModel.getArc(3).getWeight());
    }

    @Test
    public void check() {
        ptNetModel.edit(ptNetModel.getElement(0), ptNetModel.createPlace("ABC", 20, 30, -4, -22));

        int expected = 131;
        int actual = ((Place) ptNetModel.getElement(6)).getTokenCapacity();

        assertEquals(expected, actual);

        expected = 25;
        actual = (int) ((Place) ptNetModel.getElement(6)).getToken();

        assertEquals(expected, actual);
    }
}