package com.gkcontas.patterns.bridge;

public class PasswordResetNotification extends Notification {

    private final String link;

    public PasswordResetNotification(MessageChannel channel, String link) {
        super(channel);
        this.link = link;
    }

    @Override
    public String notify(String recipient) {
        return channel.deliver(recipient, "Reset your password at " + link);
    }
}
