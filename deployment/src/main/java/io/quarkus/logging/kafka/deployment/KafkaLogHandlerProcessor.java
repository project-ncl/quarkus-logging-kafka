package io.quarkus.logging.kafka.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.annotations.ExecutionTime;
import io.quarkus.deployment.annotations.Record;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.LogHandlerBuildItem;
import io.quarkus.logging.kafka.KafkaLogConfig;
import io.quarkus.logging.kafka.KafkaLogHandlerRecorder;

class KafkaLogHandlerProcessor {

    private static final String FEATURE = "logging-kafka";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    @Record(ExecutionTime.RUNTIME_INIT)
    LogHandlerBuildItem build(KafkaLogHandlerRecorder recorder, KafkaLogConfig config) {
        return new LogHandlerBuildItem(recorder.initializeHandler(config));
    }

}
