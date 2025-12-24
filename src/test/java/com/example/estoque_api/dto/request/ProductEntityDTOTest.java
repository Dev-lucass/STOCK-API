package com.example.estoque_api.dto.request;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductEntityDTOTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateProductEntityDTOAndReturnCorrectValues() {
        String expectedName = "Laptop Gamer";
        Boolean expectedActive = true;

        ProductEntityDTO dto = new ProductEntityDTO(expectedName, expectedActive);

        assertAll(
                () -> assertEquals(expectedName, dto.name()),
                () -> assertEquals(expectedActive, dto.active())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        ProductEntityDTO dto1 = new ProductEntityDTO("Product A", true);
        ProductEntityDTO dto2 = new ProductEntityDTO("Product A", true);
        ProductEntityDTO dto3 = new ProductEntityDTO("Product B", false);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        ProductEntityDTO dto = new ProductEntityDTO("Monitor 4K", false);
        String toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("name=Monitor 4K")),
                () -> assertTrue(toString.contains("active=false"))
        );
    }
}