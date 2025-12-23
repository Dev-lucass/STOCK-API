package com.example.estoque_api.controller;

import com.example.estoque_api.dto.request.UserEntityDTO;
import com.example.estoque_api.dto.response.entity.UserEntityResponseDTO;
import com.example.estoque_api.model.UserEntity;
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
    MockMvc mvc;

    @MockitoBean
    UserEntityService service;

    UserEntityDTO userDto;
    UserEntityResponseDTO responseUpdated;
    UserEntity userEntity;
    UserEntityResponseDTO response;

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

        userEntity = UserEntity.builder()
                .username(userDto.username())
                .address(userDto.address())
                .cpf(userDto.cpf())
                .active(true)
                .build();
    }

    @Test
    void should_save_user() throws Exception {

        when(service.save(any(UserEntityDTO.class)))
                .thenReturn(response);

        var json = """
                   {
                  "username": "lucas Silva",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                  }
                """;

        mvc.perform(
                        post("/api/v1/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.id()))
                .andExpect(jsonPath("$.username").value(response.username()))
                .andExpect(jsonPath("$.createdAt").value(response.createdAt().toString()));
    }

    @Test
    void throws_username_invalid_on_save_user() throws Exception {

        var json = """
                   {
                  "username": "l",
                  "cpf": "11144477735",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                  }
                """;

        mvc.perform(
                        post("/api/v1/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.StatusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void throws_cpf_invalid_on_save_user() throws Exception {

        var json = """
                   {
                  "username": "lucas silva",
                  "cpf": "11144",
                  "address": "Rua das Flores, 123, São Paulo, RJ"
                  }
                """;

        mvc.perform(
                        post("/api/v1/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.StatusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void throws_address_invalid_on_save_user() throws Exception {

        var json = """
                   {
                  "username": "lucas silva",
                  "cpf": "11144477735",
                  "address": "R"
                  }
                """;

        mvc.perform(
                        post("/api/v1/user")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.StatusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.dateError").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.invalidFields").isArray());
    }

    @Test
    void should_findAll_users_active() throws Exception {

        when(service.findAll())
                .thenReturn(List.of(response));

        mvc.perform(
                        get("/api/v1/user")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].username").value(response.username()))
                .andExpect(jsonPath("$[0].createdAt").value(response.createdAt().toString()));
    }


    @Test
    void should_update_user() throws Exception {

        when(service.findUserByIdOrElseThrow(1L))
                .thenReturn(userEntity);

        when(service.save(any(UserEntityDTO.class)))
                .thenReturn(response);

        var json = """
                   {
                  "username": "lucas Silva da silva",
                  "cpf": "05744110038",
                  "address": "Rua das Flores, 444, Rio de Janeiro"
                  }
                """;

        mvc.perform(
                        put("/api/v1/user/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());
    }

    @Test
    void should_delete_user() throws Exception {

        when(service.findUserByIdOrElseThrow(1L))
                .thenReturn(userEntity);

        doNothing().when(service)
                .disableById(Mockito.anyLong());

        mvc.perform(
                        delete("/api/v1/user/1")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }
}