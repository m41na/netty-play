package works.hop.db.event;

import javax.validation.Validator;
import java.util.Map;

public interface Validatable {

    Map<String, String> validate(Validator validator);
}
