package com.arrnel.payment.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
public abstract class OperationValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    protected final MessageSource messageSource;
    protected final Locale locale = LocaleContextHolder.getLocale();

    protected boolean isValid;
    protected List<String> errors = new ArrayList<>();
    protected A anno;
    protected ConstraintValidatorContext context;

    protected OperationValidator(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public void initialize(A constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        anno = constraintAnnotation;
    }

    @Override
    public boolean isValid(T request, ConstraintValidatorContext context) {
        isValid = true;
        this.context = context;
        this.context.disableDefaultConstraintViolation();
        validate(request);
        return isValid;
    }

    public abstract void validate(T request);

    protected void addErrorAndMarkNotValid(String propertyNode, String messageCode, String defaultMessage, Object[] args) {
        context.buildConstraintViolationWithTemplate(
                        messageSource.getMessage(
                                messageCode,
                                args,
                                defaultMessage,
                                locale))
                .addPropertyNode(propertyNode)
                .addConstraintViolation();

        isValid = false;
    }

}
