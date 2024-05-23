
package com.bernardomg.test.pagination;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
public abstract class AbstractPaginationIT<T> {

    protected abstract Iterable<T> read(final Pageable pageable);

    protected void testPageData(final int page, final T expected) {
        final Iterable<T> data;
        final Pageable    pageable;

        // GIVEN
        pageable = PageRequest.of(page, 1);

        // WHEN
        data = read(pageable);

        // THEN
        Assertions.assertThat(data)
            .as("paged data")
            .containsExactly(expected);
    }

    @Test
    @DisplayName("When a page request is received, the response is stored in a page structure")
    void testReadContainer_Paged() {
        final Iterable<T> data;
        final Pageable    pageable;

        // GIVEN
        pageable = Pageable.ofSize(10);

        // WHEN
        data = read(pageable);

        // THEN
        Assertions.assertThat(data)
            .as("paged data")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("When an unpaged page request is received, the response is stored in a page structure")
    void testReadContainer_Unpaged() {
        final Iterable<T> data;
        final Pageable    pageable;

        // GIVEN
        pageable = Pageable.unpaged();

        // WHEN
        data = read(pageable);

        // THEN
        Assertions.assertThat(data)
            .as("unpaged data")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("When a page request is received, the response size is the same as the page size")
    void testReadPaged_PageMax() {
        final Iterable<T> data;
        final Pageable    pageable;

        // GIVEN
        pageable = Pageable.ofSize(1);

        // WHEN
        data = read(pageable);

        // THEN
        Assertions.assertThat(data)
            .as("paged data")
            .hasSize(1);
    }

}
