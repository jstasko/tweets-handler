package sk.stasko.twitter.to.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import sk.stasko.config.TwitterToKafkaConfigData;
import sk.stasko.twitter.to.kafka.service.init.StreamInitializer;
import sk.stasko.twitter.to.kafka.service.runner.StreamRunner;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "sk.stasko")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    private final StreamRunner streamRunner;
    private final StreamInitializer streamInitializer;


    @Autowired
    public TwitterToKafkaServiceApplication(StreamRunner streamRunner, StreamInitializer streamInitializer) {
        this.streamRunner = streamRunner;
        this.streamInitializer = streamInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("Application started");
        streamInitializer.init();
        streamRunner.start();
    }
}
