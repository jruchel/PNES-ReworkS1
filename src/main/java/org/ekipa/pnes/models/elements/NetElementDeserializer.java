package org.ekipa.pnes.models.elements;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class NetElementDeserializer extends JsonDeserializer<NetElement> {

    @Override
    public NetElement deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode node = p.getCodec().readTree(p);

        JsonNode tokens = node.get("tokens");
        JsonNode state = node.get("state");
        JsonNode startNode = node.get("start");

        if (tokens != null) {
            System.out.println("Is place");
//            Place<>
        } else if (state != null) {
            System.out.println("Is transistion");

        } else if (startNode != null) {
            System.out.println("Is arc");
        }

//        if (nodes)


        return null;
    }
}
