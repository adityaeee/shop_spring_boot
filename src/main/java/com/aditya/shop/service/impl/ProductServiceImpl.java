package com.aditya.shop.service.impl;

import com.aditya.shop.constant.ResponseMessage;
import com.aditya.shop.dto.request.NewProductRequest;
import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.dto.response.ProductResponse;
import com.aditya.shop.entity.Product;
import com.aditya.shop.repository.ProductRepository;
import com.aditya.shop.service.ProductService;
import com.aditya.shop.specification.ProductSpecification;
import com.aditya.shop.utils.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;

//    @Autowired
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }


    @Override
    public Product create(NewProductRequest request) {
        validationUtil.validate(request);

        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        return productRepository.saveAndFlush(newProduct);
    }

    @Override
    public Product getById(String id) {
//
//        Optional<Product> productById = productRepository.findById(id);
//        return productById.orElse();

        Optional<Product> productById = productRepository.findById(id);

        if (productById.isEmpty()) {
//            throw new RuntimeException("product not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND);
        } else {
            return productById.get();
        }
    }

    @Override
    public Page<ProductResponse> getAll(SearchProductRequest request) {
        if(request.getPage() <= 0) {
            request.setPage(1);
        }

        String validSortBy;
        if("name".equalsIgnoreCase(request.getSortBy()) || "price".equalsIgnoreCase(request.getSortBy()) || "stock".equalsIgnoreCase(request.getSortBy())) {
            validSortBy = request.getSortBy();
        } else {
            validSortBy = "name";
        }

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), /**request.getSortBy()*/ validSortBy);

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(),sort); // rumus pagination


        Specification<Product> specification = ProductSpecification.getSpecification(request);

        Page<Product> products = productRepository.findAll(specification, pageable);
        
        return products.map(product -> {
           return ProductResponse.builder()
                   .name(product.getName())
                   .price(product.getPrice())
                   .build();
        });
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
