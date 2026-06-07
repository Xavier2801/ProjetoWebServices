package com.exampleCurso.course.services;

import com.exampleCurso.course.entities.Order;
import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.entities.enums.OrderStatus;
import com.exampleCurso.course.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    private User client;
    private Order o1;
    private Order o2;

    @BeforeEach
    void setUp() {
        client = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");

        o1 = new Order(1L, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.WAITING_PAYMENT, client);
        o2 = new Order(2L, Instant.parse("2019-07-21T03:42:20Z"), OrderStatus.PAID, client);
    }

    // ===== findAll =====

    @Test
    @DisplayName("findAll deve retornar lista com todos os pedidos")
    void findAll_deveRetornarListaDePedidos() {
        when(repository.findAll()).thenReturn(List.of(o1, o2));

        List<Order> result = service.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).contains(o1, o2);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia quando não há pedidos")
    void findAll_deveRetornarListaVazia_quandoNaoHaPedidos() {
        when(repository.findAll()).thenReturn(List.of());

        List<Order> result = service.findAll();

        assertThat(result).isEmpty();
    }

    // ===== findById =====

    @Test
    @DisplayName("findById deve retornar pedido quando id existir")
    void findById_deveRetornarPedido_quandoIdExistir() {
        when(repository.findById(1L)).thenReturn(Optional.of(o1));

        Order result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.WAITING_PAYMENT);
        assertThat(result.getClient().getName()).isEqualTo("Maria Brown");
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve lançar NoSuchElementException quando id não existir")
    void findById_deveLancarExcecao_quandoIdNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(RuntimeException.class);

        verify(repository, times(1)).findById(99L);
    }

    // ===== insert =====

    @Test
    @DisplayName("insert deve salvar e retornar pedido com id gerado")
    void insert_deveSalvarERetornarPedido() {
        Order novoPedido = new Order(null, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.WAITING_PAYMENT, client);
        Order pedidoSalvo = new Order(3L, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.WAITING_PAYMENT, client);

        when(repository.save(novoPedido)).thenReturn(pedidoSalvo);

        Order result = service.insert(novoPedido);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.WAITING_PAYMENT);
        verify(repository, times(1)).save(novoPedido);
    }

    // ===== status =====

    @Test
    @DisplayName("pedido deve ter status WAITING_PAYMENT ao ser criado")
    void pedido_deveTerStatusWaitingPayment_aoSerCriado() {
        when(repository.findById(1L)).thenReturn(Optional.of(o1));

        Order result = service.findById(1L);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.WAITING_PAYMENT);
    }

    @Test
    @DisplayName("pedido deve ter status PAID quando pago")
    void pedido_deveTerStatusPaid_quandoPago() {
        when(repository.findById(2L)).thenReturn(Optional.of(o2));

        Order result = service.findById(2L);

        assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }
}
