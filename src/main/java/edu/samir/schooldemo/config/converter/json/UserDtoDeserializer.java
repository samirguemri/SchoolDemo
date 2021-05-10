package edu.samir.schooldemo.config.converter.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.samir.schooldemo.persistence.entity.UserEntity;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class UserDtoDeserializer extends StdDeserializer<UserEntity> {

    public UserDtoDeserializer() {
        this(null);
    }

    private UserDtoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public UserEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String firstName = node.get("firstName").asText();
        String lastName = node.get("lastName").asText();
        String email = node.get("email").asText();
        LocalDate birthday = LocalDate.parse(node.get("birthday").asText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String username = node.get("username").asText();
        String password = node.get("password").asText();
        Integer age = LocalDate.now().getYear() - birthday.getYear();

        return new UserEntity(firstName,lastName,email,birthday, age,username,password);
    }

}
