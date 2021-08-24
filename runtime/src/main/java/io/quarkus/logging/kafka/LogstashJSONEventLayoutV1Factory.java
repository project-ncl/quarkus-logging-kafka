package io.quarkus.logging.kafka;

import net.logstash.log4j.JSONEventLayoutV1;

public class LogstashJSONEventLayoutV1Factory implements FormatterOrLayoutFactory {

    private static LogstashJSONEventLayoutV1Factory instance;

    public static LogstashJSONEventLayoutV1Factory getInstance() {
        if (instance == null) {
            instance = new LogstashJSONEventLayoutV1Factory();
        }
        return instance;
    }

    @Override
    public FormatterOrLayout getFormatterOrLayout() {
        JSONEventLayoutV1 layout = new JSONEventLayoutV1();
        layout.activateOptions();

        return new FormatterOrLayout(null, layout);
    }

}
