package com.nksolucoes.ecommerce.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nksolucoes.ecommerce.application.service.ProductService;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import com.nksolucoes.ecommerce.security.CurrentUserResolver;
import com.nksolucoes.ecommerce.security.JwtAuthFilter;
import com.nksolucoes.ecommerce.security.JwtService;
import com.nksolucoes.ecommerce.web.dto.request.ProductRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.ProductResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProduct() throws Exception {
        ProductRequestDTO request = new ProductRequestDTO("Produto", "Desc", BigDecimal.TEN, "Categoria", 10);
        ProductResponseDTO response = new ProductResponseDTO(UUID.randomUUID(), "Produto", "Desc", BigDecimal.TEN, "Categoria", 10, LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(service.create(any())).thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto"));
    }

    @Test
    void shouldFindAll() throws Exception {
        ProductResponseDTO product = new ProductResponseDTO(UUID.randomUUID(), "Mouse", "Desc", BigDecimal.TEN, "Periféricos", 10, LocalDateTime.now(), LocalDateTime.now());

        Mockito.when(service.findAll()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Mouse"));
    }
}
