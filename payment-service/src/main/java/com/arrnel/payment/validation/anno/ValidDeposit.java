package com.arrnel.payment.validation.anno;

import com.arrnel.payment.validation.impl.DepositValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DepositValidator.class)
public @interface ValidDeposit {

    String message() default "Invalid create deposit request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
