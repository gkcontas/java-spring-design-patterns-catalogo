package com.gkcontas.patterns.bridge;

/**
 * The abstraction side of the bridge: <em>what</em> is being communicated.
 *
 * <p>The bridge is the field below. Without it, every combination of message kind and
 * delivery channel needs its own class — OrderShippedEmail, OrderShippedSms,
 * PasswordResetEmail, PasswordResetSms — and the count multiplies: five kinds across four
 * channels is twenty classes, and a new channel adds five more. Holding the channel as a
 * reference turns that multiplication into an addition.
 */
public abstract class Notification {

    protected final MessageChannel channel;

    protected Notification(MessageChannel channel) {
        this.channel = channel;
    }

    public abstract String notify(String recipient);
}
