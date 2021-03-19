package org.ekipa.pnes.models.elements.token;

public class RangeTokenValue extends TokenValue<Range> {
    public RangeTokenValue(Range value) throws ValidationException {
        super(value);
    }

    @Override
    protected boolean validateValue(Range value) {
        return value.getEnd() > value.getStart();
    }
}
