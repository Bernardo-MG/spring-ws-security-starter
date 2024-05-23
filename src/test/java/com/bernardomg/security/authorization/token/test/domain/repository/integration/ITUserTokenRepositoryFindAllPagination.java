
package com.bernardomg.security.authorization.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.security.authentication.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.authorization.token.domain.model.UserToken;
import com.bernardomg.security.authorization.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.authorization.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.authorization.token.test.config.factory.UserTokens;
import com.bernardomg.test.config.annotation.IntegrationTest;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@IntegrationTest
@DisplayName("UserTokenRepository - find all - pagination")
@OnlyUser
@ValidUserToken
class ITUserTokenRepositoryFindAllPagination extends AbstractPaginationIT<UserToken> {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Override
    protected final Iterable<UserToken> read(final Pageable pageable) {
        return userTokenRepository.findAll(pageable);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAll_Page1() {
        testPageData(0, UserTokens.valid());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAll_Page2() {
        final Iterable<UserToken> logins;
        final Pageable            pageable;

        // GIVEN
        pageable = PageRequest.of(1, 1);

        // WHEN
        logins = userTokenRepository.findAll(pageable);

        // THEN
        Assertions.assertThat(logins)
            .isEmpty();
    }

}
