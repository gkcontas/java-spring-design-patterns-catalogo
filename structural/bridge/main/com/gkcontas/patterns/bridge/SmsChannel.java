package com.gkcontas.patterns.bridge;

public class SmsChannel implements MessageChannel {

    @Override
    public String deliver(String recipient, String body) {
        return "sms to %s: %s".formatted(recipient, body);
    }
}
