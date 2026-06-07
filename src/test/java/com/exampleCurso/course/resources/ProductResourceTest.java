package com.exampleCurso.course.resources;

import com.exampleCurso.course.entities.Product;
import com.exampleCurso.course.services.ProductService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
class ProductResourceTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        // ... resto do setUp que já existe
    }

    @MockitoBean
    private ProductService service;

    private Product p1;
    private Product p2;
    private Product p3;

    @BeforeEach
    void setUpDatabase() {
        p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        p2 = new Product(2L, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
        p3 = new Product(3L, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "");
    }

    // ===== GET /products =====

    @Test
    @DisplayName("GET /products deve retornar 200 com lista de produtos")
    void findAll_deveRetornar200_comListaDeProdutos() throws Exception {
        when(service.findAll()).thenReturn(List.of(p1, p2, p3));

        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("The Lord of the Rings"))
                .andExpect(jsonPath("$[0].price").value(90.5))
                .andExpect(jsonPath("$[1].name").value("Smart TV"))
                .andExpect(jsonPath("$[2].name").value("Macbook Pro"));
    }

    @Test
    @DisplayName("GET /products deve retornar 200 com lista vazia")
    void findAll_deveRetornar200_comListaVazia() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ===== GET /products/{id} =====

    @Test
    @DisplayName("GET /products/{id} deve retornar 200 com produto quando id existir")
    void findById_deveRetornar200_quandoIdExistir() throws Exception {
        when(service.findById(1L)).thenReturn(p1);

        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("The Lord of the Rings"))
                .andExpect(jsonPath("$.price").value(90.5))
                .andExpect(jsonPath("$.description").value("Lorem ipsum dolor sit amet, consectetur."));
    }

    @Test
    @DisplayName("GET /products/{id} deve retornar 200 para Smart TV")
    void findById_deveRetornar200_paraSmartTV() throws Exception {
        when(service.findById(2L)).thenReturn(p2);

        mockMvc.perform(get("/products/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Smart TV"))
                .andExpect(jsonPath("$.price").value(2190.0));
    }

    @Test
    @DisplayName("GET /products/{id} deve retornar 200 para Macbook Pro")
    void findById_deveRetornar200_paraMacbookPro() throws Exception {
        when(service.findById(3L)).thenReturn(p3);

        mockMvc.perform(get("/products/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Macbook Pro"))
                .andExpect(jsonPath("$.price").value(1250.0));
    }

    // ===== POST /products =====

    @Test
    @DisplayName("POST /products deve retornar 201 com produto criado")
    void insert_deveRetornar201_comProdutoCriado() throws Exception {
        Product produtoSalvo = new Product(4L, "PC Gamer", "Donec aliquet odio ac rhoncus cursus.", 1200.0, "");

        when(service.insert(any(Product.class))).thenReturn(produtoSalvo);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PC Gamer\",\"description\":\"Donec aliquet odio ac rhoncus cursus.\",\"price\":1200.0,\"imgUrl\":\"\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.name").value("PC Gamer"))
                .andExpect(jsonPath("$.price").value(1200.0))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("POST /products deve retornar 201 com Rails for Dummies criado")
    void insert_deveRetornar201_comRailsForDummies() throws Exception {
        Product produtoSalvo = new Product(5L, "Rails for Dummies", "Cras fringilla convallis sem vel faucibus.", 100.99, "");

        when(service.insert(any(Product.class))).thenReturn(produtoSalvo);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Rails for Dummies\",\"description\":\"Cras fringilla convallis sem vel faucibus.\",\"price\":100.99,\"imgUrl\":\"\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Rails for Dummies"))
                .andExpect(jsonPath("$.price").value(100.99))
                .andExpect(header().exists("Location"));
    }
}