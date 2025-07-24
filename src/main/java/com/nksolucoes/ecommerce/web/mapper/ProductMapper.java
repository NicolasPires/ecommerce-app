package com.nksolucoes.ecommerce.web.mapper;

import com.nksolucoes.ecommerce.domain.Product;
import com.nksolucoes.ecommerce.web.dto.request.ProductRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ProductResponseDTO toResponse(Product product);

    void updateEntity(ProductRequestDTO dto, @MappingTarget Product product);
}
