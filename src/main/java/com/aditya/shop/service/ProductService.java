package com.aditya.shop.service;

import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product create(Product product);
    Product getById(String id);
    Page<Product> getAll(SearchProductRequest request);
    Product update(Product product);
    void delete(String id);
}
