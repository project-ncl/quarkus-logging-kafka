package io.quarkus.logging.kafka;

import java.util.Optional;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.kafka.log4jappender.KafkaLog4jAppender;
import org.apache.log4j.Layout;
import org.jboss.logmanager.ExtHandler;
import org.jboss.logmanager.handlers.AsyncHandler;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

/**
 * The recorder providing configured {@link KafkaLog4jAppender} wrapped in a {@link Log4jAppenderHandler}. Optionally
 * the result can be wrapped in an {@link AsyncHandler} instance. The format of the produced log can be defined by
 * a {@link Formatter} or a {@link Layout}. That can be injected by implementation of {@link FormatterOrLayoutFactory}.
 * If no implementation of that interface is available, it uses {@link net.logstash.log4j.JSONEventLayoutV1}.
 *
 * @author <a href="mailto:pkocandr@redhat.com">Petr Kocandrle</a>
 */
@Recorder
public class KafkaLogHandlerRecorder {

    private static final Logger loggingLogger = Logger.getLogger("io.quarkus.logging.kafka");

    @Inject
    private Optional<FormatterOrLayoutFactory> formatterOrLayoutFactory;

    /**
     * Kafka logging handler initialization based on the config.
     *
     * @param config the kafka log config
     * @return initialized handler if configured
     */
    public RuntimeValue<Optional<Handler>> initializeHandler(final KafkaLogConfig config) {
        if (!config.enabled) {
            return new RuntimeValue<>(Optional.empty());
        }

        KafkaLog4jAppender appender = createAppender(config);
        loggingLogger.info("Configured with layout: " + appender.getLayout());

        Log4jAppenderHandler kafkaHandler = new Log4jAppenderHandler(appender, false);

        kafkaHandler.setLevel(config.level);

        // set a formatter or a layout
        FormatterOrLayout formatterOrLayout;
        if (formatterOrLayoutFactory != null && formatterOrLayoutFactory.isPresent()) {
            formatterOrLayout = formatterOrLayoutFactory.get().getFormatterOrLayout();
        } else {
            formatterOrLayout = LogstashJSONEventLayoutV1Factory.getInstance().getFormatterOrLayout();
        }
        if (formatterOrLayout == null) {
            loggingLogger.warning("No formatter or layout for kafka logger provided.");
        } else if (formatterOrLayout.hasLayout()) {
            appender.setLayout(formatterOrLayout.getLayout());
        } else if (formatterOrLayout.hasFormatter()) {
            kafkaHandler.setFormatter(formatterOrLayout.getFormatter());
        } else {
            loggingLogger.warning(
                    "No formatter or layout for kafka logger was prensent in the FormatterOrLayout instance: "
                            + formatterOrLayout);
        }

        ExtHandler rootHandler;

        if (config.async) {
            AsyncHandler asyncWrapper;
            if (config.asyncQueueLength.isPresent()) {
                asyncWrapper = new AsyncHandler(config.asyncQueueLength.get());
            } else {
                asyncWrapper = new AsyncHandler();
            }
            config.asyncOverflowAction.ifPresent(action -> asyncWrapper.setOverflowAction(action));
            asyncWrapper.setLevel(config.level);

            asyncWrapper.addHandler(kafkaHandler);

            rootHandler = asyncWrapper;
        } else {
            rootHandler = kafkaHandler;
        }

        return new RuntimeValue<>(Optional.of(rootHandler));
    }

    private KafkaLog4jAppender createAppender(final KafkaLogConfig config) {
        loggingLogger.info("Processing config to create KafkaLog4jAppender: " + config);

        KafkaLog4jAppender appender = new KafkaLog4jAppender();
        appender.setBrokerList(config.brokerList);
        appender.setTopic(config.topic);

        config.compressionType.ifPresent(type -> appender.setCompressionType(type));
        config.securityProtocol.ifPresent(protocol -> appender.setSecurityProtocol(protocol));
        config.sslTruststoreLocation.ifPresent(location -> appender.setSslTruststoreLocation(location));
        config.sslTruststorePassword.ifPresent(password -> appender.setSslTruststorePassword(password));
        config.sslKeystoreType.ifPresent(type -> appender.setSslKeystoreType(type));
        config.sslKeystoreLocation.ifPresent(location -> appender.setSslKeystoreLocation(location));
        config.sslKeystorePassword.ifPresent(password -> appender.setSslKeystorePassword(password));
        config.saslKerberosServiceName.ifPresent(name -> appender.setSaslKerberosServiceName(name));
        config.clientJaasConfPath.ifPresent(path -> appender.setClientJaasConfPath(path));
        config.kerb5ConfPath.ifPresent(path -> appender.setKerb5ConfPath(path));
        config.maxBlockMs.ifPresent(maxBlockMs -> appender.setMaxBlockMs(maxBlockMs));

        config.retries.ifPresent(retries -> appender.setRetries(retries));
        config.requiredNumAcks.ifPresent(acks -> appender.setRequiredNumAcks(acks));
        config.deliveryTimeoutMs.ifPresent(timeout -> appender.setDeliveryTimeoutMs(timeout));
        config.ignoreExceptions.ifPresent(ignoreExceptions -> appender.setIgnoreExceptions(ignoreExceptions));
        config.syncSend.ifPresent(syncSend -> appender.setSyncSend(syncSend));

        loggingLogger.finer("Running appender.activateOptions()");
        appender.activateOptions();
        loggingLogger.finer("Finished appender.activateOptions()");

        return appender;
    }

}
