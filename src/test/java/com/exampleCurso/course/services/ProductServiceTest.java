package com.exampleCurso.course.services;

import com.exampleCurso.course.entities.Product;
import com.exampleCurso.course.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product p1;
    private Product p2;
    private Product p3;

    @BeforeEach
    void setUp() {
        p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        p2 = new Product(2L, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
        p3 = new Product(3L, "Macbook Pro", "Nam eleifend maximus tortor, at mollis.", 1250.0, "");
    }

    // ===== findAll =====

    @Test
    @DisplayName("findAll deve retornar lista com todos os produtos")
    void findAll_deveRetornarListaDeProdutos() {
        when(repository.findAll()).thenReturn(List.of(p1, p2, p3));

        List<Product> result = service.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).contains(p1, p2, p3);
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findAll deve retornar lista vazia quando não há produtos")
    void findAll_deveRetornarListaVazia_quandoNaoHaProdutos() {
        when(repository.findAll()).thenReturn(List.of());

        List<Product> result = service.findAll();

        assertThat(result).isEmpty();
    }

    // ===== findById =====

    @Test
    @DisplayName("findById deve retornar produto quando id existir")
    void findById_deveRetornarProduto_quandoIdExistir() {
        when(repository.findById(1L)).thenReturn(Optional.of(p1));

        Product result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("The Lord of the Rings");
        assertThat(result.getPrice()).isEqualTo(90.5);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("findById deve lançar exceção quando id não existir")
    void findById_deveLancarExcecao_quandoIdNaoExistir() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(RuntimeException.class);

        verify(repository, times(1)).findById(99L);
    }

    // ===== insert =====

    @Test
    @DisplayName("insert deve salvar e retornar produto com id gerado")
    void insert_deveSalvarERetornarProduto() {
        Product novoProduto = new Product(null, "PC Gamer", "Donec aliquet odio ac rhoncus cursus.", 1200.0, "");
        Product produtoSalvo = new Product(4L, "PC Gamer", "Donec aliquet odio ac rhoncus cursus.", 1200.0, "");

        when(repository.save(novoProduto)).thenReturn(produtoSalvo);

        Product result = service.insert(novoProduto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getName()).isEqualTo("PC Gamer");
        assertThat(result.getPrice()).isEqualTo(1200.0);
        verify(repository, times(1)).save(novoProduto);
    }

    // ===== preço =====

    @Test
    @DisplayName("produto deve ter preço maior que zero")
    void produto_deveTerPrecoMaiorQueZero() {
        when(repository.findById(1L)).thenReturn(Optional.of(p1));

        Product result = service.findById(1L);

        assertThat(result.getPrice()).isGreaterThan(0.0);
    }

    @Test
    @DisplayName("produto mais caro deve ser a Smart TV")
    void produto_maisCaroDeveSerSmartTV() {
        when(repository.findAll()).thenReturn(List.of(p1, p2, p3));

        List<Product> result = service.findAll();

        Product maisCaro = result.stream()
                .max((a, b) -> Double.compare(a.getPrice(), b.getPrice()))
                .orElseThrow();

        assertThat(maisCaro.getName()).isEqualTo("Smart TV");
        assertThat(maisCaro.getPrice()).isEqualTo(2190.0);
    }
}
