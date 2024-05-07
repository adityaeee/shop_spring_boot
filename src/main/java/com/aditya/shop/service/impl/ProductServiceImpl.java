package com.aditya.shop.service.impl;

import com.aditya.shop.entity.Product;
import com.aditya.shop.repository.ProductRepository;
import com.aditya.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

//    @Autowired
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }


    @Override
    public Product create(Product product) {
//        saveAndflush, jadi ketika product di save akan mengembalikan id yang berhasil di save
        productRepository.saveAndFlush(product);
        return product;
    }

    @Override
    public Product getById(String id) {
//
//        Optional<Product> productById = productRepository.findById(id);
//        return productById.orElse();

        Optional<Product> productById = productRepository.findById(id);

        if (productById.isEmpty()) {
            throw new RuntimeException("product not found");
        } else {
            return productById.get();
        }
    }

    @Override
    public List<Product> getAll(String name, Long minPrice, Long maxPrice, Integer stock) {
        List<Product> products = new ArrayList<>();
        List<Product> result = new ArrayList<>();

        if (name != null){
            return productRepository.findAllByNameLike("%" + name + "%");
        }else if (minPrice != null) {
           return productRepository.findAllByPriceGreaterThanEqual(minPrice);
        }else if (maxPrice != null) {
            return productRepository.findAllByPriceLessThanEqual(maxPrice);
        }else if (stock != null) {
            return productRepository.findAllByStockGreaterThanEqual(stock);
        }else {
       return productRepository.findAll();
        }

    }


    @Override
    public Product update(Product product) {
        getById(product.getId());
        return productRepository.saveAndFlush(product);
    }

    @Override
    public void delete(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }
}
