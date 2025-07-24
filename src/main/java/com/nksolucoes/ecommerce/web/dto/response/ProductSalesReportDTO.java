package com.nksolucoes.ecommerce.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductSalesReportDTO {
    private String name;
    private Long totalQuantity;
    private BigDecimal totalRevenue;
}
