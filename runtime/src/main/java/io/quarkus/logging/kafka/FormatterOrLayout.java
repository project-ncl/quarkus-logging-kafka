package io.quarkus.logging.kafka;

import java.util.logging.Formatter;

import org.apache.log4j.Layout;

/**
 * Contains a {@link Formatter} or a {@link Layout} for logging handler setup.
 *
 * @author <a href="mailto:pkocandr@redhat.com">Petr Kocandrle</a>
 */
public class FormatterOrLayout {

    private Formatter formatter;

    private Layout layout;

    public FormatterOrLayout(Formatter formatter, Layout layout) {
        this.formatter = formatter;
        this.layout = layout;
    }

    public boolean hasFormatter() {
        return formatter != null;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public boolean hasLayout() {
        return layout != null;
    }

    public Layout getLayout() {
        return layout;
    }

    @Override
    public String toString() {
        return "FormatterOrLayout [formatter=" + formatter + ", layout=" + layout + "]";
    }

}
