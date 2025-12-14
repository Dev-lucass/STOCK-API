package com.example.estoque_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    /**
     * vou modificar esse atributo logo apos criar o productEntity
     */
    private Long fkProduct;

    public InventoryEntity() {
    }

    public InventoryEntity(Long id, Integer quantity, Long fkProduct) {
        this.id = id;
        this.quantity = quantity;
        this.fkProduct = fkProduct;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFkProduct() {
        return fkProduct;
    }

    public void setFkProduct(Long fkProduct) {
        this.fkProduct = fkProduct;
    }
}
