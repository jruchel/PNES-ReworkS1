package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PTNetModelTest {
    private PTNetModel ptNetModel;

    @BeforeEach
    public void initialize() {
        ptNetModel = new PTNetModel();
        ptNetModel.createPlace("Wojciech", 300, 600, 131, 25);
        ptNetModel.createPlace("Sebastian0", 742, 641, 101, 46);
        ptNetModel.createPlace("Mirek", 5, 7, 10, 2);
        ptNetModel.createTransition("Kuba", 5, 1, 10);
        ptNetModel.createTransition("Kacper", 3, 2, 20);
        ptNetModel.createTransition("Adrian", 91, 5000, 1022);
        try {
            ptNetModel.createArc(ptNetModel.getElement(0),ptNetModel.getElement(5),5);
            ptNetModel.createArc(ptNetModel.getElement(1),ptNetModel.getElement(4),7);
            ptNetModel.createArc(ptNetModel.getElement(2),ptNetModel.getElement(3),1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkNumberOfFoundObjects() {
        Place place = new Place("", "Kuba", 3, 5, 13);
        
        int expected = 2;
        int actual = ptNetModel.findObjects(place).size();

        assertEquals(expected, actual);

    }

    @Test
    public void checkForNothingToFind() {
        Transition transition = new Transition("", "Bartosz", 0, 0, 0);
        transition.setReady();

        int expected = 0;
        int actual = ptNetModel.findObjects(transition).size();

        assertEquals(expected, actual);
    }
}