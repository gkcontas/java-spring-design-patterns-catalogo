package com.gkcontas.patterns.command;

public class AppendTextCommand implements Command {

    private final TextDocument document;
    private final String text;

    public AppendTextCommand(TextDocument document, String text) {
        this.document = document;
        this.text = text;
    }

    @Override
    public void execute() {
        document.append(text);
    }

    @Override
    public void undo() {
        document.removeLast(text.length());
    }

    @Override
    public String description() {
        return "append '%s'".formatted(text);
    }
}
