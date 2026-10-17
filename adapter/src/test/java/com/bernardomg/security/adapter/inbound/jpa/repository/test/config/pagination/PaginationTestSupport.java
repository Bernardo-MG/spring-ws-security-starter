
package com.bernardomg.security.adapter.inbound.jpa.repository.test.config.pagination;

import java.util.Objects;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.assertj.core.api.SoftAssertions;

import com.bernardomg.pagination.domain.Page;
import com.bernardomg.pagination.domain.Pagination;

public abstract class PaginationTestSupport<T> {

    private final int totalElements;

    protected PaginationTestSupport(final int total) {
        totalElements = Objects.requireNonNull(total);
    }

    protected final void assertPageContents(final Page<T> data, final T expected) {
        Assertions.assertThat(data)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("paged data")
            .containsExactly(expected);
    }

    protected final void assertPageMetadata(final Page<T> data, final int page) {
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(data.size())
                .as("number of elements in page")
                .isEqualTo(1);
            softly.assertThat(data.totalElements())
                .as("total number of elements")
                .isEqualTo(totalElements);
            softly.assertThat(data.page())
                .as("page number")
                .isEqualTo(page);
            softly.assertThat(data.totalPages())
                .as("total number of pages")
                .isEqualTo(totalElements);
        });
    }

    protected final void assertPageSize(final Page<T> data) {
        Assertions.assertThat(data)
            .extracting(Page::content)
            .asInstanceOf(InstanceOfAssertFactories.LIST)
            .as("paged data")
            .hasSize(1);
    }

    protected abstract Page<T> read(final Pagination pagination);

}
