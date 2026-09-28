package com.gkcontas.patterns.command;

/**
 * An operation turned into an object.
 *
 * <p>That reification is the whole idea, and everything the pattern is used for follows
 * from it: an object can be put in a queue, logged, retried, sent over a wire, and — as
 * here — asked to reverse itself. A method call can do none of those.
 */
public interface Command {

    void execute();

    /**
     * Undo needs whatever state the redo destroyed, which is why a command usually has to
     * capture it at construction rather than recompute it later.
     */
    void undo();

    String description();
}
