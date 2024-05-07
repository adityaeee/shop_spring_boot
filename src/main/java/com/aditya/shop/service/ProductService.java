package com.aditya.shop.service;

import com.aditya.shop.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    Product create(Product product);
    Product getById(String id);
    List<Product> getAll(String name, Long minPrice, Long maxPrice, Integer stock);
    Product update(Product product);
    void delete(String id);
}
