package org.ekipa.pnes.models.elements.token;

public class IntegerTokenValue extends TokenValue<Long> {
    public IntegerTokenValue(Long value) throws ValidationException {
        super(value);
    }

    @Override
    protected boolean validateValue(Long value) {
        return value >= 0 && value <= 100;
    }
}
