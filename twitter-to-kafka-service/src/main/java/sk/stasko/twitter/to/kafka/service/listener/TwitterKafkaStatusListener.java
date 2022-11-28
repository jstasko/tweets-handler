package sk.stasko.twitter.to.kafka.service.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.v1.Status;
import twitter4j.v1.StatusAdapter;
@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

    @Override
    public void onStatus(Status status) {
        LOG.info("Twitter status with text {}", status.getText());
    }
}
