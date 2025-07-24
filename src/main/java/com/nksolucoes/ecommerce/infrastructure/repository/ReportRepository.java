package com.nksolucoes.ecommerce.infrastructure.repository;

import com.nksolucoes.ecommerce.web.dto.response.ProductSalesReportDTO;
import com.nksolucoes.ecommerce.web.dto.response.RevenueByDateDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository extends Repository<com.nksolucoes.ecommerce.domain.OrderItem, Long> {

    @Query("SELECT new com.nksolucoes.ecommerce.web.dto.response.ProductSalesReportDTO(" +
            "oi.product.name, SUM(oi.quantity), SUM(oi.product.price * oi.quantity)) " +
            "FROM OrderItem oi GROUP BY oi.product.name ORDER BY SUM(oi.quantity) DESC")
    List<ProductSalesReportDTO> findTopSellingProducts();

    @Query("SELECT new com.nksolucoes.ecommerce.web.dto.response.RevenueByDateDTO(" +
            "DATE(o.createdAt), SUM(o.total)) " +
            "FROM Order o WHERE o.createdAt BETWEEN :start AND :end " +
            "GROUP BY DATE(o.createdAt) ORDER BY DATE(o.createdAt)")
    List<RevenueByDateDTO> findRevenueBetweenDates(@Param("start") LocalDate start,
                                                   @Param("end") LocalDate end);

    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    @Query("SELECT AVG(o.total) FROM Order o")
    java.math.BigDecimal findAverageOrderValue();
}
