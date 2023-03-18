package sk.stasko.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "elastic-config")
@Configuration
@Data
public class ElasticConfigData {
    private String indexName;
    private String connectionUrl;
    private Integer connectTimeoutMs;
    private Integer socketTimeoutMs;
}
