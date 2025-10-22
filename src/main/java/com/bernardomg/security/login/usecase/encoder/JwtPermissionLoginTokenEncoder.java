
package com.bernardomg.security.login.usecase.encoder;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.permission.domain.model.ResourcePermission;
import com.bernardomg.security.user.domain.repository.UserPermissionRepository;

/**
 * Encodes a JWT token including the permissions for the user.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public class JwtPermissionLoginTokenEncoder implements LoginTokenEncoder {

    /**
     * Logger for the class.
     */
    private static final Logger            log = LoggerFactory.getLogger(JwtPermissionLoginTokenEncoder.class);

    /**
     * Token encoder for creating authentication tokens.
     */
    private final TokenEncoder             tokenEncoder;

    /**
     * User permissions repository.
     */
    private final UserPermissionRepository userPermissionRepository;

    /**
     * Token validity time in seconds.
     */
    private final Duration                 validity;

    public JwtPermissionLoginTokenEncoder(final TokenEncoder tknEncoder,
            final UserPermissionRepository userPermissionRepo, final Duration vldt) {
        super();

        tokenEncoder = Objects.requireNonNull(tknEncoder);
        userPermissionRepository = Objects.requireNonNull(userPermissionRepo);
        validity = Objects.requireNonNull(vldt);
    }

    @Override
    public final String encode(final String username) {
        final Map<String, List<String>> permissions;
        final Instant                   expiration;
        final Instant                   issuedAt;
        final String                    token;
        final JwtTokenData              data;

        permissions = getPermissionsMap(username);

        // Issued right now
        issuedAt = Instant.now();
        // Expires in a number of seconds equal to validity
        // TODO: handle validity in the encoder
        expiration = Instant.now()
            .plus(validity);

        // Build token data for the wrapped encoder
        // TODO: Test that permissions are added
        data = new JwtTokenData("", username, "", issuedAt, issuedAt, expiration, List.of(), permissions);

        token = tokenEncoder.encode(data);

        log.debug("Created token for subject {} with expiration date {}", username, expiration);

        return token;
    }

    private final Map<String, List<String>> getPermissionsMap(final String username) {
        Function<ResourcePermission, String> resourceMapper;
        Function<ResourcePermission, String> actionMapper;

        // Resource name in lower case
        resourceMapper = ResourcePermission::resource;
        resourceMapper = resourceMapper.andThen(r -> r.toLowerCase(LocaleContextHolder.getLocale()));

        // Action name in lower case
        actionMapper = ResourcePermission::action;
        actionMapper = actionMapper.andThen(a -> a.toLowerCase(LocaleContextHolder.getLocale()));

        // Transform into a map, with the resource as key, and the list of actions as value
        // TODO: query by id
        return userPermissionRepository.findAll(username)
            .stream()
            .collect(Collectors.groupingBy(resourceMapper, Collectors.mapping(actionMapper, Collectors.toList())));
    }

}
