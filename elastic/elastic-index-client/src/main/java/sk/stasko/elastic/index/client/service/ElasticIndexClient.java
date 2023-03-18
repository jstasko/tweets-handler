package sk.stasko.elastic.index.client.service;

import sk.stasko.elastic.model.index.IndexModel;

import java.util.List;
import java.util.Optional;

public interface ElasticIndexClient<T extends IndexModel> {
    List<Optional<String>> save(List<T> documents);
}
