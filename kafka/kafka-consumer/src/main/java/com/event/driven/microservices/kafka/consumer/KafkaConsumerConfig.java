package com.event.driven.microservices.kafka.consumer;

import com.event.driven.microservices.app.config.KafkaConfigData;
import com.event.driven.microservices.app.config.KafkaConsumerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

@EnableKafka
@Configuration
public class KafkaConsumerConfig<K extends Serializable, V extends SpecificRecordBase> {
    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    public KafkaConsumerConfig(KafkaConfigData kafkaConfigData, KafkaConsumerConfigData kafkaConsumerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
    }

    @Bean

    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.getBootstrapServers());
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getKeyDeserializer());
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerConfigData.getValueDeserializer());
        props.put(GROUP_ID_CONFIG, kafkaConsumerConfigData.getConsumerGroupId());
        props.put(AUTO_OFFSET_RESET_CONFIG, kafkaConsumerConfigData.getAutoOffsetReset());
        props.put(kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        props.put(kafkaConsumerConfigData.getSpecificAvroReaderKey(), kafkaConsumerConfigData.getSpecificAvroReader());
        props.put(SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerConfigData.getSessionTimeOutMs());
        props.put(HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getHeartBeatIntervalsMs());
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerConfigData.getMaxPollIntervalMs());
        props.put(MAX_PARTITION_FETCH_BYTES_CONFIG,
                  kafkaConsumerConfigData.getMaxPartitionFetchBytesDefault() * kafkaConsumerConfigData.getMaxPartitionFetchBytesBoostFactor());
        props.put(MAX_POLL_RECORDS_CONFIG, kafkaConsumerConfigData.getMaxPollRecords());
        return props;
    }

    @Bean
    public ConsumerFactory<K, V> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<K, V> listenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setConsumerFactory(consumerFactory());
        listenerContainerFactory.setBatchListener(kafkaConsumerConfigData.getBatchListener());
        listenerContainerFactory.setConcurrency(kafkaConsumerConfigData.getConcurrencyLevel());
        listenerContainerFactory.setAutoStartup(kafkaConsumerConfigData.getAutoStartup());
        listenerContainerFactory.getContainerProperties().setPollTimeout(kafkaConsumerConfigData.getPollTimeOutMs());
        return listenerContainerFactory;
    }
}
