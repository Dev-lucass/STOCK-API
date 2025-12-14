package com.example.estoque_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_history")
public class HistoryEntity {

    @EmbeddedId
    private HistoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    public HistoryEntity() {}

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = new HistoryId();
        }

        if (id.getCreatedAt() == null) {
            id.setCreatedAt(LocalDateTime.now());
        }
    }

    public HistoryEntity(UserEntity user, ProductEntity product) {
        this.user = user;
        this.product = product;
        this.id = new HistoryId(
                user.getId(),
                product.getId(),
                LocalDateTime.now()
        );
    }


}
