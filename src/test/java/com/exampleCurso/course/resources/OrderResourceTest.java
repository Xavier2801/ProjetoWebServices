package com.exampleCurso.course.resources;

import com.exampleCurso.course.entities.Order;
import com.exampleCurso.course.entities.User;
import com.exampleCurso.course.entities.enums.OrderStatus;
import com.exampleCurso.course.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderResourceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // ... resto do setUp que já existe
    }

    @MockitoBean
    private OrderService service;

    private User client;
    private Order o1;
    private Order o2;

    @BeforeEach
    void setUpDatabase() {
        client = new User(1L, "Maria Brown", "maria@gmail.com", "988888888", "123456");
        o1 = new Order(1L, Instant.parse("2019-06-20T19:53:07Z"), OrderStatus.WAITING_PAYMENT, client);
        o2 = new Order(2L, Instant.parse("2019-07-21T03:42:20Z"), OrderStatus.PAID, client);
    }

    // ===== GET /orders =====

    @Test
    @DisplayName("GET /orders deve retornar 200 com lista de pedidos")
    void findAll_deveRetornar200_comListaDePedidos() throws Exception {
        when(service.findAll()).thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].orderStatus").value("WAITING_PAYMENT"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].orderStatus").value("PAID"));
    }

    @Test
    @DisplayName("GET /orders deve retornar 200 com lista vazia")
    void findAll_deveRetornar200_comListaVazia() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ===== GET /orders/{id} =====

    @Test
    @DisplayName("GET /orders/{id} deve retornar 200 com pedido quando id existir")
    void findById_deveRetornar200_quandoIdExistir() throws Exception {
        when(service.findById(1L)).thenReturn(o1);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderStatus").value("WAITING_PAYMENT"))
                .andExpect(jsonPath("$.client.name").value("Maria Brown"));
    }

    @Test
    @DisplayName("GET /orders/{id} deve retornar 200 com pedido PAID")
    void findById_deveRetornar200_comPedidoPago() throws Exception {
        when(service.findById(2L)).thenReturn(o2);

        mockMvc.perform(get("/orders/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.orderStatus").value("PAID"));
    }

    // ===== POST /orders =====

    @Test
    @DisplayName("POST /orders deve retornar 201 com pedido criado")
    void insert_deveRetornar201_comPedidoCriado() throws Exception {
        Order pedidoSalvo = new Order(3L, Instant.parse("2019-07-22T15:21:22Z"), OrderStatus.WAITING_PAYMENT, client);

        when(service.insert(any(Order.class))).thenReturn(pedidoSalvo);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moment\":\"2019-07-22T15:21:22Z\",\"orderStatus\":\"WAITING_PAYMENT\",\"client\":{\"id\":1}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.orderStatus").value("WAITING_PAYMENT"))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("POST /orders deve retornar 201 com pedido PAID")
    void insert_deveRetornar201_comPedidoPago() throws Exception {
        Order pedidoSalvo = new Order(4L, Instant.parse("2019-07-23T10:00:00Z"), OrderStatus.PAID, client);

        when(service.insert(any(Order.class))).thenReturn(pedidoSalvo);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moment\":\"2019-07-23T10:00:00Z\",\"orderStatus\":\"PAID\",\"client\":{\"id\":1}}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.orderStatus").value("PAID"))
                .andExpect(header().exists("Location"));
    }
}
