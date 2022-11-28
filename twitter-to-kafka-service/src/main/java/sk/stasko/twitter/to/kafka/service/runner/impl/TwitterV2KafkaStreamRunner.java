package sk.stasko.twitter.to.kafka.service.runner.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import sk.stasko.config.TwitterToKafkaConfigData;
import sk.stasko.twitter.to.kafka.service.runner.StreamRunner;
import sk.stasko.twitter.to.kafka.service.runner.impl.client.TwitterV2StreamClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-v2-tweets", havingValue = "true", matchIfMissing = true)
public class TwitterV2KafkaStreamRunner implements StreamRunner {

    private final Logger LOG = LoggerFactory.getLogger(TwitterV2KafkaStreamRunner.class);
    private final TwitterToKafkaConfigData twitterToKafkaConfigData;
    private final TwitterV2StreamClient twitterV2StreamClient;

    @Autowired
    public TwitterV2KafkaStreamRunner(TwitterToKafkaConfigData twitterToKafkaConfigData, TwitterV2StreamClient twitterV2StreamClient) {
        this.twitterToKafkaConfigData = twitterToKafkaConfigData;
        this.twitterV2StreamClient = twitterV2StreamClient;
    }

    @Override
    public void start() {
        String bearerToken = twitterToKafkaConfigData.getTwitterV2BearerToken();

        if (bearerToken != null) {
            try {
                twitterV2StreamClient.setupRules(bearerToken, getRules());
                twitterV2StreamClient.connectStream(bearerToken);
            } catch (IOException | URISyntaxException e) {
                LOG.error("Error streaming tweets!", e);
                throw new RuntimeException("Error streaming tweets!", e);
            }
        } else {
            LOG.error("There was problem getting your bearer token" +
                    " please set up your bearer token in your application.yml");
            throw new RuntimeException("There was problem getting your bearer token" +
                    " please set up your bearer token in your application.yml");
        }
    }

    private Map<String, String> getRules() {
        List<String> keywords = twitterToKafkaConfigData.getTwitterKeywords();
        Map<String, String> rules = new HashMap<>();

        keywords.forEach(i -> {
            rules.put(i, "Keyword: " + i);
        });

        LOG.info("Created filter for twitter stream for keywords: {}", keywords);
        return rules;
    }

}
