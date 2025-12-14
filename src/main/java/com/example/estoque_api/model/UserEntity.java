package com.example.estoque_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String username;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean active;

    @PrePersist
    public void prePersist() {
        if (active == null) {
            active = true;
        }
    }
}
