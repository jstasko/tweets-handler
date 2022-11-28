package sk.stasko.twitter.to.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import sk.stasko.config.TwitterToKafkaConfigData;
import sk.stasko.twitter.to.kafka.service.runner.StreamRunner;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "sk.stasko")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    private final TwitterToKafkaConfigData twitterToKafkaConfigData;
    private final StreamRunner streamRunner;

    @Autowired
    public TwitterToKafkaServiceApplication(TwitterToKafkaConfigData twitterToKafkaConfigData,
                                            StreamRunner streamRunner) {
        this.twitterToKafkaConfigData = twitterToKafkaConfigData;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Application started");
        LOG.info(Arrays.toString(twitterToKafkaConfigData.getTwitterKeywords().toArray()));
        LOG.info(twitterToKafkaConfigData.getWelcomeMessage());
        streamRunner.start();
    }
}
