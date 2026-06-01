
package com.bernardomg.security.password.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bernardomg.validation.domain.model.FieldFailure;
import com.bernardomg.validation.validator.FieldRule;

/**
 * Checks the password is strong.
 */
public final class PasswordResetHasStrongPasswordRule implements FieldRule<String> {

    /**
     * Logger for the class.
     */
    private static final Logger     log = LoggerFactory.getLogger(PasswordResetHasStrongPasswordRule.class);

    /**
     * Password constrating checker.
     * <p>
     * TODO: Initialize in the validation library
     */
    private final PasswordValidator validator;

    public PasswordResetHasStrongPasswordRule() {
        super();

        final List<Rule> rules = new ArrayList<>();

        // TODO: Make this configurable on annotation
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
    }

    @Override
    public final Optional<FieldFailure> check(final String password) {
        final Optional<FieldFailure> failure;
        final FieldFailure           fieldFailure;
        final RuleResult             result;

        result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            log.error("The password doesn't fit security constraints");
            fieldFailure = new FieldFailure("tooWeak", "password", "");
            failure = Optional.of(fieldFailure);
        } else {
            failure = Optional.empty();
        }

        return failure;
    }

}
