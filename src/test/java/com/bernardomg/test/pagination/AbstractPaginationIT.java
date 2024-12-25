
package com.bernardomg.test.pagination;

import java.util.Objects;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import com.bernardomg.data.domain.Pagination;
import com.bernardomg.test.config.annotation.IntegrationTest;

@IntegrationTest
public abstract class AbstractPaginationIT<T> {

    private final int totalElements;

    public AbstractPaginationIT(final int total) {
        super();

        totalElements = Objects.requireNonNull(total);
    }

    protected abstract Iterable<T> read(final Pagination pagination);

    protected void testPageData(final int page, final T expected) {
        final Iterable<T> data;
        final Pagination  pagination;

        // GIVEN
        pagination = new Pagination(page, 1);

        // WHEN
        data = read(pagination);

        // THEN
        Assertions.assertThat(data)
            .as("paged data")
            .containsExactly(expected);
    }

    @Test
    @DisplayName("When a page request is received, the response is stored in a page structure")
    void testReadContainer_Paged() {
        final Iterable<T> data;
        final Pagination  pagination;

        // GIVEN
        pagination = new Pagination(1, 10);

        // WHEN
        data = read(pagination);

        // THEN
        Assertions.assertThat(data)
            .as("paged data")
            .isInstanceOf(Page.class);
    }

    @Test
    @DisplayName("When a page is returned, it returns the correct page data")
    void testReadPaged_PageData() {
        final Page<T>    data;
        final Pagination pagination;
        final int        page;

        // GIVEN
        page = 1;
        pagination = new Pagination(page, 1);

        // WHEN
        data = (Page<T>) read(pagination);

        // THEN

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(data.getNumberOfElements())
                .as("number of elements in page")
                .isEqualTo(1);
            softly.assertThat(data.getTotalElements())
                .as("total number of elements")
                .isEqualTo(totalElements);
            softly.assertThat(data.getNumber())
                .as("page number")
                .isEqualTo(page);
            softly.assertThat(data.getTotalPages())
                .as("total number of pages")
                .isEqualTo(totalElements);
        });
    }

    @Test
    @DisplayName("When a page request is received, the response size is the same as the page size")
    void testReadPaged_PageMax() {
        final Iterable<T> data;
        final Pagination  pagination;

        // GIVEN
        pagination = new Pagination(1, 1);

        // WHEN
        data = read(pagination);

        // THEN
        Assertions.assertThat(data)
            .as("paged data")
            .hasSize(1);
    }

}
