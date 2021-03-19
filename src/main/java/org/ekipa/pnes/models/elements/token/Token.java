package org.ekipa.pnes.models.elements.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.elements.NetElement;

@AllArgsConstructor
@Getter
@Setter
public class Token<V extends TokenValue> {
    private NetElement parent;
    private V value;
}
