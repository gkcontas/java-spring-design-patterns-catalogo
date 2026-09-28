package com.gkcontas.patterns.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CommandHistoryTest {

    private final TextDocument document = new TextDocument();
    private final CommandHistory history = new CommandHistory();

    @Test
    void shouldUndoInReverseOrder() {
        history.run(new AppendTextCommand(document, "Hello"));
        history.run(new AppendTextCommand(document, ", world"));
        history.run(new AppendTextCommand(document, "!"));
        assertThat(document.content()).isEqualTo("Hello, world!");

        history.undoLast();
        assertThat(document.content()).isEqualTo("Hello, world");
        history.undoLast();
        assertThat(document.content()).isEqualTo("Hello");
    }

    @Test
    void shouldReportWhenThereIsNothingLeftToUndo() {
        assertThat(history.undoLast()).isFalse();

        history.run(new AppendTextCommand(document, "x"));
        assertThat(history.undoLast()).isTrue();
        assertThat(history.undoLast()).isFalse();
    }

    @Test
    void shouldProduceAnAuditTrailForFree() {
        history.run(new AppendTextCommand(document, "a"));
        history.run(new AppendTextCommand(document, "b"));

        // Once an operation is an object it can be logged, queued, retried or shipped
        // across a wire. A method call can be none of those things.
        assertThat(history.log()).containsExactly("append 'b'", "append 'a'");
    }

    @Test
    void shouldLetTheInvokerStayIgnorantOfWhatItRuns() {
        StringBuilder sideEffect = new StringBuilder();
        history.run(new Command() {
            @Override
            public void execute() {
                sideEffect.append("done");
            }

            @Override
            public void undo() {
                sideEffect.setLength(0);
            }

            @Override
            public String description() {
                return "anonymous operation";
            }
        });

        assertThat(sideEffect).hasToString("done");
        assertThat(history.size()).isEqualTo(1);
        history.undoLast();
        assertThat(sideEffect).isEmpty();
    }
}
