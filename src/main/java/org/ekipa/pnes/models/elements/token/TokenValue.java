package org.ekipa.pnes.models.elements.token;

public abstract class TokenValue<V> {
    protected V value;

    public TokenValue(V value) throws ValidationException {
        setValue(value);
    }

    public V getValue() {
        return value;
    }

    /**
     * Ustala wartość Tokena oraz przeprowadza walidację danych do niego przypisanych }
     *
     * @param value wartość tokena
     * @throws ValidationException niepoprawna wartość tokena
     */

    public void setValue(V value) throws ValidationException {
        if (!validateValue(value))
            throw new ValidationException(String.format("%s is not a valid token value.", value.toString()));
        this.value = value;
    }

    protected abstract boolean validateValue(V value);


}
