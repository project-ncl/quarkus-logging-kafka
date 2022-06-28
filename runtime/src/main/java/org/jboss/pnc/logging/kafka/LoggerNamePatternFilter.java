package org.jboss.pnc.logging.kafka;

import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.regex.Pattern;

public class LoggerNamePatternFilter implements Filter {
    private final Pattern pattern;

    public LoggerNamePatternFilter(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean isLoggable(LogRecord logRecord) {
        return pattern.matcher(logRecord.getLoggerName()).matches();
    }
}