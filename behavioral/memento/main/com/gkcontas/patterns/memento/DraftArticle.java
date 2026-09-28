package com.gkcontas.patterns.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * The originator. It is the only thing that knows how to read its own state back.
 *
 * <p>The memento is a nested record whose components are not reachable from outside this
 * class — {@link Snapshot} is public so it can be held by a caretaker, but its fields are
 * only consumed by {@link #restore(Snapshot)}. That asymmetry is the pattern: the
 * caretaker can keep a snapshot and hand it back, and still cannot inspect or forge one.
 * Exposing getters on the memento is the usual way this is implemented and it gives away
 * the encapsulation the pattern exists to protect.
 */
public class DraftArticle {

    private String title;
    private final List<String> paragraphs = new ArrayList<>();

    public DraftArticle(String title) {
        this.title = title;
    }

    public void retitle(String title) {
        this.title = title;
    }

    public void addParagraph(String paragraph) {
        paragraphs.add(paragraph);
    }

    public String title() {
        return title;
    }

    public List<String> paragraphs() {
        return List.copyOf(paragraphs);
    }

    /** Copies the mutable list: a snapshot sharing it would change as the draft changes. */
    public Snapshot snapshot() {
        return new Snapshot(title, List.copyOf(paragraphs));
    }

    public void restore(Snapshot snapshot) {
        this.title = snapshot.title;
        this.paragraphs.clear();
        this.paragraphs.addAll(snapshot.paragraphs);
    }

    /** Opaque to everyone except the originator. */
    public record Snapshot(String title, List<String> paragraphs) {

        public Snapshot {
            paragraphs = List.copyOf(paragraphs);
        }
    }
}
