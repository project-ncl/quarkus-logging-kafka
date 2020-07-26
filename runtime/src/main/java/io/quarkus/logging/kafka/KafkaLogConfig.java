package io.quarkus.logging.kafka;

import java.util.logging.Level;

import org.apache.kafka.log4jappender.KafkaLog4jAppender;
import org.jboss.logmanager.formatters.JsonFormatter;
import org.jboss.logmanager.handlers.AsyncHandler;
import org.jboss.logmanager.handlers.AsyncHandler.OverflowAction;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(phase = ConfigPhase.RUN_TIME, name = "log.handler.kafka")
public class KafkaLogConfig {
    /**
     * Determine whether to enable the Kafka logging handler
     */
    @ConfigItem
    public boolean enabled;

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getBrokerList() brokerList}.
     */
    @ConfigItem
    public String brokerList;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getTopic() topic}.
     */
    @ConfigItem
    public String topic;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getCompressionType() compressionType}.
     */
    @ConfigItem
    public String compressionType;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSecurityProtocol() securityProtocol}.
     */
    @ConfigItem
    public String securityProtocol;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslTruststoreLocation() sslTruststoreLocation}.
     */
    @ConfigItem
    public String sslTruststoreLocation;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslTruststorePassword() sslTruststorePassword}.
     */
    @ConfigItem
    public String sslTruststorePassword;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslKeystoreType() sslKeystoreType}
     */
    @ConfigItem
    public String sslKeystoreType;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslKeystoreLocation() sslKeystoreLocation}.
     */
    @ConfigItem
    public String sslKeystoreLocation;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSslKeystorePassword() sslKeystorePassword}.
     */
    @ConfigItem
    public String sslKeystorePassword;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSaslKerberosServiceName() saslKerberosServiceName}.
     */
    @ConfigItem
    public String saslKerberosServiceName;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getClientJaasConfPath() clientJaasConfPath}.
     */
    @ConfigItem
    public String clientJaasConfPath;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getKerb5ConfPath() kerb5ConfPath}.
     */
    @ConfigItem
    public String kerb5ConfPath;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getMaxBlockMs() maxBlockMs}.
     */
    @ConfigItem
    public Integer maxBlockMs;

    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getRetries() retries}.
     */
    @ConfigItem
    public Integer retries;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getRequiredNumAcks() requiredNumAcks}.
     */
    @ConfigItem
    public Integer requiredNumAcks;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getDeliveryTimeoutMs() deliveryTimeoutMs}.
     */
    @ConfigItem
    public Integer deliveryTimeoutMs;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getIgnoreExceptions() ignoreExceptions}.
     */
    @ConfigItem
    public Boolean ignoreExceptions;
    /**
     * KafkaLog4jAppender's {@link KafkaLog4jAppender#getSyncSend() syncSend}.
     */
    @ConfigItem
    public Boolean syncSend;

    /**
     * Date format pattern used in {@link JsonFormatter} used in the logging Handler.
     *
     * @see java.text.SimpleDateFormat
     */
    @ConfigItem(defaultValue = "yyyy-MM-dd HH:mm:ss,SSS")
    public String timestampPattern;

    /**
     * The logging-kafka log level.
     */
    @ConfigItem(defaultValue = "ALL")
    public Level level;

    /**
     * Determine whether the log handler should be wrapped in an instance of {@link AsyncHandler}.
     */
    @ConfigItem
    public boolean async;
    /**
     * AsyncHandler's {@link AsyncHandler#getQueueLength() queueLength}
     */
    @ConfigItem
    public Integer asyncQueueLength;
    /**
     * AsyncHandler's {@link AsyncHandler#getOverflowAction() overflowAction}
     */
    @ConfigItem
    public OverflowAction asyncOverflowAction;

}
