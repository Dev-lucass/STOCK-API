package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.service.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserEntityController.class)
@ExtendWith(MockitoExtension.class)
class UserEntityControllerTest {

    @Autowired

    @MockitoBean


    @BeforeEach
    void setUp() {
        userDto = UserEntityDTO.builder()
                .username("lucas Silva")
                .cpf("11144477735")
                .address("Rua das Flores, 123, São Paulo, RJ")
                .build();

        responseUpdated = UserEntityResponseDTO.builder()
                .id(1L)
                .username("lucas Silva da silva")
                .createdAt(LocalDate.now())
                .build();

        response = UserEntityResponseDTO.builder()
                .id(1L)
                .username(userDto.username())
                .createdAt(LocalDate.now())
                .build();
    }

    @Test

        var json = """
                {
                  "username": "lucas Silva",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.username").value(response.username()))
                .andExpect(jsonPath("$.createdAt").value(response.createdAt().toString()));
    }

    @Test
        var json = """
                {
                  "username": "l",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
        var json = """
                {
                  "cpf": "11144",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
        var json = """
                {
                  "cpf": "11144477735",
                  "address": "R"
                }
                """;

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test


                        .accept(MediaType.APPLICATION_JSON)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].username").value(response.username()))
                .andExpect(jsonPath("$[0].createdAt").value(response.createdAt().toString()));
    }


    @Test

        var json = """
                {
                  "username": "lucas Silva da silva",
                  "cpf": "05744110038",
                  "address": "Rua das Flores, 444, Rio de Janeiro"
                }
                """;

                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
    }

    @Test


                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                .andExpect(status().isNoContent());
    }
    }