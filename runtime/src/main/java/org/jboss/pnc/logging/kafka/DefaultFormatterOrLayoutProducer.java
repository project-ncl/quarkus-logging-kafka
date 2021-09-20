package org.jboss.pnc.logging.kafka;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Layout;
import org.jboss.logmanager.formatters.JsonFormatter;
import org.jboss.logmanager.formatters.StructuredFormatter.Key;

import java.util.HashMap;
import java.util.logging.Formatter;

/**
 * Creates an instance of {@link FormatterOrLayout} containing either a default {@link Formatter} or a default
 * {@link Layout}.
 *
 * @author <a href="mailto:pkocandr@redhat.com">Petr Kocandrle</a>
 */
public class DefaultFormatterOrLayoutProducer {


    public static FormatterOrLayout kafkaLayout(String timestampPattern) {
        PncLoggingLayout layout = new PncLoggingLayout(timestampPattern);

        return new FormatterOrLayout(null, layout);
    }

    public static FormatterOrLayout jsonFormatter(String datetimeFormat) {
        HashMap<Key, String> keyOverrides = new HashMap<>();
        keyOverrides.put(Key.TIMESTAMP, "@timestamp");

        JsonFormatter formatter = new JsonFormatter(keyOverrides);
        if (!StringUtils.isEmpty(datetimeFormat)) {
            formatter.setDateFormat(datetimeFormat);
        }

        return new FormatterOrLayout(formatter, null);
    }

}
