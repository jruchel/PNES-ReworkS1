package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.Circle;
import org.ekipa.pnes.models.NetElement;
import org.ekipa.pnes.models.Rectangle;
import org.ekipa.pnes.models.token.IntegerTokenValue;
import org.ekipa.pnes.models.token.Token;
import org.ekipa.pnes.models.token.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdGeneratorTest {

    private NetElement Rectangle1;
    private NetElement Rectangle2;
    private NetElement Rectangle3;
    private NetElement Circle1;
    private NetElement Circle2;
    private NetElement Circle3;
    private Token token;


    @BeforeEach
    private void initialize() throws ValidationException {


        Rectangle1 = new Rectangle("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle2 = new Rectangle("P-1", "P", 4, 5, 20, Rectangle2, Rectangle3);
        Rectangle3 = new Rectangle("P-2", "P", 4, 5, 20, Rectangle3, Rectangle1);

        Circle1 = new Circle<IntegerTokenValue>("K-0", "K", 2, 3, 5);
        Circle2 = new Circle<IntegerTokenValue>("K-1", "K", 2, 3, 5);
        Circle3 = new Circle<IntegerTokenValue>("K-2", "K", 2, 3, 5);


        token = new Token<IntegerTokenValue>(Circle1, new IntegerTokenValue(5L));

    }

    @Test
    public void doesIdWillBeChangedToCorrect() {
        Rectangle1.setId("P-5");

        String expected = "P-0";
        String actual = IdGenerator.setId(Rectangle1).getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesShapeHaveTheSameIdBeforeUsingSetIdAndAfter() {

        String expected = Rectangle1.getId();
        String actual = IdGenerator.setId(Rectangle1).getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesIdWillBeChangedForTheObjectWithTheSameFields() {
        Rectangle1 = new Rectangle("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle2 = new Rectangle("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle3 = new Rectangle("P-0", "P", 2, 3, 20, Rectangle1, Rectangle2);


        IdGenerator.setId(Rectangle1);
        IdGenerator.setId(Rectangle2);
        IdGenerator.setId(Rectangle3);

        String expected = "P-2";
        String actual = Rectangle3.getId();

        assertEquals(expected, actual);
    }

    @Test
    public void doesIdGenerateWellForCircle() {

        int correctGenerateId = 0;

        IdGenerator.setId(Circle1);
        IdGenerator.setId(Circle2);
        IdGenerator.setId(Circle3);

        ArrayList<String> listOfGeneratedId = new ArrayList<>();

        listOfGeneratedId.add(Circle1.getId());
        listOfGeneratedId.add(Circle2.getId());
        listOfGeneratedId.add(Circle3.getId());

        for (int i = 0; i < listOfGeneratedId.size(); i++) {
            if (listOfGeneratedId.get(i).equals("K-" + i)) {
                correctGenerateId++;
            }
        }

        int expected = 3;
        int actual = correctGenerateId;

        assertEquals(expected, actual);

    }


    @Test
    public void doesIdGenerateWellForRectangle() {

        int correctGenerateId = 0;

        IdGenerator.setId(Rectangle1);
        IdGenerator.setId(Rectangle2);
        IdGenerator.setId(Rectangle3);

        ArrayList<String> listOfGeneratedId = new ArrayList<>();

        listOfGeneratedId.add(Rectangle1.getId());
        listOfGeneratedId.add(Rectangle2.getId());
        listOfGeneratedId.add(Rectangle3.getId());

        for (int i = 0; i < listOfGeneratedId.size(); i++) {
            if (listOfGeneratedId.get(i).equals("P-" + i)) {
                correctGenerateId++;
            }
        }

        int expected = 3;
        int actual = correctGenerateId;

        assertEquals(expected, actual);

    }

    @Test
    public void doesIdGenerateWellForCircleAndRectangleTogether() {

        int correctGenerateIdRectangle = 0;
        int correctGenerateIdCircle = 0;
        int correctIdAtTheMomentRectangle = 0;
        int correctIdAtTheMomentCircle = 0;

        IdGenerator.setId(Rectangle1);
        IdGenerator.setId(Circle1);
        IdGenerator.setId(Rectangle2);
        IdGenerator.setId(Circle2);
        IdGenerator.setId(Rectangle3);
        IdGenerator.setId(Circle3);

        ArrayList<String> listOfGeneratedId = new ArrayList<>();

        listOfGeneratedId.add(Rectangle1.getId());
        listOfGeneratedId.add(Circle1.getId());
        listOfGeneratedId.add(Rectangle2.getId());
        listOfGeneratedId.add(Circle2.getId());
        listOfGeneratedId.add(Rectangle3.getId());
        listOfGeneratedId.add(Circle3.getId());

        for (int i = 0; i < listOfGeneratedId.size(); i++) {
            if (listOfGeneratedId.get(i).charAt(0) == 'P') {
                if (listOfGeneratedId.get(i).equals("P-" + correctIdAtTheMomentRectangle)) {
                    correctIdAtTheMomentRectangle++;
                }
                correctGenerateIdRectangle++;
            } else if (listOfGeneratedId.get(i).charAt(0) == 'K') {
                if (listOfGeneratedId.get(i).equals("K-" + correctIdAtTheMomentCircle)) {
                    correctIdAtTheMomentCircle++;
                }
                correctGenerateIdCircle++;
            }
        }

        int actual = 0;

        if (correctGenerateIdRectangle == correctGenerateIdCircle) {
            actual = correctGenerateIdCircle;
        }
        int expected = 3;
        assertTrue(expected == actual);
    }

}

