package com.example.estoque_api.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Data
public class HistoryId implements Serializable {
    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;
}


