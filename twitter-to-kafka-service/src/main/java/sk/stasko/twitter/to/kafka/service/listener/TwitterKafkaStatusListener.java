package sk.stasko.twitter.to.kafka.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sk.stasko.config.KafkaConfigData;
import sk.stasko.kafka.avro.model.TwitterAvroModel;
import sk.stasko.kafka.producer.service.KafkaProducer;
import sk.stasko.twitter.to.kafka.service.mapper.TwitterStatusToAvroMapper;
import twitter4j.v1.Status;
import twitter4j.v1.StatusAdapter;
@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);
    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
    private final TwitterStatusToAvroMapper twitterStatusToAvroMapper;

    @Autowired
    public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData, KafkaProducer<Long, TwitterAvroModel> kafkaProducer, TwitterStatusToAvroMapper twitterStatusToAvroMapper) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.twitterStatusToAvroMapper = twitterStatusToAvroMapper;
    }

    // we want to partition the data using Twitter avrom model id
    @Override
    public void onStatus(Status status) {
        LOG.info("Twitter status with text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = twitterStatusToAvroMapper.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getId(), twitterAvroModel);
    }
}
