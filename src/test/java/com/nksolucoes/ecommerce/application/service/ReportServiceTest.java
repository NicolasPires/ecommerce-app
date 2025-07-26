package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.infrastructure.repository.ReportRepository;
import com.nksolucoes.ecommerce.web.dto.response.ProductSalesReportDTO;
import com.nksolucoes.ecommerce.web.dto.response.RevenueByDateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ReportServiceTest {

    private ReportRepository repository;
    private ReportService service;

    @BeforeEach
    void setUp() {
        repository = mock(ReportRepository.class);
        service = new ReportService(repository);
    }

    @Test
    void shouldReturnTopSellingProducts() {
        List<ProductSalesReportDTO> mockList = List.of(
                new ProductSalesReportDTO("Notebook", 150L, BigDecimal.ONE),
                new ProductSalesReportDTO("Mouse", 100L, BigDecimal.ONE)
        );

        when(repository.findTopSellingProducts()).thenReturn(mockList);

        List<ProductSalesReportDTO> result = service.getTopSellingProducts();

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().getName()).isEqualTo("Notebook");
        verify(repository).findTopSellingProducts();
    }

    @Test
    void shouldReturnRevenueBetweenDates() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        List<RevenueByDateDTO> mockRevenues = List.of(
                new RevenueByDateDTO(LocalDate.of(2024, 1, 15), new BigDecimal("1000.00")),
                new RevenueByDateDTO(LocalDate.of(2024, 2, 15), new BigDecimal("2500.00"))
        );

        when(repository.findRevenueBetweenDates(start, end)).thenReturn(mockRevenues);

        List<RevenueByDateDTO> result = service.getRevenueBetween(start, end);

        assertThat(result).hasSize(2);
        assertThat(result.get(1).getTotalRevenue()).isEqualTo(new BigDecimal("2500.00"));
        verify(repository).findRevenueBetweenDates(start, end);
    }

    @Test
    void shouldReturnOrderCountByStatus() {
        List<Object[]> mockData = List.of(
                new Object[]{"PENDING", 5L},
                new Object[]{"PAID", 10L},
                new Object[]{"CANCELLED", 2L}
        );

        when(repository.countOrdersByStatus()).thenReturn(mockData);

        Map<String, Long> result = service.getOrderCountByStatus();

        assertThat(result)
                .containsEntry("PENDING", 5L)
                .containsEntry("PAID", 10L)
                .containsEntry("CANCELLED", 2L);
        verify(repository).countOrdersByStatus();
    }

    @Test
    void shouldReturnAverageOrderValue() {
        BigDecimal average = new BigDecimal("347.89");
        when(repository.findAverageOrderValue()).thenReturn(average);

        BigDecimal result = service.getAverageOrderValue();

        assertThat(result).isEqualTo(average);
        verify(repository).findAverageOrderValue();
    }
}
