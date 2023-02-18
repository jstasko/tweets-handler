package sk.stasko.twitter.to.kafka.service.mapper;

import org.springframework.stereotype.Component;
import sk.stasko.kafka.avro.model.TwitterAvroModel;
import twitter4j.v1.Status;

import java.time.ZoneId;

@Component
public class TwitterStatusToAvroMapper {
    public TwitterAvroModel getTwitterAvroModelFromStatus(Status status) {
        return TwitterAvroModel
                .newBuilder()
                .setId(status.getId())
                .setUserId(status.getUser().getId())
                .setText(status.getText())
                .setCreatedAt(status.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .build();
    }
}
