package com.gkcontas.patterns.bridge;

/** The implementation side of the bridge: <em>how</em> a message is delivered. */
public interface MessageChannel {

    String deliver(String recipient, String body);
}
