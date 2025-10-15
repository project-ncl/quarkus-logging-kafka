package org.jboss.pnc.logging.kafka;

import java.util.Optional;
import java.util.logging.Level;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import org.apache.kafka.log4jappender.KafkaLog4jAppender;
import org.jboss.logmanager.handlers.AsyncHandler;
import org.jboss.logmanager.handlers.AsyncHandler.OverflowAction;

@ConfigMapping(prefix = "quarkus.log.handler.kafka")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface KafkaLogConfig {

    /**
     * Determine whether to enable the Kafka logging handler
     */
    @WithDefault("false")
    boolean enabled();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getBrokerList() brokerList}.
     */
    String brokerList();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getTopic() topic}.
     */
    String topic();

    /**
     * The pattern used in message formatter to format datetime of log messages.
     */
    Optional<String> timestampPattern();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getCompressionType() compressionType}.
     */
    Optional<String> compressionType();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSecurityProtocol() securityProtocol}.
     */
    Optional<String> securityProtocol();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslTruststoreLocation() sslTruststoreLocation}.
     */
    Optional<String> sslTruststoreLocation();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslTruststorePassword() sslTruststorePassword}.
     */
    Optional<String> sslTruststorePassword();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslKeystoreType() sslKeystoreType}
     */
    Optional<String> sslKeystoreType();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslKeystoreLocation() sslKeystoreLocation}.
     */
    Optional<String> sslKeystoreLocation();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslKeystorePassword() sslKeystorePassword}.
     */
    Optional<String> sslKeystorePassword();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSaslKerberosServiceName() saslKerberosServiceName}.
     */
    Optional<String> saslKerberosServiceName();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getClientJaasConfPath() clientJaasConfPath}.
     */
    Optional<String> clientJaasConfPath();

    /**
     * Provide the client Jaas SASL login details via a string
     */
    Optional<String> saslJaasConf();

    /**
     * Allow user to specify SASL mechanism for authentication to Kafka.
     */
    Optional<String> saslMechanism();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getKerb5ConfPath() kerb5ConfPath}.
     */
    Optional<String> kerb5ConfPath();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getMaxBlockMs() maxBlockMs}.
     */
    Optional<Integer> maxBlockMs();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getRetries() retries}.
     */
    Optional<Integer> retries();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getRequiredNumAcks() requiredNumAcks}.
     */
    Optional<Integer> requiredNumAcks();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getDeliveryTimeoutMs() deliveryTimeoutMs}.
     */
    Optional<Integer> deliveryTimeoutMs();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getIgnoreExceptions() ignoreExceptions}.
     */
    Optional<Boolean> ignoreExceptions();

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSyncSend() syncSend}.
     */
    Optional<Boolean> syncSend();

    /**
     * The logging-kafka log level.
     */
    @WithDefault("ALL")
    Level level();

    /**
     * Determine whether the log handler should be wrapped in an instance of {@link AsyncHandler}. {@code true} by
     * default.
     */
    @WithDefault("true")
    boolean async();

    /**
     * AsyncHandler's {@link AsyncHandler#getQueueLength() queueLength}
     */
    Optional<Integer> asyncQueueLength();

    /**
     * AsyncHandler's {@link AsyncHandler#getOverflowAction() overflowAction}
     */
    Optional<OverflowAction> asyncOverflowAction();

    /**
     * Pattern for filtering messages by logger name. When set, only messages with logger name matching the pattern will
     * be accepted by the appender.
     */
    Optional<String> filterLoggerNamePattern();
}
