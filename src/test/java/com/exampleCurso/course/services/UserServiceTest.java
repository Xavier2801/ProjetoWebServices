package com.exampleCurso.course.services;

import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.repositories.UserRepository;
import com.exampleCurso.course.services.exception.DatabaseException;
import com.exampleCurso.course.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    private User u1;
    private User u2;

    @BeforeEach
    void setUp() {
        u1 = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        u2 = new User(2L, "Alex Green", "alex@gmail.com", "977777777", "123456");
    }

    // ===== findAll =====

    @Test
    @DisplayName("findAll deve retornar lista com todos os usuários")
    void findAll_deveRetornarListaDeUsuarios() {
        when(repository.findAll()).thenReturn(List.of(u1, u2));

        List<User> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).contains(u1, u2);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia quando não há usuários")
    void findAll_deveRetornarListaVazia_quandoNaoHaUsuarios() {
        when(repository.findAll()).thenReturn(List.of());

        List<User> result = service.findAll();

        assertThat(result).isEmpty();
    }

    // ===== findById =====

    @Test
    @DisplayName("findById deve retornar usuário quando id existir")
    void findById_deveRetornarUsuario_quandoIdExistir() {
        when(repository.findById(1L)).thenReturn(Optional.of(u1));

        User result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Maria Brown");
        assertThat(result.getEmail()).isEqualTo("maria@gmail.com");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando id não existir")
    void findById_deveLancarResourceNotFoundException_quandoIdNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, times(1)).findById(99L);
    }

    // ===== insert =====

    @Test
    @DisplayName("insert deve salvar e retornar usuário com id gerado")
    void insert_deveSalvarERetornarUsuario() {
        User novoUser = new User(null, "João Silva", "joao@email.com", "11999999999", "123456");
        User userSalvo = new User(3L, "João Silva", "joao@email.com", "11999999999", "123456");

        when(repository.save(novoUser)).thenReturn(userSalvo);

        User result = service.insert(novoUser);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("João Silva");
        verify(repository, times(1)).save(novoUser);
    }

    // ===== delete =====

    @Test
    @DisplayName("delete deve remover usuário quando id existir")
    void delete_deveRemoverUsuario_quandoIdExistir() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        assertThatCode(() -> service.delete(1L)).doesNotThrowAnyException();

        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("delete deve lançar ResourceNotFoundException quando id não existir")
    void delete_deveLancarResourceNotFoundException_quandoIdNaoExistir() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("delete deve lançar DatabaseException quando usuário tiver dependências")
    void delete_deveLancarDatabaseException_quandoUsuarioTiverDependencias() {
        when(repository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(1L);

        assertThatThrownBy(() -> service.delete(1L))
                .isInstanceOf(DatabaseException.class);

        verify(repository, times(1)).deleteById(1L);
    }

    // ===== update =====

    @Test
    @DisplayName("update deve atualizar e retornar usuário quando id existir")
    void update_deveAtualizarERetornarUsuario_quandoIdExistir() {
        User dadosAtualizados = new User(null, "Maria Atualizada", "maria_nova@gmail.com", "911111111", "123456");
        User userAtualizado = new User(1L, "Maria Atualizada", "maria_nova@gmail.com", "911111111", "123456");

        when(repository.getReferenceById(1L)).thenReturn(u1);
        when(repository.save(u1)).thenReturn(userAtualizado);

        User result = service.update(1L, dadosAtualizados);

        assertThat(result.getName()).isEqualTo("Maria Atualizada");
        assertThat(result.getEmail()).isEqualTo("maria_nova@gmail.com");
        assertThat(result.getPhone()).isEqualTo("911111111");
        verify(repository, times(1)).getReferenceById(1L);
        verify(repository, times(1)).save(u1);
    }

    @Test
    @DisplayName("update deve lançar ResourceNotFoundException quando id não existir")
    void update_deveLancarResourceNotFoundException_quandoIdNaoExistir() {
        User dadosAtualizados = new User(null, "Maria Atualizada", "maria_nova@gmail.com", "911111111", "123456");

        when(repository.getReferenceById(99L)).thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> service.update(99L, dadosAtualizados))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(repository, never()).save(any());
    }
}
