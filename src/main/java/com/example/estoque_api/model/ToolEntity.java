package com.example.estoque_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ToolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private Boolean inUse;

    @Column(nullable = false)
    private LocalDateTime createdIn;

    private Double currentLifeCycle;
    private Integer usageCount;
    private LocalTime usageTime;
    private LocalTime lastUsageStart;
    private Double degradationRate;
    private Double minimumViableLife;

    @PrePersist
    public void prePersist() {
        if (active == null) active = true;
        if (inUse == null) inUse = false;
        if (usageCount == null) usageCount = 0;
        if (currentLifeCycle == null) currentLifeCycle = 100.0;
        if (lastUsageStart == null) lastUsageStart = LocalTime.MIDNIGHT;

        degradationRate = 1.5;
        minimumViableLife = 40.0;
    }
}
