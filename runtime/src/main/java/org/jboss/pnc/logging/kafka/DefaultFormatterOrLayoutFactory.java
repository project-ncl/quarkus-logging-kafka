package org.jboss.pnc.logging.kafka;

import java.util.logging.Formatter;

import io.quarkus.arc.DefaultBean;
import net.logstash.log4j.JSONEventLayoutV1;

import org.apache.log4j.Layout;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

/**
 * Creates an instance of {@link FormatterOrLayout} containing either a default {@link Formatter} or a default
 * {@link Layout}.
 *
 * @author <a href="mailto:pkocandr@redhat.com">Petr Kocandrle</a>
 */
@Dependent
public class DefaultFormatterOrLayoutFactory {

    @Produces
    @DefaultBean
    public FormatterOrLayout formatterOrLayout() {
        JSONEventLayoutV1 layout = new JSONEventLayoutV1();
        layout.activateOptions();

        return new FormatterOrLayout(null, layout);
    }

}
