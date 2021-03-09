package org.ekipa.pnes.models.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.ekipa.pnes.models.NetElement;

@AllArgsConstructor
@Getter
@Setter
public class Token<V extends TokenValue> {
    private NetElement parent;
    private V value;
}
