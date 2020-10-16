package sg.edu.nus.comp.cs3219.viz.common.util.Deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import sg.edu.nus.comp.cs3219.viz.common.entity.record.Conference;

public class ConferenceDeserializer extends StdDeserializer<Conference> {

    public ConferenceDeserializer() {this(null);}

    protected ConferenceDeserializer(Class<?> vc) {
        super(vc);
    }

    private String getStrValueByField(JsonNode node, String fieldName){
        if (node.hasNonNull(fieldName)){
            return node.get(fieldName).asText();
        }
        return null;
    }

    private Date getDateValueByField(JsonNode node, String fieldName) {
        if (node.hasNonNull(fieldName)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return sdf.parse(node.get(fieldName).asText());
            } catch (ParseException e) {
                System.out.println("Error in parsing/ deserializing date");
            }
        }
        return null;
    }

    @Override
    public Conference deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        Conference newConference = new Conference();
        String name = getStrValueByField(node, "name");
        Date date = getDateValueByField(node, "date");
        String description = getStrValueByField(node, "description");
//        newConference.setId(new ConferencePK(null, name));
        newConference.setName(name);
        newConference.setDate(date);
        newConference.setDescription(description);

        return newConference;
    }
}
