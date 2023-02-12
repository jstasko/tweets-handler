package sk.stasko.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka-producer-config")
public class KafkaProducerConfigData {
    // what kind of key will u use ? if Long it will be long
    private String keySerializerClass;
    // name of class to serializer with
    private String valueSerializerClass;
    // options: snappy - from google - fastest, gzip, none, lz4
    private String compressionType;
    // all - we want to get ack from all replicas to be more resilient
    // 1 - wait only current brokers ack
    // 0 - no ack
    private String acks;
    // in kbs - default value
    private Integer batchSize;
    // if higher than higher throughput

    private Integer batchSizeBootsFactor;
    // add a delay on producer in case of light load
    private Integer lingerMs;
    // timeout error  after x Ms
    private Integer requestTimeoutMs;
    private Integer retryCount;
}
