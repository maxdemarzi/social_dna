package do_.dna;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class Validators {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static HashMap getValidCypherStatements(String body) throws IOException {
        HashMap input;

        // Parse the input
        try {
            input = objectMapper.readValue(body, HashMap.class);
        } catch (Exceptions e) {
            throw Exceptions.invalidInput;
        }

        if (!input.containsKey("source_user_id")) {
            throw Exceptions.missingSourceUserIdParameter;
        } else if (((String)input.get("source_user_id")).isEmpty()) {
            throw Exceptions.invalidSourceUserIdParameter;
        }

        if (!input.containsKey("target_country_code")) {
            throw Exceptions.missingTargetCountryCodeParameter;
        } else if (((String)input.get("target_country_code")).isEmpty()) {
            throw Exceptions.invalidTargetCountryCodeParameter;
        }

        if (!input.containsKey("source_gender")) {
            throw Exceptions.missingSourceGenderParameter;
        } else if (((String)input.get("source_user_id")).isEmpty()) {
            throw Exceptions.invalidSourceGenderParameter;
        }

        if (!input.containsKey("target_gender")) {
            throw Exceptions.missingTargetGenderParameter;
        } else if (((String)input.get("source_user_id")).isEmpty()) {
            throw Exceptions.invalidTargetGenderParameter;
        }

        //Sensible defaults
        if (!input.containsKey("target_age_gte")) {
            input.put("target_age_gte", 0);
        }
        if (!input.containsKey("target_age_lte")) {
            input.put("target_age_lte", 120);
        }
        if (!input.containsKey("recommendation_age_gte")) {
            input.put("recommendation_age_gte", 0);
        }
        if (!input.containsKey("recommendation_age_lte")) {
            input.put("recommendation_age_lte", 120);
        }
        if (!input.containsKey("skip_similar_person")) {
            input.put("skip_similar_person", 0);
        }
        if (!input.containsKey("limit_similar_person")) {
            input.put("limit_similar_person", 25);
        }
        if (!input.containsKey("skip_person")) {
            input.put("skip_person", 0);
        }
        if (!input.containsKey("limit_person")) {
            input.put("limit_person", 25);
        }
        return input;
    }
}
