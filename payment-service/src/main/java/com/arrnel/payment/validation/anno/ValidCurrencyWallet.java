package com.arrnel.payment.validation.anno;

import com.arrnel.payment.validation.impl.CurrencyWalletValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyWalletValidator.class)
public @interface ValidCurrencyWallet {

    String message() default "Invalid create currency wallet request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
