package com.gkcontas.patterns.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import org.junit.jupiter.api.Test;

class ReportProxyTest {

    @Test
    void cachingProxyShouldCallTheRealSubjectOnlyOnce() {
        SlowReportRepository real = new SlowReportRepository();
        ReportRepository proxy = new CachingReportProxy(real);

        assertThat(proxy.generate("sales")).isEqualTo("report:sales");
        assertThat(proxy.generate("sales")).isEqualTo("report:sales");
        assertThat(proxy.generate("sales")).isEqualTo("report:sales");

        // Same answer every time, one actual call. The client cannot tell the difference,
        // which is the requirement.
        assertThat(real.invocations()).isEqualTo(1);
    }

    @Test
    void protectionProxyShouldRefuseBeforeReachingTheRealSubject() {
        SlowReportRepository real = new SlowReportRepository();
        ReportRepository proxy = new AccessControlledReportProxy(real, Set.of("sales"));

        assertThat(proxy.generate("sales")).isEqualTo("report:sales");
        assertThatThrownBy(() -> proxy.generate("payroll")).isInstanceOf(SecurityException.class);

        // The refused call never reached the delegate.
        assertThat(real.invocations()).isEqualTo(1);
    }

    @Test
    void proxiesShouldStackLikeAnyOtherWrapper() {
        SlowReportRepository real = new SlowReportRepository();
        ReportRepository proxy = new AccessControlledReportProxy(
                new CachingReportProxy(real), Set.of("sales"));

        proxy.generate("sales");
        proxy.generate("sales");

        assertThat(real.invocations()).isEqualTo(1);
    }

    @Test
    void selfInvocationShouldBypassTheProxyEntirely() {
        SlowReportRepository real = new SlowReportRepository();
        // The caching proxy wraps the object; the object then calls itself.
        SelfInvocationExample target = new SelfInvocationExample(real);
        ReportRepository proxied = new CachingReportProxy(target);

        proxied.generate("sales");
        target.generateTwiceInternally("sales");

        // Three logical calls, three real ones: the two internal calls went to `this` and
        // never touched the proxy. This is exactly why a @Transactional or @Cacheable
        // method called from a neighbouring method in the same class does nothing at all —
        // no transaction, no caching, and no error to tell you.
        assertThat(real.invocations()).isEqualTo(3);
    }
}
