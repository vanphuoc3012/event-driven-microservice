package com.event.driven.microservices.kafka.admin.client;

import com.event.driven.microservices.app.config.KafkaConfigData;
import com.event.driven.microservices.app.config.RetryConfigData;
import com.event.driven.microservices.kafka.admin.exception.KafkaClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaAdminClient {
    private final KafkaConfigData kafkaConfigData;
    private final RetryConfigData retryConfigData;
    private final AdminClient adminClient;
    private final RetryTemplate retryTemplate;
    private final WebClient webClient;

    public void createTopics() {

        try {
            retryTemplate.execute(this::doCreateTopics);
        } catch (Throwable e) {
            throw new KafkaClientException("Reached maximum number of retry for creating kafka " +
                                                   "topics !");
        }

        checkTopicsCreated();
    }

    private CreateTopicsResult doCreateTopics(RetryContext retryContext) {
        var topicNames = kafkaConfigData.getTopicNamesToCreate();
        log.info("Creating {} topics, attempt {}", topicNames, retryContext.getRetryCount());
        List<NewTopic> kafkaTopics = topicNames.stream().map(topic -> new NewTopic(
                topic.trim(),
                kafkaConfigData.getNumberPartitions(),
                kafkaConfigData.getReplicationFactor()
        )).collect(Collectors.toList());
        return adminClient.createTopics(kafkaTopics);
    }

    private void checkTopicsCreated() {
        var topics = getTopics();
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        for (String topicToCheck : kafkaConfigData.getTopicNamesToCreate()) {
            while (!isTopicCreated(topics, topicToCheck)) {
                checkMaxRetry(retryCount++, maxRetry);
                sleep(sleepTimeMs);
                sleepTimeMs *= multiplier;
                topics = getTopics();
            }
        }
    }

    public void checkSchemaRegistry() {
        int retryCount = 1;
        Integer maxRetry = retryConfigData.getMaxAttempts();
        int multiplier = retryConfigData.getMultiplier().intValue();
        Long sleepTimeMs = retryConfigData.getSleepTimeMs();
        while (!getSchemaRegistryStatusCode().is2xxSuccessful()) {
            checkMaxRetry(retryCount++, maxRetry);
            sleep(sleepTimeMs);
            sleepTimeMs *= multiplier;
        }
    }

    private HttpStatus getSchemaRegistryStatusCode() {
        try {
            return webClient
                    .get()
                    .uri(kafkaConfigData.getSchemaRegistryUrl())
                    .exchangeToMono(clientResponse -> Mono.just(clientResponse.statusCode()))
                    .block();
        } catch (Exception e) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        }
    }

    private void sleep(Long sleepTimeMs) {
        try {
            Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            throw new KafkaClientException("Error while sleeping for waiting new created topics!!");
        }
    }

    private void checkMaxRetry(Integer retry, Integer maxRetry) {
        if (retry > maxRetry) {
            throw new KafkaClientException(
                    "Reached max number of retry for reading kafka topic(s)!!");
        }
    }

    private boolean isTopicCreated(Collection<TopicListing> topics, String topicToCheck) {
        if (Objects.isNull(topics)) {
            return false;
        }
        return topics.stream().anyMatch(topic -> topic.name().equals(topicToCheck));

    }

    public Collection<TopicListing> getTopics() {
        Collection<TopicListing> topics;
        try {
            topics = retryTemplate.execute(this::doGetTopics);
        } catch (Exception e) {
            throw new KafkaClientException("Reached maximum number of retry for creating kafka " +
                                                   "topics !");
        }

        return topics;
    }

    private Collection<TopicListing> doGetTopics(RetryContext retryContext) throws ExecutionException, InterruptedException {
        log.info("Reading kafka topic {}, attempt {}", kafkaConfigData.getTopicNamesToCreate(),
                 retryContext.getRetryCount());
        Collection<TopicListing> topics = adminClient.listTopics().listings().get();
        if (Objects.nonNull(topics)) {
            topics.forEach(topic -> log.debug("Topic with name {}", topic.name()));
        }
        return topics;
    }
}
