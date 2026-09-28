package com.gkcontas.patterns.iterator;

@FunctionalInterface
public interface PagedSource<T> {

    Page<T> fetch(int pageNumber);
}
