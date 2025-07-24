package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.infrastructure.repository.ReportRepository;
import com.nksolucoes.ecommerce.web.dto.response.ProductSalesReportDTO;
import com.nksolucoes.ecommerce.web.dto.response.RevenueByDateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;

    public List<ProductSalesReportDTO> getTopSellingProducts() {
        return repository.findTopSellingProducts();
    }

    public List<RevenueByDateDTO> getRevenueBetween(LocalDate start, LocalDate end) {
        return repository.findRevenueBetweenDates(start, end);
    }

    public Map<String, Long> getOrderCountByStatus() {
        return repository.countOrdersByStatus().stream()
                .collect(Collectors.toMap(
                        obj -> obj[0].toString(),
                        obj -> (Long) obj[1]
                ));
    }

    public BigDecimal getAverageOrderValue() {
        return repository.findAverageOrderValue();
    }
}
