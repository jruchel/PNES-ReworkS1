package org.ekipa.pnes.models.token;

public class StringTokenValue extends TokenValue<String> {
    public StringTokenValue(String value) throws ValidationException {
        super(value);
    }

    @Override
    protected boolean validateValue(String value) {
        return true;
    }
}
