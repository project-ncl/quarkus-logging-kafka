package io.quarkus.logging.kafka;

import java.util.logging.Formatter;

import org.apache.log4j.Layout;

/**
 * Creates an instance of {@link FormatterOrLayout} containing either a {@link Formatter} or a {@link Layout}.
 *
 * @author <a href="mailto:pkocandr@redhat.com">Petr Kocandrle</a>
 */
public interface FormatterOrLayoutFactory {

    FormatterOrLayout getFormatterOrLayout();

}
