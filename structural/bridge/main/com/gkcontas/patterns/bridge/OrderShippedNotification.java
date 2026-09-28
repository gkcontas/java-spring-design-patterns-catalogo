package com.gkcontas.patterns.bridge;

public class OrderShippedNotification extends Notification {

    private final String trackingCode;

    public OrderShippedNotification(MessageChannel channel, String trackingCode) {
        super(channel);
        this.trackingCode = trackingCode;
    }

    @Override
    public String notify(String recipient) {
        return channel.deliver(recipient, "Your order shipped, tracking " + trackingCode);
    }
}
