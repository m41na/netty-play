package works.hop.db.event;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatableEntity implements Validatable {

    @Override
    public Map<String, String> validate(Validator validator) {
        Set<ConstraintViolation<Validatable>> constraintViolations = validator.validate(this);
        return constraintViolations.stream().collect(Collectors.toMap(x -> x.getPropertyPath().toString(), x -> x.getMessage()));
    }
}
