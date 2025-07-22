package com.nksolucoes.ecommerce.application.service;

import com.nksolucoes.ecommerce.domain.Product;
import com.nksolucoes.ecommerce.infrastructure.repository.ProductRepository;
import com.nksolucoes.ecommerce.web.dto.request.ProductRequestDTO;
import com.nksolucoes.ecommerce.web.dto.response.ProductResponseDTO;
import com.nksolucoes.ecommerce.web.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Product product = mapper.toEntity(dto);
        return mapper.toResponse(repository.save(product));
    }

    public ProductResponseDTO update(UUID id, ProductRequestDTO dto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        mapper.updateEntity(dto, product);
        return mapper.toResponse(repository.save(product));
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        repository.deleteById(id);
    }

    public ProductResponseDTO findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public List<ProductResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }
}

