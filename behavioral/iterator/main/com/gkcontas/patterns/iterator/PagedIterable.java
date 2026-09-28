package com.gkcontas.patterns.iterator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The case where writing an iterator still earns its keep: traversal that is not over a
 * collection in memory.
 *
 * <p>Java gave away the everyday version of this pattern in 1.2 — any class implementing
 * {@code Iterable} gets the for-each loop and no one writes {@code Iterator} by hand to
 * walk a list. What the JDK did not give away is <em>lazy</em> traversal over something
 * that is fetched as it goes. Here each page is only requested when the previous one runs
 * out, so a caller can stop early and never pay for the rest.
 */
public class PagedIterable<T> implements Iterable<T> {

    private final PagedSource<T> source;

    public PagedIterable(PagedSource<T> source) {
        this.source = source;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {

            private int nextPageNumber = 0;
            private List<T> current = List.of();
            private int indexInPage = 0;
            private boolean sourceExhausted = false;

            @Override
            public boolean hasNext() {
                while (indexInPage >= current.size() && !sourceExhausted) {
                    Page<T> page = source.fetch(nextPageNumber++);
                    current = page.items();
                    indexInPage = 0;
                    sourceExhausted = !page.hasNext();
                }
                return indexInPage < current.size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return current.get(indexInPage++);
            }
        };
    }
}
