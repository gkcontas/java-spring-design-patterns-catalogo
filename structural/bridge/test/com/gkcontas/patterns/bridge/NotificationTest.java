package com.gkcontas.patterns.bridge;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void shouldCombineAnyMessageKindWithAnyChannel() {
        assertThat(new OrderShippedNotification(new EmailChannel(), "BR123").notify("ana@example.com"))
                .isEqualTo("email to ana@example.com: Your order shipped, tracking BR123");
        assertThat(new OrderShippedNotification(new SmsChannel(), "BR123").notify("+5511999"))
                .startsWith("sms to +5511999");
        assertThat(new PasswordResetNotification(new EmailChannel(), "https://x/reset").notify("b@e.com"))
                .startsWith("email to b@e.com: Reset your password");
    }

    @Test
    void shouldAddAChannelWithoutAddingOneClassPerMessageKind() {
        // A new channel is one class. Without the bridge it would be one class per
        // (channel x message kind) pair, and this test would need two new types.
        MessageChannel push = (recipient, body) -> "push to %s: %s".formatted(recipient, body);

        List<Notification> notifications = List.of(
                new OrderShippedNotification(push, "BR999"),
                new PasswordResetNotification(push, "https://x/reset"));

        assertThat(notifications).allSatisfy(notification ->
                assertThat(notification.notify("device-1")).startsWith("push to device-1"));
    }
}
