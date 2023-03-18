package sk.stasko.elastic.index.client.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;
import sk.stasko.config.ElasticConfigData;
import sk.stasko.elastic.index.client.service.ElasticIndexClient;
import sk.stasko.elastic.index.client.util.ElasticIndexUtil;
import sk.stasko.elastic.model.index.impl.TwitterModelIndex;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "false")
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterModelIndex> {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticIndexClient.class);
    private final ElasticIndexUtil<TwitterModelIndex> elasticIndexUtil;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticConfigData elasticConfigData;

    @Autowired
    public TwitterElasticIndexClient(ElasticIndexUtil<TwitterModelIndex> elasticIndexUtil,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticConfigData elasticConfigData) {
        this.elasticIndexUtil = elasticIndexUtil;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticConfigData = elasticConfigData;
    }

    @Override
    public List<Optional<String>> save(List<TwitterModelIndex> documents) {
        List<IndexQuery> indexQueries = this.elasticIndexUtil.getIndexQueries(documents);
        List<IndexedObjectInformation> savedDocuments = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticConfigData.getIndexName())
        );

        List<Optional<String>> documentsIds = savedDocuments.stream()
                .map(d -> Optional.ofNullable(d.getId())).toList();

        LOG.info("Documents indexed successfully with type: {} and ids: {}", TwitterModelIndex.class.getName(),
                documentsIds);

        return documentsIds;
    }
}
