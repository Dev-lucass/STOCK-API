package com.example.estoque_api.dto.request;

import com.example.estoque_api.dto.request.persist.ToolDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ToolDTOTest {

    private String name;
    private boolean active;

    @BeforeEach
    void setUp() {
        name = "Hammer";
        active = true;
    }

    @Test
    void shouldCreateToolDTOUsingBuilder() {
        var dto = ToolDTO.builder()
                .name(name)
                .active(active)
                .build();

        assertAll(
                () -> assertEquals(name, dto.name()),
                () -> assertEquals(active, dto.active())
        );
    }

    @Test
    void shouldVerifyEquality() {
        var dto1 = new ToolDTO(name, active);
        var dto2 = new ToolDTO(name, active);
        var dto3 = new ToolDTO("Screwdriver", false);

        assertAll(
                () -> assertEquals(dto1, dto2),
                () -> assertEquals(dto1.hashCode(), dto2.hashCode()),
                () -> assertNotEquals(dto1, dto3)
        );
    }

    @Test
    void shouldVerifyToString() {
        var dto = new ToolDTO(name, active);
        var toString = dto.toString();

        assertAll(
                () -> assertTrue(toString.contains("name=" + name)),
                () -> assertTrue(toString.contains("active=" + active))
        );
    }
}