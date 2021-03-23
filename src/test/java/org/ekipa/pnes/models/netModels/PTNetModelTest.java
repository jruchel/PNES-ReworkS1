package org.ekipa.pnes.models.netModels;

import org.ekipa.pnes.models.elements.NetElement;
import org.ekipa.pnes.models.elements.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PTNetModelTest {
    private PTNetModel ptNetModel;

    @BeforeEach
    public void initialize(){
        ptNetModel = new PTNetModel();
        ptNetModel.createPlace("Marcin",3,6,13,2);
        ptNetModel.createPlace("Kuba",7,6,10,4);
        ptNetModel.createPlace("Mirek",5,7,10,2);
        ptNetModel.createTransition("Kuba",5,1,10);
        ptNetModel.createTransition("Kacper",3,2,20);
        ptNetModel.createTransition("Rafał",9,5,10);
    }

    @Test
    void findObjects() {
    Place place = new Place("","Kuba",3,5,13);
        System.out.println(ptNetModel.findObjects(place));

    }
}