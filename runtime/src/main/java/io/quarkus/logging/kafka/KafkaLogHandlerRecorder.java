package io.quarkus.logging.kafka;

import java.util.Optional;
import java.util.logging.Handler;

import org.apache.kafka.log4jappender.KafkaLog4jAppender;
import org.jboss.logmanager.ExtHandler;
import org.jboss.logmanager.formatters.JsonFormatter;
import org.jboss.logmanager.handlers.AsyncHandler;

import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class KafkaLogHandlerRecorder {

    /**
     * Kafka logging handler initialization based on the config.
     *
     * @param config the kafka log config
     * @return initialized handler if configured
     */
    public RuntimeValue<Optional<Handler>> initializeHandler(final KafkaLogConfig config) {
        if (!config.enabled) {
            System.out.print("Kafka logger config is DISABLED!");
            return new RuntimeValue<>(Optional.empty());
        }
        System.out.print("Kafka logger config is ENABLED!");

        KafkaLog4jAppender appender = createAppender(config);

        Log4jAppenderHandler kafkaHandler = new Log4jAppenderHandler(appender);

        kafkaHandler.setLevel(config.level);

        JsonFormatter formatter = new JsonFormatter();
        formatter.setDateFormat(config.timestampPattern);
        kafkaHandler.setFormatter(formatter);

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

        return appender;
    }

}
