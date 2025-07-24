package com.nksolucoes.ecommerce.web.controller;

import com.nksolucoes.ecommerce.application.service.ReportService;
import com.nksolucoes.ecommerce.web.dto.response.ProductSalesReportDTO;
import com.nksolucoes.ecommerce.web.dto.response.RevenueByDateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/top-products")
    public List<ProductSalesReportDTO> topSellingProducts() {
        return reportService.getTopSellingProducts();
    }

    @GetMapping("/revenue")
    public List<RevenueByDateDTO> revenueByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return reportService.getRevenueBetween(start, end);
    }

    @GetMapping("/orders-by-status")
    public Map<String, Long> ordersByStatus() {
        return reportService.getOrderCountByStatus();
    }

    @GetMapping("/average-order-value")
    public BigDecimal averageOrderValue() {
        return reportService.getAverageOrderValue();
    }
}
