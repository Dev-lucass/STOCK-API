package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.exceptions.DuplicateResouceException;
import com.example.estoque_api.exceptions.ResourceNotFoundException;
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
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserEntityController.class)
@ExtendWith(MockitoExtension.class)
class UserEntityControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserEntityService service;

    private UserEntityDTO userDto;
    private UserEntityResponseDTO responseUpdated;
    private UserEntityResponseDTO response;

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
    void should_save_user_successfully() throws Exception {
        when(service.save(any(UserEntityDTO.class))).thenReturn(response);

        var json = """
                {
                  "username": "lucas Silva",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

        mvc.perform(post("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.username").value(response.username()))
                .andExpect(jsonPath("$.createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_throw_bad_request_when_username_invalid() throws Exception {
        var json = """
                {
                  "username": "l",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

        mvc.perform(post("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void should_throw_bad_request_when_cpf_invalid() throws Exception {
        var json = """
                {
                  "username": "lucas Silva",
                  "cpf": "11144",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

        mvc.perform(post("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void should_throw_bad_request_when_address_invalid() throws Exception {
        var json = """
                {
                  "username": "lucas Silva",
                  "cpf": "11144477735",
                  "address": "R"
                }
                """;

        mvc.perform(post("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void should_throw_conflict_when_cpf_duplicate_on_create() throws Exception {
        when(service.save(any(UserEntityDTO.class)))
                .thenThrow(new DuplicateResouceException("User already registered"));

        var json = """
                {
                  "username": "lucas Silva",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                }
                """;

        mvc.perform(post("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("User already registered"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }

    @Test
    void should_find_all_active_users() throws Exception {
        when(service.findAll()).thenReturn(List.of(response));

        mvc.perform(get("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].username").value(response.username()))
                .andExpect(jsonPath("$[0].createdAt").value(response.createdAt().toString()));
    }

    @Test
    void should_return_empty_list_when_no_active_users() throws Exception {
        when(service.findAll()).thenReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/user")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_update_user_successfully() throws Exception {
        when(service.update(eq(1L), any(UserEntityDTO.class))).thenReturn(responseUpdated);

        var json = """
                {
                  "username": "lucas Silva da silva",
                  "cpf": "05744110038",
                  "address": "Rua das Flores, 444, Rio de Janeiro"
                }
                """;

        mvc.perform(put("/api/v1/user/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(responseUpdated.id()))
                .andExpect(jsonPath("$.id").value(responseUpdated.id()))
                .andExpect(jsonPath("$.createdAt").value(responseUpdated.createdAt().toString()));
    }

    @Test
    void should_throw_bad_request_when_update_nonexistent_user() throws Exception {
        when(service.update(eq(40L), any(UserEntityDTO.class)))
                .thenThrow(new ResourceNotFoundException("Invalid user id"));

        var json = """
                {
                  "username": "lucas Silva da silva",
                  "cpf": "05744110038",
                  "address": "Rua das Flores, 444, Rio de Janeiro"
                }
                """;

        mvc.perform(put("/api/v1/user/40")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid user id"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }

    @Test
    void should_throw_conflict_when_update_user_with_duplicate_cpf() throws Exception {
        when(service.update(eq(1L), any(UserEntityDTO.class)))
                .thenThrow(new DuplicateResouceException("User already registered"));

        var json = """
                {
                  "username": "lucas Silva da silva",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 444, Rio de Janeiro"
                }
                """;

        mvc.perform(put("/api/v1/user/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.message").value("User already registered"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }

    @Test
    void should_delete_user_successfully() throws Exception {
        doNothing().when(service).disableById(1L);

        mvc.perform(delete("/api/v1/user/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_throw_bad_request_when_delete_nonexistent_user() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Invalid user id"))
                .when(service).disableById(40L);

        mvc.perform(delete("/api/v1/user/40")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Invalid user id"))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()));
    }
}
