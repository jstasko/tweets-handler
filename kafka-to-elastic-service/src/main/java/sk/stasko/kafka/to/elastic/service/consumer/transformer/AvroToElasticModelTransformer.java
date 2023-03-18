package sk.stasko.kafka.to.elastic.service.consumer.transformer;

import org.springframework.stereotype.Component;
import sk.stasko.elastic.model.index.impl.TwitterModelIndex;
import sk.stasko.kafka.avro.model.TwitterAvroModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
public class AvroToElasticModelTransformer {

    public List<TwitterModelIndex> getElasticModels(List<TwitterAvroModel> avroModels) {
        return avroModels.stream()
                .map(twitterAvroModel -> TwitterModelIndex
                        .builder()
                        .userId(twitterAvroModel.getUserId())
                        .id(String.valueOf(twitterAvroModel.getId()))
                        .text(twitterAvroModel.getText())
                        .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(twitterAvroModel.getCreatedAt()),
                        ZoneId.systemDefault()))
                        .build())
                .toList();
    }
}
