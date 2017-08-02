package controllers.json;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.libs.Json;

/**
 * Object mapper configuration.
 */
class ObjectMapperEx {
    ObjectMapperEx() {
        final ObjectMapper mapper = Json.newDefaultMapper()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        Json.setObjectMapper(mapper);
    }
}
