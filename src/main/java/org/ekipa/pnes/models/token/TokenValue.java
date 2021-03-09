package org.ekipa.pnes.models.token;

public abstract class TokenValue<V> {
    protected V value;

    public TokenValue(V value) throws ValidationException {
        setValue(value);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) throws ValidationException {
        if (!validateValue(value))
            throw new ValidationException(String.format("%s is not a valid token value.", value.toString()));
        this.value = value;
    }

    protected abstract boolean validateValue(V value);


}
