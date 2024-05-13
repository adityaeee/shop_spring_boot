package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.entity.Product;
import com.aditya.shop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product>  createNewProduct(@RequestBody Product product) {
        Product newProduct = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProduct(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(name = "direction", defaultValue = "asc") String direction
    ) {
        SearchProductRequest request = SearchProductRequest.builder()
                .name(name)
                .direction(direction)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();

        Page<Product> products = productService.getAll(request);
        return ResponseEntity.ok(products);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<Product> getByid(@PathVariable String id) {
        Product product = productService.getById(id);
        return ResponseEntity.ok(product);
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
