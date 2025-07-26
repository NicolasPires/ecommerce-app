package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.domain.Product;
import com.nksolucoes.ecommerce.infrastructure.repository.ProductRepository;
import com.nksolucoes.ecommerce.web.dto.request.ProductRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.ProductResponseDTO;
import com.nksolucoes.ecommerce.web.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository repository;
    private ProductMapper mapper;
    private ProductService service;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        mapper = mock(ProductMapper.class);
        service = new ProductService(repository, mapper);
    }

    @Test
    void shouldCreateProduct() {
        ProductRequestDTO requestDTO = new ProductRequestDTO(
                "Notebook", "Notebook Gamer", new BigDecimal("4999.90"), "Informática", 10);

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Notebook")
                .description("Notebook Gamer")
                .price(new BigDecimal("4999.90"))
                .category("Informática")
                .stockQuantity(10)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductResponseDTO responseDTO = new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

        when(mapper.toEntity(requestDTO)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);
        when(mapper.toResponse(product)).thenReturn(responseDTO);

        ProductResponseDTO result = service.create(requestDTO);

        assertThat(result).isEqualTo(responseDTO);
        verify(repository).save(product);
    }

    @Test
    void shouldFindProductById() {
        UUID id = UUID.randomUUID();

        Product product = Product.builder()
                .id(id)
                .name("Monitor")
                .description("Monitor 24''")
                .price(new BigDecimal("899.90"))
                .category("Informática")
                .stockQuantity(5)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductResponseDTO responseDTO = new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

        when(repository.findById(id)).thenReturn(Optional.of(product));
        when(mapper.toResponse(product)).thenReturn(responseDTO);

        ProductResponseDTO result = service.findById(id);

        assertThat(result).isEqualTo(responseDTO);
    }

    @Test
    void shouldListAllProducts() {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Mouse")
                .description("Mouse sem fio")
                .price(new BigDecimal("149.90"))
                .category("Periféricos")
                .stockQuantity(20)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ProductResponseDTO responseDTO = new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );

        when(repository.findAll()).thenReturn(List.of(product));
        when(mapper.toResponse(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result = service.findAll();

        assertThat(result).containsExactly(responseDTO);
    }

    @Test
    void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();
        ProductRequestDTO dto = new ProductRequestDTO("New Name", "Updated Desc", new BigDecimal("299.90"), "Updated Cat", 20);

        Product product = Product.builder().id(id).build();
        Product updated = Product.builder()
                .id(id)
                .name("New Name")
                .description("Updated Desc")
                .price(new BigDecimal("299.90"))
                .category("Updated Cat")
                .stockQuantity(20)
                .build();

        ProductResponseDTO responseDTO = new ProductResponseDTO(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getPrice(),
                updated.getCategory(),
                updated.getStockQuantity(),
                updated.getCreatedAt(),
                updated.getUpdatedAt()
        );

        when(repository.findById(id)).thenReturn(Optional.of(product));
        doAnswer(inv -> {
            // Simula a atualização do produto
            ProductRequestDTO inputDto = inv.getArgument(0);
            Product target = inv.getArgument(1);
            target.setName(inputDto.name());
            target.setDescription(inputDto.description());
            target.setPrice(inputDto.price());
            target.setCategory(inputDto.category());
            target.setStockQuantity(inputDto.stockQuantity());
            return null;
        }).when(mapper).updateEntity(eq(dto), eq(product));
        when(repository.save(product)).thenReturn(updated);
        when(mapper.toResponse(updated)).thenReturn(responseDTO);

        ProductResponseDTO result = service.update(id, dto);

        assertThat(result.name()).isEqualTo("New Name");
        verify(repository).save(product);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentProduct() {
        UUID id = UUID.randomUUID();
        ProductRequestDTO dto = new ProductRequestDTO("X", "Y", BigDecimal.ONE, "Z", 1);

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(repository, never()).save(any());
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentProduct() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(repository, never()).deleteById(any());
    }
}
