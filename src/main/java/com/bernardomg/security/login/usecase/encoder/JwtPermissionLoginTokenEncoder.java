
package com.bernardomg.security.login.usecase.encoder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bernardomg.security.jwt.encoding.JwtTokenData;
import com.bernardomg.security.jwt.encoding.TokenEncoder;
import com.bernardomg.security.permission.data.domain.model.ResourcePermission;
import com.bernardomg.security.user.permission.domain.repository.UserPermissionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Encodes a JWT token including the permissions for the user.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public class JwtPermissionLoginTokenEncoder implements LoginTokenEncoder {

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

        permissions = getPermissionsMap(username);
        return encode(username, permissions);
    }

    private final String encode(final String subject, final Map<String, List<String>> permissions) {
        final LocalDateTime expiration;
        final LocalDateTime issuedAt;
        final String        token;
        final JwtTokenData  data;

        // Issued right now
        issuedAt = LocalDateTime.now();
        // Expires in a number of seconds equal to validity
        // TODO: handle validity in the encoder
        expiration = LocalDateTime.now()
            .plus(validity);

        // Build token data for the wrapped encoder
        data = JwtTokenData.builder()
            .withSubject(subject)
            .withIssuedAt(issuedAt)
            .withNotBefore(issuedAt)
            .withExpiration(expiration)
            // TODO: Test that permissions are added
            .withPermissions(permissions)
            .build();

        token = tokenEncoder.encode(data);

        log.debug("Created token for subject {} with expiration date {}", subject, expiration);

        return token;
    }

    private final Map<String, List<String>> getPermissionsMap(final String username) {
        Function<ResourcePermission, String> resourceMapper;
        Function<ResourcePermission, String> actionMapper;

        // Resource name in lower case
        resourceMapper = ResourcePermission::resource;
        resourceMapper = resourceMapper.andThen(String::toLowerCase);

        // Action name in lower case
        actionMapper = ResourcePermission::action;
        actionMapper = actionMapper.andThen(String::toLowerCase);

        // Transform into a map, with the resource as key, and the list of actions as value
        // TODO: query by id
        return userPermissionRepository.findAll(username)
            .stream()
            .collect(Collectors.groupingBy(resourceMapper, Collectors.mapping(actionMapper, Collectors.toList())));
    }

}
