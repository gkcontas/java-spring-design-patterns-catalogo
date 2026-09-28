package com.gkcontas.patterns.bridge;

public class EmailChannel implements MessageChannel {

    @Override
    public String deliver(String recipient, String body) {
        return "email to %s: %s".formatted(recipient, body);
    }
}
