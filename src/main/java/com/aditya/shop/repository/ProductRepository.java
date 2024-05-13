package com.aditya.shop.repository;

import com.aditya.shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    List<Product> findAllByNameLike(String name);
    List<Product> findAllByPriceLessThanEqual(Long maxPrice);
    List<Product> findAllByPriceGreaterThanEqual(Long minPrice);
    List<Product> findAllByStockGreaterThanEqual(Integer stock);
}
