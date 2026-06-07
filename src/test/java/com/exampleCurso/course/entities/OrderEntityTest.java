package com.exampleCurso.course.entities;

import com.exampleCurso.course.entities.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class OrderEntityTest {

    private User client;
    private Order order;
    private Product p1;
    private Product p2;
    private Product p3;

    @BeforeEach
    void setUp() {
        client = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        order = new Order(1L, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.WAITING_PAYMENT, client);

        p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum.", 90.5, "");
        p2 = new Product(2L, "Macbook Pro", "Nam eleifend.", 1250.0, "");
        p3 = new Product(3L, "Rails for Dummies", "Cras fringilla.", 100.99, "");
    }

    // ===== getTotal =====

    @Test
    @DisplayName("getTotal deve retornar zero quando pedido não tem itens")
    void getTotal_deveRetornarZero_quandoPedidoSemItens() {
        assertThat(order.getTotal()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getTotal deve retornar soma correta com um item")
    void getTotal_deveRetornarSomaCorreta_comUmItem() {
        // Lord of the Rings x2 = 181.0
        OrderItem oi1 = new OrderItem(order, p1, 2, p1.getPrice());
        order.getItems().add(oi1);

        assertThat(order.getTotal()).isEqualTo(181.0);
    }

    @Test
    @DisplayName("getTotal deve retornar soma correta com múltiplos itens")
    void getTotal_deveRetornarSomaCorreta_comMultiplosItens() {
        // Lord of the Rings x2 = 181.0
        // Macbook Pro x1 = 1250.0
        // Total = 1431.0
        OrderItem oi1 = new OrderItem(order, p1, 2, p1.getPrice());
        OrderItem oi2 = new OrderItem(order, p2, 1, p2.getPrice());
        order.getItems().add(oi1);
        order.getItems().add(oi2);

        assertThat(order.getTotal()).isEqualTo(1431.0);
    }

    @Test
    @DisplayName("getTotal deve retornar soma correta com três itens")
    void getTotal_deveRetornarSomaCorreta_comTresItens() {
        // Lord of the Rings x2 = 181.0
        // Macbook Pro x1 = 1250.0
        // Rails for Dummies x2 = 201.98
        // Total = 1632.98
        OrderItem oi1 = new OrderItem(order, p1, 2, p1.getPrice());
        OrderItem oi2 = new OrderItem(order, p2, 1, p2.getPrice());
        OrderItem oi3 = new OrderItem(order, p3, 2, p3.getPrice());
        order.getItems().add(oi1);
        order.getItems().add(oi2);
        order.getItems().add(oi3);

        assertThat(order.getTotal()).isCloseTo(1632.98, within(0.01));
    }

    // ===== getSubTotal =====

    @Test
    @DisplayName("getSubTotal deve retornar price x quantity corretamente")
    void getSubTotal_deveRetornarPriceVezesQuantity() {
        OrderItem oi = new OrderItem(order, p1, 2, p1.getPrice());

        assertThat(oi.getSubTotal()).isEqualTo(181.0);
    }

    @Test
    @DisplayName("getSubTotal deve retornar valor correto para quantidade 1")
    void getSubTotal_deveRetornarValorCorreto_paraQuantidadeUm() {
        OrderItem oi = new OrderItem(order, p2, 1, p2.getPrice());

        assertThat(oi.getSubTotal()).isEqualTo(1250.0);
    }

    @Test
    @DisplayName("getSubTotal deve retornar valor correto para quantidade maior")
    void getSubTotal_deveRetornarValorCorreto_paraQuantidadeMaior() {
        OrderItem oi = new OrderItem(order, p3, 3, p3.getPrice());

        assertThat(oi.getSubTotal()).isCloseTo(302.97, within(0.01));
    }

    // ===== OrderStatus =====

    @Test
    @DisplayName("order deve ter status correto ao ser criado")
    void order_deveTerStatusCorreto_aoSerCriado() {
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.WAITING_PAYMENT);
    }

    @Test
    @DisplayName("order deve atualizar status corretamente")
    void order_deveAtualizarStatus_corretamente() {
        order.setOrderStatus(OrderStatus.PAID);

        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("order deve manter client correto")
    void order_deveManterClientCorreto() {
        assertThat(order.getClient().getName()).isEqualTo("Maria Brown");
        assertThat(order.getClient().getEmail()).isEqualTo("maria@gmail.com");
    }

    @Test
    @DisplayName("order deve manter moment correto")
    void order_deveManterMomentCorreto() {
        assertThat(order.getMoment()).isEqualTo(Instant.parse("2019-06-20T19:53:07Z"));
    }

    // ===== equals e hashCode =====

    @Test
    @DisplayName("dois orders com mesmo id devem ser iguais")
    void equals_doisOrdersComMesmoId_devemSerIguais() {
        Order order2 = new Order(1L, Instant.now(), OrderStatus.PAID, client);

        assertThat(order).isEqualTo(order2);
    }

    @Test
    @DisplayName("dois orders com ids diferentes não devem ser iguais")
    void equals_doisOrdersComIdsDiferentes_naoDevemSerIguais() {
        Order order2 = new Order(2L, Instant.now(), OrderStatus.PAID, client);

        assertThat(order).isNotEqualTo(order2);
    }
}
