package com.gkcontas.patterns.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A caching proxy: same interface, controlled access.
 *
 * <p>The client cannot tell it apart from the real thing, which is the requirement — a
 * proxy that changes the interface is an adapter, and one that changes the answer is a bug.
 */
public class CachingReportProxy implements ReportRepository {

    private final ReportRepository delegate;
    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public CachingReportProxy(ReportRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public String generate(String reportName) {
        return cache.computeIfAbsent(reportName, delegate::generate);
    }
}
