package controllers.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * State (soft-deleted indicator) fields serializer.
 */
public class StateSerializer extends StdSerializer<Boolean> {

    protected StateSerializer() {
        super(Boolean.class);
    }

    @Override
    public void serialize(final Boolean value, final JsonGenerator gen, final SerializerProvider provider)
            throws IOException {
        if (value) {
            gen.writeString("deleted");
        } else {
            gen.writeString("active");
        }
    }
}
