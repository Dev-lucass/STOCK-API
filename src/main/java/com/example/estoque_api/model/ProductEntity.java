package com.example.estoque_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_product")
public class ProductEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private Boolean active;

    @PrePersist
    private void prePersist() {
        if (this.active == null) {
            this.active = false;
        }
    }


    public ProductEntity() {
    }

    public ProductEntity(Long id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
