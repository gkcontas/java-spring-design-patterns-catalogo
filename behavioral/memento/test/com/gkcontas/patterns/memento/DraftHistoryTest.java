package com.gkcontas.patterns.memento;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DraftHistoryTest {

    private final DraftArticle article = new DraftArticle("Draft");
    private final DraftHistory history = new DraftHistory(3);

    @Test
    void shouldRestoreAPreviousState() {
        history.save(article);
        article.retitle("Second title");
        article.addParagraph("body");

        history.undo(article);

        assertThat(article.title()).isEqualTo("Draft");
        assertThat(article.paragraphs()).isEmpty();
    }

    @Test
    void shouldNotShareMutableStateWithTheSnapshot() {
        article.addParagraph("first");
        history.save(article);

        article.addParagraph("second");

        // The snapshot copied the list. Sharing it would make the snapshot change along
        // with the object it was supposed to remember, which defeats the whole thing.
        history.undo(article);
        assertThat(article.paragraphs()).containsExactly("first");
    }

    @Test
    void shouldForgetTheOldestStateOnceTheHistoryIsFull() {
        for (int version = 1; version <= 5; version++) {
            history.save(article);
            article.retitle("version " + version);
        }

        // Depth capped at three. An unbounded undo history keeps every version of the
        // object alive — a memory leak with a friendly name.
        assertThat(history.depth()).isEqualTo(3);
    }

    @Test
    void shouldReportWhenThereIsNothingToUndo() {
        assertThat(history.undo(article)).isFalse();
    }
}
