package com.gkcontas.patterns.proxy;

import java.util.concurrent.atomic.AtomicInteger;

/** The real subject: correct, and expensive enough that callers want it called less. */
public class SlowReportRepository implements ReportRepository {

    private final AtomicInteger invocations = new AtomicInteger();

    @Override
    public String generate(String reportName) {
        invocations.incrementAndGet();
        return "report:" + reportName;
    }

    public int invocations() {
        return invocations.get();
    }
}
