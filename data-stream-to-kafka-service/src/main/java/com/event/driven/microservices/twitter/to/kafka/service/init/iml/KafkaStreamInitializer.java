package com.event.driven.microservices.twitter.to.kafka.service.init.iml;

import com.event.driven.microservices.app.config.KafkaConfigData;
import com.event.driven.microservices.kafka.admin.client.KafkaAdminClient;
import com.event.driven.microservices.twitter.to.kafka.service.init.StreamInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaStreamInitializer implements StreamInitializer {
    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    @Override
    public void init() {
        kafkaAdminClient.createTopics();
        kafkaAdminClient.checkSchemaRegistry();
        log.info("Topics with name {} is ready for operations!!",
                 kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
