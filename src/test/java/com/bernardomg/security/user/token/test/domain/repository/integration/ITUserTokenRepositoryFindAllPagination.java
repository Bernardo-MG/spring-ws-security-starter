
package com.bernardomg.security.user.token.test.domain.repository.integration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.data.domain.Sorting;
import com.bernardomg.security.user.test.config.annotation.OnlyUser;
import com.bernardomg.security.user.token.domain.model.UserToken;
import com.bernardomg.security.user.token.domain.repository.UserTokenRepository;
import com.bernardomg.security.user.token.test.config.annotation.ValidUserToken;
import com.bernardomg.security.user.token.test.config.factory.UserTokens;
import com.bernardomg.test.pagination.AbstractPaginationIT;

@DisplayName("UserTokenRepository - find all - pagination")
@OnlyUser
@ValidUserToken
class ITUserTokenRepositoryFindAllPagination extends AbstractPaginationIT<UserToken> {

    @Autowired
    private UserTokenRepository userTokenRepository;

    public ITUserTokenRepositoryFindAllPagination() {
        super(1);
    }

    @Override
    protected final Iterable<UserToken> read(final Pagination pagination) {
        final Sorting sorting;

        sorting = Sorting.unsorted();
        return userTokenRepository.findAll(pagination, sorting);
    }

    @Test
    @DisplayName("Returns all the data for the first page")
    void testFindAll_Page1() {
        testPageData(1, UserTokens.valid());
    }

    @Test
    @DisplayName("Returns all the data for the second page")
    void testFindAll_Page2() {
        final Iterable<UserToken> logins;
        final Pagination          pagination;
        final Sorting             sorting;

        // GIVEN
        pagination = new Pagination(2, 1);
        sorting = Sorting.unsorted();

        // WHEN
        logins = userTokenRepository.findAll(pagination, sorting);

        // THEN
        Assertions.assertThat(logins)
            .isEmpty();
    }

}
