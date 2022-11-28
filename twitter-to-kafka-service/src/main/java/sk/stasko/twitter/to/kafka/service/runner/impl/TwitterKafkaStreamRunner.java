package sk.stasko.twitter.to.kafka.service.runner.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import sk.stasko.config.TwitterToKafkaConfigData;
import sk.stasko.twitter.to.kafka.service.listener.TwitterKafkaStatusListener;
import sk.stasko.twitter.to.kafka.service.runner.StreamRunner;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.v1.FilterQuery;
import twitter4j.v1.TwitterStream;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Component
@ConditionalOnProperty(name = "twitter-to-kafka-service.enable-v2-tweets", havingValue = "false")
public class TwitterKafkaStreamRunner implements StreamRunner {

    private final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStreamRunner.class);
    private final TwitterToKafkaConfigData twitterToKafkaConfigData;
    private final TwitterKafkaStatusListener twitterKafkaStatusListener;

    private TwitterStream twitterStream;

    @Autowired
    public TwitterKafkaStreamRunner(TwitterToKafkaConfigData twitterToKafkaConfigData,
                                    TwitterKafkaStatusListener twitterKafkaStatusListener) {
        this.twitterToKafkaConfigData = twitterToKafkaConfigData;
        this.twitterKafkaStatusListener = twitterKafkaStatusListener;
    }

    @Override
    public void start() throws TwitterException {
        twitterStream = Twitter
                .newBuilder()
                .listener(twitterKafkaStatusListener)
                .build().v1().stream();

        addFilter();
    }

    @PreDestroy
    public void shutdown() {
        if (twitterStream != null) {
            LOG.info("Closing twitter stream!");
            twitterStream.shutdown();
        }
    }

    private void addFilter() {
        String[] keywords = twitterToKafkaConfigData.getTwitterKeywords().toArray(new String[0]);
        FilterQuery filterQuery = FilterQuery.ofTrack(keywords);
        twitterStream.filter(filterQuery);
        LOG.info("Started filtering tweets for keywords {}", Arrays.toString(keywords));
    }
}
