package com.example.estoque_api.model;

import com.example.estoque_api.enums.InventoryAction;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryAction action;

    @Column(nullable = false)
    private int quantity;

    protected HistoryEntity() {
    }

    public HistoryEntity(UserEntity user,
                         ProductEntity product,
                         InventoryAction action,
                         int quantity) {

        this.user = user;
        this.product = product;
        this.action = action;
        this.quantity = quantity;
        this.id = new HistoryId(
                user.getId(),
                product.getId(),
                LocalDateTime.now()
        );
    }

    public HistoryId getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public InventoryAction getAction() {
        return action;
    }

    public int getQuantity() {
        return quantity;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = new HistoryId();
        }
        if (id.getCreatedAt() == null) {
            id.setCreatedAt(LocalDateTime.now());
        }
    }
}
