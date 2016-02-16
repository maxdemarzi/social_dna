package do_.dna;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class Exceptions extends WebApplicationException {
    public Exceptions(int code, String error)  {
        super(new Throwable(error), Response.status(code)
                .entity("{\"error\":\"" + error + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build());

    }

    public static Exceptions invalidInput = new Exceptions(400, "Invalid Input");

    public static Exceptions missingSourceUserIdParameter = new Exceptions(400, "Missing source_user_id Parameter.");
    public static Exceptions invalidSourceUserIdParameter = new Exceptions(400, "Invalid source_user_id Parameter.");

    public static Exceptions missingTargetCountryCodeParameter = new Exceptions(400, "Missing target_country_code Parameter.");
    public static Exceptions invalidTargetCountryCodeParameter = new Exceptions(400, "Invalid target_country_code Parameter.");

    public static Exceptions missingSourceGenderParameter = new Exceptions(400, "Missing source_gender Parameter.");
    public static Exceptions invalidSourceGenderParameter = new Exceptions(400, "Invalid source_gender Parameter.");

    public static Exceptions missingTargetGenderParameter = new Exceptions(400, "Missing target_gender Parameter.");
    public static Exceptions invalidTargetGenderParameter = new Exceptions(400, "Invalid target_gender Parameter.");


}