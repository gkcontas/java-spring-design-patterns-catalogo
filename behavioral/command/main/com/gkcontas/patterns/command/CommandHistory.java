package com.gkcontas.patterns.command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/** The invoker. It runs commands and keeps the history that makes undo possible. */
public class CommandHistory {

    private final Deque<Command> executed = new ArrayDeque<>();

    public void run(Command command) {
        command.execute();
        executed.push(command);
    }

    public boolean undoLast() {
        if (executed.isEmpty()) {
            return false;
        }
        executed.pop().undo();
        return true;
    }

    /** The audit trail comes free once operations are objects. */
    public List<String> log() {
        return executed.stream().map(Command::description).toList();
    }

    public int size() {
        return executed.size();
    }
}
