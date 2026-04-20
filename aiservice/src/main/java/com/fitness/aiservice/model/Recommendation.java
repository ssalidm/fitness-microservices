package com.fitness.aiservice.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendations")
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safety;

    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recommendation rec)) return false;
        return id != null && id.equals(rec.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
