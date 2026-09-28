package com.gkcontas.patterns.command;

/** The receiver: it knows how to change, not when or why. */
public class TextDocument {

    private final StringBuilder content = new StringBuilder();

    public void append(String text) {
        content.append(text);
    }

    public void removeLast(int characters) {
        content.delete(content.length() - characters, content.length());
    }

    public String content() {
        return content.toString();
    }
}
