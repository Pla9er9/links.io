package io.links.server.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document("image")
@Data
@Builder
public class Image {
    @Id
    private String id;
    @DocumentReference
    private User user;
    private Binary image;
    private LocalDateTime uploadTime;
}

