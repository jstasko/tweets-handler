package sk.stasko.elastic.index.client.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import sk.stasko.elastic.model.index.impl.TwitterModelIndex;

@Repository
public interface TwitterElasticSearchRepository extends ElasticsearchRepository<TwitterModelIndex, String> {
}
