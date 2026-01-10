package com.example.estoque_api.dto.request;

import com.example.estoque_api.dto.request.persist.ToolDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToolDTOTest {

    @Test
    @DisplayName("Should successfully instantiate the record and return correct values")
    void shouldCreateToolEntityDTOAndReturnCorrectValues() {
        var expectedName = "Laptop Gamer";
        var expectedActive = true;

        var dto = new ToolDTO(expectedName, expectedActive);

        assertAll(
                () -> assertEquals(expectedName, dto.name()),
                () -> assertEquals(expectedActive, dto.active())
        );
    }

    @Test
    @DisplayName("Should verify equality between two instances with same values")
    void shouldVerifyEquality() {
        var dto1 = new ToolDTO("Tool A", true);
        var dto2 = new ToolDTO("Tool A", true);
        var dto3 = new ToolDTO("Tool B", false);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    @DisplayName("Should verify if toString method contains all record fields")
    void shouldVerifyToString() {
        var dto = new ToolDTO("Monitor 4K", false);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("toolName=Monitor 4K")),
                () -> assertTrue(toString.contains("userActive=false"))
        );
    }
}