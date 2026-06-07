package com.exampleCurso.course.resources;

import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.services.UserService;
import com.exampleCurso.course.services.exception.DatabaseException;
import com.exampleCurso.course.services.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    private User u1;
    private User u2;

    @BeforeEach
    void setUp() {
        u1 = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        u2 = new User(2L, "Alex Green", "alex@gmail.com", "977777777", "123456");
    }

    // ===== GET /users =====

    @Test
    @DisplayName("GET /users deve retornar 200 com lista de usuários")
    void findAll_deveRetornar200_comListaDeUsuarios() throws Exception {
        when(service.findAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Maria Brown"))
                .andExpect(jsonPath("$[1].name").value("Alex Green"));
    }

    @Test
    @DisplayName("GET /users deve retornar 200 com lista vazia")
    void findAll_deveRetornar200_comListaVazia() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ===== GET /users/{id} =====

    @Test
    @DisplayName("GET /users/{id} deve retornar 200 com usuário quando id existir")
    void findById_deveRetornar200_quandoIdExistir() throws Exception {
        when(service.findById(1L)).thenReturn(u1);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Maria Brown"))
                .andExpect(jsonPath("$.email").value("maria@gmail.com"))
                .andExpect(jsonPath("$.phone").value("988888888"));
    }

    @Test
    @DisplayName("GET /users/{id} deve retornar 404 quando id não existir")
    void findById_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(service.findById(99L)).thenThrow(new ResourceNotFoundException(99L));

        mockMvc.perform(get("/users/99"))
                .andExpect(status().isNotFound());
    }

    // ===== POST /users =====

    @Test
    @DisplayName("POST /users deve retornar 201 com usuário criado")
    void insert_deveRetornar201_comUsuarioCriado() throws Exception {
        User userSalvo = new User(3L, "João Silva", "joao@email.com", "11999999999", "123456");

        when(service.insert(any(User.class))).thenReturn(userSalvo);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"João Silva\",\"email\":\"joao@email.com\",\"phone\":\"11999999999\",\"password\":\"123456\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(header().exists("Location"));
    }

    // ===== DELETE /users/{id} =====

    @Test
    @DisplayName("DELETE /users/{id} deve retornar 204 quando id existir")
    void delete_deveRetornar204_quandoIdExistir() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(1L);
    }

    @Test
    @DisplayName("DELETE /users/{id} deve retornar 404 quando id não existir")
    void delete_deveRetornar404_quandoIdNaoExistir() throws Exception {
        doThrow(new ResourceNotFoundException(99L)).when(service).delete(99L);

        mockMvc.perform(delete("/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /users/{id} deve retornar 400 quando usuário tiver dependências")
    void delete_deveRetornar400_quandoUsuarioTiverDependencias() throws Exception {
        doThrow(new DatabaseException("Integrity violation")).when(service).delete(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isBadRequest());
    }

    // ===== PUT /users/{id} =====

    @Test
    @DisplayName("PUT /users/{id} deve retornar 200 com usuário atualizado")
    void update_deveRetornar200_comUsuarioAtualizado() throws Exception {
        User userAtualizado = new User(1L, "Maria Atualizada", "maria_nova@gmail.com", "911111111", "123456");

        when(service.update(eq(1L), any(User.class))).thenReturn(userAtualizado);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Maria Atualizada\",\"email\":\"maria_nova@gmail.com\",\"phone\":\"911111111\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Atualizada"))
                .andExpect(jsonPath("$.email").value("maria_nova@gmail.com"));
    }

    @Test
    @DisplayName("PUT /users/{id} deve retornar 404 quando id não existir")
    void update_deveRetornar404_quandoIdNaoExistir() throws Exception {
        when(service.update(eq(99L), any(User.class))).thenThrow(new ResourceNotFoundException(99L));

        mockMvc.perform(put("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Maria Atualizada\",\"email\":\"maria_nova@gmail.com\",\"phone\":\"911111111\",\"password\":\"123456\"}"))
                .andExpect(status().isNotFound());
    }
}