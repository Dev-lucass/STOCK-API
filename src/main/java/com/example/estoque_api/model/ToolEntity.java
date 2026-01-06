package com.example.estoque_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private Double currentLifeCycle;
    private Integer usageCount;
    private LocalTime usageTime;
    private Double degradationRate;
    private Double minimumViableLife;

    @PrePersist
    public void prePersist() {
        if (active == null) active = true;
        if (usageCount == null) usageCount = 0;
        if (currentLifeCycle == null) currentLifeCycle = 100.0;
        if (usageTime == null) usageTime = LocalTime.MIDNIGHT;

        degradationRate = 1.5;
        minimumViableLife = 40.0;
    }
}
