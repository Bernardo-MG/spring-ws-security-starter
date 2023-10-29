
package com.bernardomg.security.user.validation;

import java.util.Collection;

import com.bernardomg.security.user.model.request.UserUpdate;
import com.bernardomg.security.user.persistence.repository.UserRepository;
import com.bernardomg.validation.AbstractValidator;
import com.bernardomg.validation.failure.FieldFailure;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class UpdateUserValidator extends AbstractValidator<UserUpdate> {

    private final UserRepository userRepository;

    public UpdateUserValidator(final UserRepository userRepo) {
        super();

        userRepository = userRepo;
    }

    @Override
    protected final void checkRules(final UserUpdate user, final Collection<FieldFailure> failures) {
        FieldFailure failure;

        // Verify the email is not registered
        if (userRepository.existsByIdNotAndEmail(user.getId(), user.getEmail())) {
            log.error("A user already exists with the email {}", user.getEmail());
            // TODO: Is the code exists or is it existing? Make sure all use the same
            failure = FieldFailure.of("email", "existing", user.getEmail());
            failures.add(failure);
        }

    }

}
