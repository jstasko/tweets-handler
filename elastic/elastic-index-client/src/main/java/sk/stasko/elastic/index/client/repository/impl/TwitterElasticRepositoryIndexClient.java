package sk.stasko.elastic.index.client.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import sk.stasko.elastic.index.client.repository.TwitterElasticSearchRepository;
import sk.stasko.elastic.index.client.service.ElasticIndexClient;
import sk.stasko.elastic.model.index.impl.TwitterModelIndex;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterModelIndex> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);
    private final TwitterElasticSearchRepository twitterElasticSearchRepository;

    @Autowired
    public TwitterElasticRepositoryIndexClient(TwitterElasticSearchRepository twitterElasticSearchRepository) {
        this.twitterElasticSearchRepository = twitterElasticSearchRepository;
    }

    @Override
    public List<Optional<String>> save(List<TwitterModelIndex> documents) {
        List<TwitterModelIndex> savedDocuments =
                (List<TwitterModelIndex>) this.twitterElasticSearchRepository.saveAll(documents);
        List<Optional<String>> ids = savedDocuments
                .stream().map(model -> Optional.ofNullable(model.getId())).toList();

        LOG.info("Documents indexed successfully with type: {} and ids: {}", TwitterModelIndex.class.getName(),
                ids);

        return ids;
    }
}
