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
            result = productRepository.findAllByNameLike("%" + name + "%");
        }else {
            result = productRepository.findAll();
        }

        if (minPrice != null) {
            products = productRepository.findAllByPriceGreaterThanEqual(minPrice);
//            System.out.println(products);
//            for(Product product : products) {
//                for (Product res : result) {
//                    if(!res.equals(product)) {
//                        result.remove(res);
//                    }
//                }
//            }
            for(Product product : result) {
             if (!products.contains(product)){
                 result.remove(product);
             }
            }
        }

//        if (maxPrice != null) {
//            result =  productRepository.findAllByPriceLessThanEqual(maxPrice);
//            result.forEach(product -> products.add(product));
//        }
//
//        if (stock != null) {
//            result.clear();
//            result =  productRepository.findAllByStockGreaterThanEqual(stock);
//            result.forEach(product -> products.add(product));
//        }

       return result;
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
