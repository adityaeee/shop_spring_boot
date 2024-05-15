package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.constant.ResponseMessage;
import com.aditya.shop.dto.request.NewProductRequest;
import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.dto.response.CommonResponse;
import com.aditya.shop.dto.response.PagingResponse;
import com.aditya.shop.dto.response.ProductResponse;
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
    public ResponseEntity<CommonResponse<Product>>  createNewProduct(@RequestBody NewProductRequest request) {
        Product newProduct = productService.create(request);
        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message(ResponseMessage.SUCCESS_SAVE_DATA)
                .data(newProduct)
                .build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductResponse>>> getAllProduct(
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

        Page<ProductResponse> products = productService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(products.getTotalPages())
                .totalElements(products.getTotalElements())
                .page(products.getPageable().getPageNumber())
                .size(products.getPageable().getPageSize())
                .hasNext(products.hasNext())
                .hasPrevious(products.hasPrevious())
                .build();

        CommonResponse<List<ProductResponse>> response = CommonResponse.<List<ProductResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(products.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<Product>> getByid(@PathVariable String id) {
        Product product = productService.getById(id);

        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_GET_DATA)
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<CommonResponse<Product>> updateProduct(@RequestBody Product request) {
        Product product = productService.update(request);

        CommonResponse<Product> response = CommonResponse.<Product>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
                .data(product)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = APIUrl.PATH_VAR_ID)
    public ResponseEntity<CommonResponse<String>> deleteById (@PathVariable String id) {
        productService.delete(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message(ResponseMessage.SUCCESS_DELETE_DATA)
                .build();

        return ResponseEntity.ok(response);
    }

}
