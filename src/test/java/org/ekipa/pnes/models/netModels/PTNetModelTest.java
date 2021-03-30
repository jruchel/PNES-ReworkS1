package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.NetElement;
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
    public void doesEditObjectChangeFieldsForPlace() {
        ptNetModel.edit(ptNetModel.getElement(0), ptNetModel.getElement(1));

        String expectedID = ptNetModel.getElement(0).getId();
        String actualID = ptNetModel.getElement(1).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getElement(0).getName();
        String actualName = ptNetModel.getElement(1).getName();
        assertEquals(expectedName, actualName);

        double expectedX = ptNetModel.getElement(0).getX();
        double actualX = ptNetModel.getElement(1).getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getElement(0).getY();
        double actualY = ptNetModel.getElement(1).getY();
        assertEquals(expectedY, actualY);
    }

    @Test
    public void doesEditObjectChangeFieldsForTransition() {
        ptNetModel.editObject(ptNetModel.getElement(3), ptNetModel.getElement(4));

        String expectedID = ptNetModel.getElement(3).getId();
        String actualID = ptNetModel.getElement(4).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getElement(3).getName();
        String actualName = ptNetModel.getElement(4).getName();
        assertEquals(expectedName, actualName);

        double expectedX = ptNetModel.getElement(3).getX();
        double actualX = ptNetModel.getElement(4).getX();
        assertEquals(expectedX, actualX);

        double expectedY = ptNetModel.getElement(3).getY();
        double actualY = ptNetModel.getElement(4).getY();
        assertEquals(expectedY, actualY);

    }

    @Test
    public void EditObjectBehaveiorForObjectsOfDifferentClass() {
        ptNetModel.edit(ptNetModel.getElement(0), ptNetModel.getElement(5));

        String expectedID = ptNetModel.getElement(0).getId();
        String actualID = ptNetModel.getElement(5).getId();
        assertNotEquals(expectedID, actualID);

        String expectedName = ptNetModel.getElement(0).getName();
        String actualName = ptNetModel.getElement(5).getName();
        assertNotEquals(expectedName, actualName);

        double expectedX = ptNetModel.getElement(0).getX();
        double actualX = ptNetModel.getElement(5).getX();
        assertNotEquals(expectedX, actualX);

        double expectedY = ptNetModel.getElement(0).getY();
        double actualY = ptNetModel.getElement(5).getY();
        assertNotEquals(expectedY, actualY);
    }

    @Test
    public void doesEditObjectForTheSameObjects() {
        ptNetModel.editObject(ptNetModel.getElement(0), ptNetModel.getElement(0));
        assertEquals(ptNetModel.getElement(0), ptNetModel.getElement(0));
    }

    @Test
    public void doesEditObjectChangeFieldsForArcs() {
        ptNetModel.editObject(ptNetModel.getArc(0), ptNetModel.getArc(1));

        String expectedID = ptNetModel.getArc(0).getId();
        String actualID = ptNetModel.getArc(1).getId();
        assertNotEquals(expectedID, actualID);

        NetElement expectedStart = ptNetModel.getArc(0).getStart();
        NetElement actualStart = ptNetModel.getArc(1).getStart();
        assertNotEquals(expectedStart, actualStart);

        NetElement expectedEnd = ptNetModel.getArc(0).getEnd();
        NetElement actualEnd = ptNetModel.getArc(1).getEnd();
        assertNotEquals(expectedEnd, actualEnd);

        double expectedWeight = ptNetModel.getArc(0).getWeight();
        double actualWeight = ptNetModel.getArc(1).getWeight();
        assertEquals(expectedWeight, actualWeight);
    }

    @Test
    public void changingArcWeightForNegativeValue() throws Exception {

        ptNetModel.edit(ptNetModel.getArc(0), ptNetModel.createArc(ptNetModel.getElement(1), ptNetModel.getElement(3), -5));

        int expected = 5;

        assertEquals(expected, ptNetModel.getArc(3).getWeight());
    }

    @Test
    public void doesEditObjectTurnsStartAndEndInArc() throws Exception {
        ptNetModel.edit(ptNetModel.getArc(0), ptNetModel.createArc(ptNetModel.getElement(1), ptNetModel.getElement(3), 3));

        Object expectedStart = ptNetModel.getElement(0);
        Object actualStart = ptNetModel.getElement(1);
        assertNotEquals(expectedStart, actualStart);

        Object expectedEnd = ptNetModel.getElement(5);
        Object actualEnd = ptNetModel.getElement(3);
        assertNotEquals(expectedEnd, actualEnd);

    }

    @Test
    public void validationForNegativeValues() {
        Place<Integer> newplace = new Place<>("", "name", 3,5,-30,-4);
        ptNetModel.edit(ptNetModel.getElement(0), newplace);


        double expectedX = 300;
        double actualX = ptNetModel.getElement(0).getX();

        assertEquals(expectedX, actualX);

        double expectedY = 600;
        double actualY = ptNetModel.getElement(0).getY();

        assertEquals(expectedY, actualY);

        int expected = 131;
        int actual = ((Place) ptNetModel.getElement(0)).getTokenCapacity();

        assertEquals(expected, actual);
        if (ptNetModel.getElement(0).getClass().equals(new Place<Integer>().getClass())) {
            expected = 25;
            actual =((Place<Integer>) ptNetModel.getElement(0)).getToken();
        }else {
            assertThrows(Exception.class, () -> ((Place<Integer>) ptNetModel.getElement(0)).getToken());
        }
        assertEquals(expected, actual);
    }
}