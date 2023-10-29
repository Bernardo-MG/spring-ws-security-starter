
package com.bernardomg.validation.constraint;

import java.util.ArrayList;
import java.util.List;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class PasswordConstraintValidator implements ConstraintValidator<StrongPassword, String> {

    @Override
    public void initialize(final StrongPassword arg0) {}

    @Override
    public final boolean isValid(final String password, final ConstraintValidatorContext context) {
        final List<Rule>        rules;
        final PasswordValidator validator;
        final RuleResult        result;
        final boolean           valid;

        rules = new ArrayList<>();
        // Rule 1: Password length should be in between
        // 8 and 16 characters
        rules.add(new LengthRule(8, 16));
        // Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());
        // Rule 3.a: At least one Upper-case character
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        // Rule 3.b: At least one Lower-case character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        // Rule 3.c: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        // Rule 3.d: At least one special character
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

        validator = new PasswordValidator(rules);

        result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            valid = true;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.join(",", validator.getMessages(result)))
                .addConstraintViolation();
            valid = false;
        }

        return valid;
    }
}
