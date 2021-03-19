package org.ekipa.pnes.models.elements.token;

public class FloatingPointTokenValue extends TokenValue<Double> {
    public FloatingPointTokenValue(Double value) throws ValidationException {
        super(value);
    }

    @Override
    protected boolean validateValue(Double value) {
        return value >= 0 && value <= 1;
    }
}
