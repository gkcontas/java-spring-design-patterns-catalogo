package com.gkcontas.patterns.proxy;

import java.util.Set;

/** A protection proxy: the decision to allow the call, made before the call. */
public class AccessControlledReportProxy implements ReportRepository {

    private final ReportRepository delegate;
    private final Set<String> allowedReports;

    public AccessControlledReportProxy(ReportRepository delegate, Set<String> allowedReports) {
        this.delegate = delegate;
        this.allowedReports = Set.copyOf(allowedReports);
    }

    @Override
    public String generate(String reportName) {
        if (!allowedReports.contains(reportName)) {
            throw new SecurityException("Not allowed to generate " + reportName);
        }
        return delegate.generate(reportName);
    }
}
