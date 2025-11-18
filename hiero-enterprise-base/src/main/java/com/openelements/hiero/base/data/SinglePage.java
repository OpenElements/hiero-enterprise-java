package com.openelements.hiero.base.data;

import java.util.List;
import java.util.Objects;

/**
 * Basic {@link Page} implementation backed by an in-memory list.
 *
 * @param <T> element type for the page
 */
public final class SinglePage<T> implements Page<T> {

    private final List<T> data;

    public SinglePage(final List<T> data) {
        this.data = List.copyOf(Objects.requireNonNull(data, "data must not be null"));
    }

    @Override
    public int getPageIndex() {
        return 0;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Page<T> next() {
        throw new IllegalStateException("No next page");
    }

    @Override
    public Page<T> first() {
        return this;
    }

    @Override
    public boolean isFirst() {
        return true;
    }
}

