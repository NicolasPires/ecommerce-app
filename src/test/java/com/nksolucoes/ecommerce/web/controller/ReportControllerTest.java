package com.nksolucoes.ecommerce.web.controller;

import com.nksolucoes.ecommerce.application.service.ReportService;
import com.nksolucoes.ecommerce.infrastructure.repository.UserRepository;
import com.nksolucoes.ecommerce.security.JwtAuthFilter;
import com.nksolucoes.ecommerce.web.dto.response.ProductSalesReportDTO;
import com.nksolucoes.ecommerce.web.dto.response.RevenueByDateDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService service;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldReturnTopSellingProducts() throws Exception {
        Mockito.when(service.getTopSellingProducts())
                .thenReturn(List.of(new ProductSalesReportDTO("Produto X", 10L, BigDecimal.ONE)));

        mockMvc.perform(get("/api/reports/top-products"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnRevenueByDate() throws Exception {
        LocalDate start = LocalDate.now().minusDays(5);
        LocalDate end = LocalDate.now();

        Mockito.when(service.getRevenueBetween(start, end))
                .thenReturn(List.of(new RevenueByDateDTO(start, BigDecimal.TEN)));

        mockMvc.perform(get("/api/reports/revenue")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnOrderCountByStatus() throws Exception {
        Mockito.when(service.getOrderCountByStatus()).thenReturn(Map.of("PAID", 5L));

        mockMvc.perform(get("/api/reports/orders-by-status"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnAverageOrderValue() throws Exception {
        Mockito.when(service.getAverageOrderValue()).thenReturn(BigDecimal.valueOf(150));

        mockMvc.perform(get("/api/reports/average-order-value"))
                .andExpect(status().isOk());
    }
}
