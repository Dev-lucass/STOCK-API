package com.example.estoque_api.model;

import com.example.estoque_api.enums.InventoryAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    private ToolEntity tool;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InventoryAction action;

    @Column(nullable = false)
    private long inventoryId;

    @Column(nullable = false)
    private int quantityTaken;

    @Column
    private LocalDateTime createdAt;
}
