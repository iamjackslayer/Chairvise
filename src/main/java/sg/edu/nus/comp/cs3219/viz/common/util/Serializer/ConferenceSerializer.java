package sg.edu.nus.comp.cs3219.viz.common.util.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;

public class ConferenceSerializer extends StdSerializer<Conference> {

    public ConferenceSerializer() {this(null);}

    protected ConferenceSerializer(Class<Conference> t) {
        super(t);
    }

    @Override
    public void serialize(Conference value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("numAuthorRecord", value.getNumAuthorRecord());
        gen.writeNumberField("numReviewRecord", value.getNumReviewRecord());
        gen.writeNumberField("numSubmissionRecord", value.getNumSubmissionRecord());
        gen.writeStringField("name", value.getName());
        if(value.getDate() != null) {
            gen.writeStringField("date", value.getDate().toString());
        }
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("creator_identifier", value.getCreatorIdentifier());
        gen.writeEndObject();
    }
}
