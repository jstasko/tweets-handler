package sk.stasko.kafka.to.elastic.service.consumer.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sk.stasko.config.KafkaConfigData;
import sk.stasko.config.KafkaConsumerConfigData;
import sk.stasko.elastic.index.client.service.ElasticIndexClient;
import sk.stasko.elastic.model.index.impl.TwitterModelIndex;
import sk.stasko.kafka.admin.config.client.KafkaAdminClient;
import sk.stasko.kafka.avro.model.TwitterAvroModel;
import sk.stasko.kafka.to.elastic.service.consumer.KafkaConsumer;
import sk.stasko.kafka.to.elastic.service.consumer.transformer.AvroToElasticModelTransformer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaConsumer.class);
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;

    private final KafkaConsumerConfigData kafkaConsumerConfigData;

    private final AvroToElasticModelTransformer avroToElasticModelTransformer;

    private final ElasticIndexClient<TwitterModelIndex> elasticIndexClient;


    @Autowired
    public TwitterKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                KafkaAdminClient kafkaAdminClient,
                                KafkaConfigData kafkaConfigData,
                                KafkaConsumerConfigData kafkaConsumerConfigData,
                                AvroToElasticModelTransformer avroToElasticModelTransformer,
                                ElasticIndexClient<TwitterModelIndex> elasticIndexClient) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.avroToElasticModelTransformer = avroToElasticModelTransformer;
        this.elasticIndexClient = elasticIndexClient;
    }


    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations!", kafkaConfigData.getTopicNamesToCreate().toArray());
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(this.kafkaConsumerConfigData.getConsumerGroupId())).start();

    }

    @Override
    @KafkaListener(id="${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        LOG.info("{} number of message received with keys {}, partitions {} and offset {}, " +
                "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

        List<TwitterModelIndex> twitterModelIndices = avroToElasticModelTransformer.getElasticModels(messages);
        List<Optional<String>> ids = elasticIndexClient.save(twitterModelIndices);
        LOG.info("Documents saved to elasticsearch with ids {}", ids.toArray());
    }
}
