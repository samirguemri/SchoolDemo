package edu.samir.schooldemo.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.samir.schooldemo.entities.Administrator;
import edu.samir.schooldemo.entities.Role;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class AdministratorDeserializer extends StdDeserializer<Administrator> {

    public AdministratorDeserializer() {
        this(null);
    }

    private AdministratorDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Administrator deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String firstName = node.get("firstName").asText();
        String lastName = node.get("lastName").asText();
        String email = node.get("email").asText();
        LocalDate birthday = LocalDate.parse(node.get("birthday").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String username = node.get("username").asText();
        String password = node.get("password").asText();
        Role role = Role.valueOf(node.get("role").asText());

        return new Administrator(firstName,lastName,email,birthday,username,password,role);
    }
}
