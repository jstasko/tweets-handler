package sk.stasko.elastic.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import sk.stasko.elastic.model.index.IndexModel;

import java.time.LocalDateTime;

// document is annotation that tells elasticsearch that class if candidate for mapping
@Document(indexName = "#{elasticConfigData.indexName}")
@Builder
@Data
public class TwitterModelIndex implements IndexModel {
    private final static String PATTERN = "uuuu-MM-dd'T'HH:mm:ssZZ";

    @JsonProperty
    private Long userId;
    @JsonProperty
    private String id;
    @JsonProperty
    private String text;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PATTERN)
    @JsonProperty
    private LocalDateTime createdAt;
}
