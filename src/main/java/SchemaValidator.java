import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.InputStream;
import java.util.Set;

public class SchemaValidator {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        InputStream openApiStream = SchemaValidator.class.getClassLoader()
                .getResourceAsStream("Schemas/PetSchema.json");
        if (openApiStream == null) {
            throw new IllegalArgumentException("OpenAPI file not found in resources/schemas/PetSchema.json");
        }
        JsonNode openApiNode = mapper.readTree(openApiStream);

        InputStream rulesStream = SchemaValidator.class.getClassLoader()
                .getResourceAsStream("Schemas/Validate-PetSchema.json");
        if (rulesStream == null) {
            throw new IllegalArgumentException("Validation rules file not found in resources/schemas/Validate-PetSchema.json");
        }
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        JsonSchema validationSchema = schemaFactory.getSchema(rulesStream);

        Set<ValidationMessage> errors = validationSchema.validate(openApiNode);

        if (errors.isEmpty()) {
            System.out.println("PetSchema is valid against validation rules!");
        } else {
            System.out.println("❌ Validation errors:");
            errors.forEach(err -> System.out.println(" - " + err.getMessage()));
        }
    }
}
