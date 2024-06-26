package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.entity.Product;
import com.aditya.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public Product  createNewProduct(@RequestBody Product product) {
        return productService.create(product);
    }

    @GetMapping
    public List<Product> getAllProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "minPrice", required = false) Long minPrice,
            @RequestParam(name = "maxPrice", required = false) Long maxPrice,
            @RequestParam(name = "stock", required = false) Integer stock
    ) {
        return productService.getAll(name, minPrice, maxPrice, stock);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public Product getByid(@PathVariable String id) {
        return productService.getById(id);
    }

    @PutMapping
    public Product updateProduct(@RequestBody Product product) {
        return productService.update(product);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public String deleteById (@PathVariable String id) {
        productService.delete(id);
        return "Ok, success delete product with id "+ id;
    }

}
