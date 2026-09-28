package com.gkcontas.patterns.proxy;

/**
 * The trap that makes {@code @Transactional} and {@code @Cacheable} silently do nothing.
 *
 * <p>A proxy wraps the object. Calls arriving from outside go through it; a call from one
 * method of the object to another goes straight to {@code this}, and the wrapper is never
 * involved. Spring's annotations are implemented as proxies, so annotating a method and
 * then calling it from a neighbouring method in the same class produces no transaction,
 * no caching and no error — the annotation is simply ignored.
 */
public class SelfInvocationExample implements ReportRepository {

    private final ReportRepository delegate;

    public SelfInvocationExample(ReportRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public String generate(String reportName) {
        return delegate.generate(reportName);
    }

    /** Calls generate() on {@code this}, bypassing any proxy wrapped around this object. */
    public String generateTwiceInternally(String reportName) {
        generate(reportName);
        return generate(reportName);
    }
}
