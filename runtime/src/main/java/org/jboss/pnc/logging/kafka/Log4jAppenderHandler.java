package org.jboss.pnc.logging.kafka;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;
import org.jboss.logmanager.ExtHandler;
import org.jboss.logmanager.ExtLogRecord;

/**
 * A {@link ExtHandler JUL handler} wrapper for a {@link Appender log4j appender}.
 *
 * <p>
 * Based on Log4jAppenderHandler class from wildfly-core project.
 * </p>
 *
 * @author <a href="mailto:jperkins@redhat.com">James R. Perkins</a>
 * @author <a href="mailto:pkocandr@redhat.com">Petr Kocandrle</a>
 */
public class Log4jAppenderHandler extends ExtHandler {

    private volatile Appender appender = null;
    private final boolean applyLayout;

    private static final Logger loggingLogger = Logger.getLogger("org.jboss.pnc.logging.kafka.Log4jAppenderHandler");

    private static final AtomicReferenceFieldUpdater<Log4jAppenderHandler, Appender> appenderUpdater = AtomicReferenceFieldUpdater
            .newUpdater(Log4jAppenderHandler.class, Appender.class, "appender");

    public Log4jAppenderHandler() {
        applyLayout = true;
    }

    /**
     * Construct a new instance.
     *
     * @param appender the appender to delegate to
     */
    public Log4jAppenderHandler(final Appender appender) {
        // Should be true as the formatter will always be set
        this(appender, true);
    }

    /**
     * Construct a new instance, possibly applying a {@code Layout} to the given appender instance.
     *
     * @param appender the appender to delegate to
     * @param applyLayout {@code true} to apply an emulated layout, {@code false} otherwise
     */
    public Log4jAppenderHandler(final Appender appender, final boolean applyLayout) {
        this.applyLayout = applyLayout;
        if (applyLayout) {
            appender.setLayout(null);
        }
        appenderUpdater.set(this, appender);
    }

    /**
     * Get the log4j appender.
     *
     * @return the log4j appender
     */
    public Appender getAppender() {
        return appender;
    }

    /**
     * This method does nothing. It's only purpose is to be invoked so the {@link #activate()} method will be invoked
     * when log4j appenders are also {@link org.apache.log4j.spi.OptionHandler option handlers}.
     *
     * @param ignore any string value or {@code null}
     */
    public void setDummy(final String ignore) {
        // does nothing
    }

    /**
     * Activates the appender only if it's an {@link OptionHandler option handler}.
     */
    public void activate() {
        if (appender instanceof OptionHandler) {
            ((OptionHandler) appender).activateOptions();
            if (loggingLogger.isLoggable(Level.FINE)) {
                loggingLogger.fine(String.format("Invoking OptionHandler.activateOptions() on appender %s (%s)",
                        appender.getName(), appender.getClass().getCanonicalName()));
            }
        }
    }

    /**
     * Set the Log4j appender.
     *
     * @param appender the log4j appender
     */
    public void setAppender(final Appender appender) {
        if (this.appender != null) {
            close();
        }
        if (applyLayout && appender != null) {
            final Formatter formatter = getFormatter();
            appender.setLayout(formatter == null ? null : new FormatterLayout(formatter));
        }
        appenderUpdater.set(this, appender);
    }

    @Override
    public void setFormatter(final Formatter newFormatter) throws SecurityException {
        if (applyLayout) {
            final Appender appender = this.appender;
            if (appender != null) {
                appender.setLayout(new FormatterLayout(newFormatter));
            }
        }
        super.setFormatter(newFormatter);
    }

    @Override
    protected void doPublish(final ExtLogRecord record) {
        final Appender appender = this.appender;
        if (appender == null) {
            throw new IllegalStateException("The handler is closed, cannot publish to a closed handler.");
        }
        final LoggingEvent event = new LoggingEvent(record, DummyCategory.of(record.getLoggerName()));
        appender.doAppend(event);
        super.doPublish(record);
    }

    @Override
    public void flush() {
        // Do nothing (there is no equivalent method on log4j appenders).
    }

    @Override
    public void close() throws SecurityException {
        //checkAccess(this);
        final Appender appender = appenderUpdater.getAndSet(this, null);
        if (appender != null) {
            appender.close();
        }
    }

    /**
     * Dummy category for the logging event
     */
    private static final class DummyCategory extends Category {

        static DummyCategory of(final String name) {
            return new DummyCategory(name);
        }

        protected DummyCategory(String name) {
            super(name);
        }
    }

    /**
     * An emulator for log4j {@code Layout}s.
     *
     * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
     */
    public final class FormatterLayout extends Layout {
        private final Formatter formatter;

        /**
         * Construct a new instance.
         *
         * @param formatter the formatter to delegate to
         */
        public FormatterLayout(final Formatter formatter) {
            this.formatter = formatter;
        }

        @Override
        public String format(final LoggingEvent event) {
            return formatter.format(event.getLogRecord());
        }

        @Override
        public boolean ignoresThrowable() {
            // just be safe
            return false;
        }

        @Override
        public void activateOptions() {
            // options are always activated already
        }
    }
}
