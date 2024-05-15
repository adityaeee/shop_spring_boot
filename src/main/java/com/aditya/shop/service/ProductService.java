package com.aditya.shop.service;

import com.aditya.shop.dto.request.NewProductRequest;
import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.dto.response.ProductResponse;
import com.aditya.shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product create(NewProductRequest request);
    Product getById(String id);
    Page<ProductResponse> getAll(SearchProductRequest request);
    Product update(Product product);
    void delete(String id);
}