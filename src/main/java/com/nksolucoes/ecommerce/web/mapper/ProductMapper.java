package com.nksolucoes.ecommerce.web.mapper;

import com.nksolucoes.ecommerce.domain.Product;
import com.nksolucoes.ecommerce.web.dto.request.ProductRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.ProductResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductRequestDTO dto);

    ProductResponseDTO toResponse(Product product);

    void updateEntity(ProductRequestDTO dto, @MappingTarget Product product);
}

