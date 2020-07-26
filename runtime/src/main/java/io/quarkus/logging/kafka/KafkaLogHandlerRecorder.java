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

    @SuppressWarnings("resource")
    public RuntimeValue<Optional<Handler>> initializeHandler(final KafkaLogConfig config) {
        if (!config.enabled) {
            return new RuntimeValue<>(Optional.empty());
        }

        KafkaLog4jAppender appender = createAppender(config);

        Log4jAppenderHandler kafkaHandler = new Log4jAppenderHandler(appender);
        if (config.level != null) {
            kafkaHandler.setLevel(config.level);
        }
        if (config.timestampPattern != null) {
            JsonFormatter formatter = new JsonFormatter();
            formatter.setDateFormat(config.timestampPattern);
            kafkaHandler.setFormatter(formatter);
        }

        ExtHandler handler;

        if (config.async) {
            AsyncHandler asyncWrapper;
            if (config.asyncQueueLength != null) {
                asyncWrapper = new AsyncHandler(config.asyncQueueLength);
            } else {
                asyncWrapper = new AsyncHandler();
            }
            if (config.asyncOverflowAction != null) {
                asyncWrapper.setOverflowAction(config.asyncOverflowAction);
            }
            asyncWrapper.setLevel(config.level);

            asyncWrapper.addHandler(kafkaHandler);

            handler = asyncWrapper;
        } else {
            handler = kafkaHandler;
        }

        return new RuntimeValue<>(Optional.of(handler));
    }

    private KafkaLog4jAppender createAppender(final KafkaLogConfig config) {
        KafkaLog4jAppender appender = new KafkaLog4jAppender();
        if (config.brokerList != null) {
            appender.setBrokerList(config.brokerList);
        }
        if (config.topic != null) {
            appender.setTopic(config.topic);
        }
        if (config.compressionType != null) {
            appender.setCompressionType(config.compressionType);
        }
        if (config.securityProtocol != null) {
            appender.setSecurityProtocol(config.securityProtocol);
        }
        if (config.sslTruststoreLocation != null) {
            appender.setSslTruststoreLocation(config.sslTruststoreLocation);
        }
        if (config.sslTruststorePassword != null) {
            appender.setSslTruststorePassword(config.sslTruststorePassword);
        }
        if (config.sslKeystoreType != null) {
            appender.setSslKeystoreType(config.sslKeystoreType);
        }
        if (config.sslKeystoreLocation != null) {
            appender.setSslKeystoreLocation(config.sslKeystoreLocation);
        }
        if (config.sslKeystorePassword != null) {
            appender.setSslKeystorePassword(config.sslKeystorePassword);
        }
        if (config.saslKerberosServiceName != null) {
            appender.setSaslKerberosServiceName(config.saslKerberosServiceName);
        }
        if (config.clientJaasConfPath != null) {
            appender.setClientJaasConfPath(config.clientJaasConfPath);
        }
        if (config.kerb5ConfPath != null) {
            appender.setKerb5ConfPath(config.kerb5ConfPath);
        }
        if (config.maxBlockMs != null) {
            appender.setMaxBlockMs(config.maxBlockMs);
        }

        if (config.retries != null) {
            appender.setRetries(config.retries);
        }
        if (config.requiredNumAcks != null) {
            appender.setRequiredNumAcks(config.requiredNumAcks);
        }
        if (config.deliveryTimeoutMs != null) {
            appender.setDeliveryTimeoutMs(config.deliveryTimeoutMs);
        }
        if (config.ignoreExceptions != null) {
            appender.setIgnoreExceptions(config.ignoreExceptions);
        }
        if (config.syncSend != null) {
            appender.setSyncSend(config.syncSend);
        }
        return appender;
    }
}
