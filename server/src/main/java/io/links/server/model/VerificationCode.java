package io.links.server.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Document("verification_code")
@Data
@Builder
public class VerificationCode {
    @Id
    private String id;
    private String code;
    private LocalDateTime created_dateTime;
    @DocumentReference
    private User user;
}
