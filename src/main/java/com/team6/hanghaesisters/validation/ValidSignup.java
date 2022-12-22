package com.team6.hanghaesisters.validation;

import javax.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SignupValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSignup {

    String message() default "";

    Class[] groups() default {};

    Class[] payload() default {};

}
