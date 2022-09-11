package com.intexsoft.analytics.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation constraint for a Password in string format.
 * <p>
 * e.g. 6cfb0496-fa35-4668-a970-78a873d7970e
 */
@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")
@Retention(RUNTIME)
@Target({ FIELD })
@Constraint(validatedBy = {})
@Documented
public @interface Password {

    String message() default "Weak password, password must contain minimum eight characters, at least one uppercase letter, one lowercase letter and one number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
