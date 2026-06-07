package com.exampleCurso.course.entities;

import com.exampleCurso.course.entities.entities_pk.OrderItemPK;
import com.exampleCurso.course.entities.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

class EntitiesTest {

    private User client;
    private Order order;
    private Product p1;
    private Product p2;
    private Category c1;
    private Category c2;
    private Payment payment;

    @BeforeEach
    void setUp() {
        client = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        order = new Order(1L, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.WAITING_PAYMENT, client);
        p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum.", 90.5, "");
        p2 = new Product(2L, "Smart TV", "Nulla eu imperdiet.", 2190.0, "");
        c1 = new Category(1L, "Books");
        c2 = new Category(2L, "Electronics");
        payment = new Payment(1L, Instant.parse("2019-06-20T21:53:07Z"), order);
    }

    // ===== Product =====

    @Test
    @DisplayName("Product deve retornar atributos corretamente")
    void product_deveRetornarAtributosCorretamente() {
        assertThat(p1.getId()).isEqualTo(1L);
        assertThat(p1.getName()).isEqualTo("The Lord of the Rings");
        assertThat(p1.getDescription()).isEqualTo("Lorem ipsum.");
        assertThat(p1.getPrice()).isEqualTo(90.5);
        assertThat(p1.getImgUrl()).isEqualTo("");
    }

    @Test
    @DisplayName("Product setters devem atualizar os valores corretamente")
    void product_settersDevemAtualizarValores() {
        p1.setName("New Name");
        p1.setDescription("New Desc");
        p1.setPrice(150.0);
        p1.setImgUrl("http://img.com");

        assertThat(p1.getName()).isEqualTo("New Name");
        assertThat(p1.getDescription()).isEqualTo("New Desc");
        assertThat(p1.getPrice()).isEqualTo(150.0);
        assertThat(p1.getImgUrl()).isEqualTo("http://img.com");
    }

    @Test
    @DisplayName("Product deve retornar categorias vazias ao ser criado")
    void product_deveRetornarCategoriasVazias() {
        assertThat(p1.getCategories()).isEmpty();
    }

    @Test
    @DisplayName("Product deve retornar pedidos via getOrders")
    void product_deveRetornarPedidosVazios() {
        assertThat(p1.getOrders()).isEmpty();
    }

    @Test
    @DisplayName("Dois products com mesmo id devem ser iguais")
    void product_equals_mesmoid() {
        Product p3 = new Product(1L, "Outro", "Outro", 10.0, "");
        assertThat(p1).isEqualTo(p3);
        assertThat(p1.hashCode()).isEqualTo(p3.hashCode());
    }

    @Test
    @DisplayName("Dois products com ids diferentes não devem ser iguais")
    void product_equals_idsDiferentes() {
        assertThat(p1).isNotEqualTo(p2);
    }

    @Test
    @DisplayName("Product construtor vazio deve funcionar")
    void product_construtorVazio() {
        Product p = new Product();
        assertThat(p).isNotNull();
    }

    // ===== Category =====

    @Test
    @DisplayName("Category deve retornar atributos corretamente")
    void category_deveRetornarAtributosCorretamente() {
        assertThat(c1.getId()).isEqualTo(1L);
        assertThat(c1.getName()).isEqualTo("Books");
    }

    @Test
    @DisplayName("Category setters devem atualizar os valores")
    void category_settersDevemAtualizarValores() {
        c1.setId(10L);
        c1.setName("Updated");

        assertThat(c1.getId()).isEqualTo(10L);
        assertThat(c1.getName()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("Category deve retornar produtos vazios ao ser criada")
    void category_deveRetornarProdutosVazios() {
        assertThat(c1.getProducts()).isEmpty();
    }

    @Test
    @DisplayName("Duas categories com mesmo id devem ser iguais")
    void category_equals_mesmoId() {
        Category c3 = new Category(1L, "Outro");
        assertThat(c1).isEqualTo(c3);
        assertThat(c1.hashCode()).isEqualTo(c3.hashCode());
    }

    @Test
    @DisplayName("Duas categories com ids diferentes não devem ser iguais")
    void category_equals_idsDiferentes() {
        assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    @DisplayName("Category construtor vazio deve funcionar")
    void category_construtorVazio() {
        Category c = new Category();
        assertThat(c).isNotNull();
    }

    // ===== Payment =====

    @Test
    @DisplayName("Payment deve retornar atributos corretamente")
    void payment_deveRetornarAtributosCorretamente() {
        assertThat(payment.getId()).isEqualTo(1L);
        assertThat(payment.getMoment()).isEqualTo(Instant.parse("2019-06-20T21:53:07Z"));
        assertThat(payment.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("Payment setters devem atualizar os valores")
    void payment_settersDevemAtualizarValores() {
        Instant novoMomento = Instant.parse("2020-01-01T00:00:00Z");
        payment.setId(2L);
        payment.setMoment(novoMomento);
        payment.setOrder(null);

        assertThat(payment.getId()).isEqualTo(2L);
        assertThat(payment.getMoment()).isEqualTo(novoMomento);
        assertThat(payment.getOrder()).isNull();
    }

    @Test
    @DisplayName("Dois payments com mesmo id devem ser iguais")
    void payment_equals_mesmoId() {
        Payment p2 = new Payment(1L, Instant.now(), order);
        assertThat(payment).isEqualTo(p2);
        assertThat(payment.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    @DisplayName("Dois payments com ids diferentes não devem ser iguais")
    void payment_equals_idsDiferentes() {
        Payment p3 = new Payment(2L, Instant.now(), order);
        assertThat(payment).isNotEqualTo(p3);
    }

    @Test
    @DisplayName("Payment construtor vazio deve funcionar")
    void payment_construtorVazio() {
        Payment p = new Payment();
        assertThat(p).isNotNull();
    }

    // ===== OrderItemPK =====

    @Test
    @DisplayName("OrderItemPK deve retornar order e product corretamente")
    void orderItemPK_deveRetornarOrderEProduct() {
        OrderItemPK pk = new OrderItemPK();
        pk.setOrder(order);
        pk.setProduct(p1);

        assertThat(pk.getOrder()).isEqualTo(order);
        assertThat(pk.getProduct()).isEqualTo(p1);
    }

    @Test
    @DisplayName("Dois OrderItemPKs com mesmos valores devem ser iguais")
    void orderItemPK_equals_mesmosValores() {
        OrderItemPK pk1 = new OrderItemPK();
        pk1.setOrder(order);
        pk1.setProduct(p1);

        OrderItemPK pk2 = new OrderItemPK();
        pk2.setOrder(order);
        pk2.setProduct(p1);

        assertThat(pk1).isEqualTo(pk2);
        assertThat(pk1.hashCode()).isEqualTo(pk2.hashCode());
    }

    @Test
    @DisplayName("Dois OrderItemPKs com produtos diferentes não devem ser iguais")
    void orderItemPK_equals_produtosDiferentes() {
        OrderItemPK pk1 = new OrderItemPK();
        pk1.setOrder(order);
        pk1.setProduct(p1);

        OrderItemPK pk2 = new OrderItemPK();
        pk2.setOrder(order);
        pk2.setProduct(p2);

        assertThat(pk1).isNotEqualTo(pk2);
    }

    @Test
    @DisplayName("OrderItemPK com valores nulos não deve ser igual a outro")
    void orderItemPK_equals_valoresNulos() {
        OrderItemPK pk1 = new OrderItemPK();
        OrderItemPK pk2 = new OrderItemPK();

        assertThat(pk1).isEqualTo(pk2);
    }

    // ===== StandardError / exceptions =====

    @Test
    @DisplayName("ResourceNotFoundException deve conter mensagem com id")
    void resourceNotFoundException_deveTerMensagemComId() {
        var ex = new com.exampleCurso.course.services.exception.ResourceNotFoundException(99L);
        assertThat(ex.getMessage()).contains("99");
    }

    @Test
    @DisplayName("DatabaseException deve conter mensagem")
    void databaseException_deveTerMensagem() {
        var ex = new com.exampleCurso.course.services.exception.DatabaseException("Integrity violation");
        assertThat(ex.getMessage()).isEqualTo("Integrity violation");
    }

    @Test
    @DisplayName("DatabaseException construtor vazio deve funcionar")
    void databaseException_construtorVazio() {
        var ex = new com.exampleCurso.course.services.exception.DatabaseException();
        assertThat(ex).isNotNull();
    }

    // ===== StandardError =====

    @Test
    @DisplayName("StandardError deve retornar atributos corretamente")
    void standardError_deveRetornarAtributosCorretamente() {
        Instant now = Instant.now();
        var err = new com.exampleCurso.course.resources.exceptions.StandardError(now, 404, "Not Found", "Resource not found", "/users/99");

        assertThat(err.getTimestamp()).isEqualTo(now);
        assertThat(err.getStatus()).isEqualTo(404);
        assertThat(err.getError()).isEqualTo("Not Found");
        assertThat(err.getMessage()).isEqualTo("Resource not found");
        assertThat(err.getPath()).isEqualTo("/users/99");
    }

    @Test
    @DisplayName("StandardError setters devem atualizar os valores")
    void standardError_settersDevemAtualizarValores() {
        var err = new com.exampleCurso.course.resources.exceptions.StandardError();
        Instant now = Instant.now();
        err.setTimestamp(now);
        err.setStatus(400);
        err.setError("Bad Request");
        err.setMessage("Database error");
        err.setPath("/users/1");

        assertThat(err.getTimestamp()).isEqualTo(now);
        assertThat(err.getStatus()).isEqualTo(400);
        assertThat(err.getError()).isEqualTo("Bad Request");
        assertThat(err.getMessage()).isEqualTo("Database error");
        assertThat(err.getPath()).isEqualTo("/users/1");
    }
}