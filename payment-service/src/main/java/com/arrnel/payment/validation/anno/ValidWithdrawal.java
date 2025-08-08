package com.arrnel.payment.validation.anno;

import com.arrnel.payment.validation.impl.WithdrawalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WithdrawalValidator.class)
public @interface ValidWithdrawal {

    String message() default "Invalid create withdrawal request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
