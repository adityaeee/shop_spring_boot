package com.aditya.shop.service.impl;

import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.entity.Product;
import com.aditya.shop.repository.ProductRepository;
import com.aditya.shop.service.ProductService;
import com.aditya.shop.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<Product> getAll(SearchProductRequest request) {
        if(request.getPage() <= 0) {
            request.setPage(1);
        }

        String validSortBy;
        if("name".equalsIgnoreCase(request.getSortBy()) || "price".equalsIgnoreCase(request.getSortBy()) || "stock".equalsIgnoreCase(request.getSortBy())) {
            validSortBy = request.getSortBy();
        } else {
            validSortBy = "name";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), /**productRequest.getSortBy()*/ validSortBy);

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort); // rumus pagination


        Specification<Product> specification = ProductSpecification.getSpecification(request);
        return productRepository.findAll(specification, pageable);
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
