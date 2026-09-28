package com.gkcontas.patterns.memento;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * The caretaker. It stores snapshots and hands them back, and understands none of them.
 *
 * <p>The bounded depth is not a detail: an unbounded undo history keeps every version of
 * the object alive, and for anything sizeable that is a memory leak with a friendly name.
 */
public class DraftHistory {

    private final Deque<DraftArticle.Snapshot> snapshots = new ArrayDeque<>();
    private final int maxDepth;

    public DraftHistory(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void save(DraftArticle article) {
        if (snapshots.size() == maxDepth) {
            snapshots.removeLast();
        }
        snapshots.push(article.snapshot());
    }

    public boolean undo(DraftArticle article) {
        if (snapshots.isEmpty()) {
            return false;
        }
        article.restore(snapshots.pop());
        return true;
    }

    public int depth() {
        return snapshots.size();
    }
}
