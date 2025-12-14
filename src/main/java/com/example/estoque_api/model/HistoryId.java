package com.example.estoque_api.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Embeddable
public class HistoryId implements Serializable {


    private Long userId;
    private Long productId;
    private LocalDateTime createdAt;

    public HistoryId() {
    }

    public HistoryId(Long userId, Long productId, LocalDateTime createdAt) {
        this.userId = userId;
        this.productId = productId;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryId)) return false;
        HistoryId historyId = (HistoryId) o;
        return Objects.equals(userId, historyId.userId)
                && Objects.equals(productId, historyId.productId)
                && Objects.equals(createdAt, historyId.createdAt);
    }

    @Override
    public int hashCode() {return Objects.hash(userId, productId, createdAt);}
}


