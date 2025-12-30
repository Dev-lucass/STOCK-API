package com.example.estoque_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int quantityInitial;

    @Column(nullable = false)
    private int quantityCurrent;

    @Column(nullable = false)
    private UUID inventoryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false, unique = true)
    private ToolEntity tool;

    @PrePersist
    public void prePersist() {
        if (this.quantityCurrent == 0) {
            this.quantityCurrent = this.quantityInitial;
        }
    }
}
