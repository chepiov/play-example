package controllers.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * * Helper functions to work with JSON views.
 */
public class JsonEx {

    /**
     * Converts an object to JsonNode using defined view.
     *
     * @param viewClass View for using.
     * @param data      Value to convert in Json.
     * @return the JSON node.
     */
    public static JsonNode toJson(final Class<?> viewClass, final Object data) {
        final ObjectMapper objectMapper = play.libs.Json.mapper();
        return objectMapper.setConfig(objectMapper.getSerializationConfig().withView(viewClass))
                .valueToTree(data);
    }
}
