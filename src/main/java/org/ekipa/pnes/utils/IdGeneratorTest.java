package org.ekipa.pnes.utils;

import org.ekipa.pnes.models.Arc;
import org.ekipa.pnes.models.Circle;
import org.ekipa.pnes.models.NetElement;
import org.ekipa.pnes.models.Rectangle;
import org.ekipa.pnes.models.token.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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


        Rectangle1 = new Rectangle("", "P", 2, 3, 20, Rectangle1, Rectangle2);
        Rectangle2 = new Rectangle("", "P", 4, 5, 20, Rectangle2, Rectangle3);
        Rectangle3 = new Rectangle("", "P", 4, 5, 20, Rectangle3, Rectangle1);

        Circle1 = new Circle<IntegerTokenValue>("", "K", 2, 3, 5);
        Circle2 = new Circle<IntegerTokenValue>("", "K", 2, 3, 5);
        Circle3 = new Circle<IntegerTokenValue>("", "K", 2, 3, 5);

        IdGenerator.setId(Rectangle1);
        IdGenerator.setId(Rectangle2);
        IdGenerator.setId(Rectangle3);

        token = new Token<IntegerTokenValue>(Circle1, new IntegerTokenValue(5L));

        IdGenerator.setId(Circle1);
        IdGenerator.setId(Circle2);
        IdGenerator.setId(Circle3);

    }

    @Test
    public void doesElementHaveCorrectIdForCircle() {
        assertEquals("K-1", Circle2.getId());
    }

    @Test
    public void doesElementHaveCorrectIDForRectangle() {
        assertEquals("P-1", Rectangle2.getId());
    }

    @Test
    public void doesElementExistInList() {
        assertEquals(Rectangle1, IdGenerator.setId(Rectangle1));
    }
}