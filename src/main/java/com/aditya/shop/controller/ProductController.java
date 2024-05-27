package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.constant.ResponseMessage;
import com.aditya.shop.dto.request.NewProductRequest;
import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.dto.request.UpdateProduct;
import com.aditya.shop.dto.response.CommonResponse;
import com.aditya.shop.dto.response.PagingResponse;
import com.aditya.shop.dto.response.ProductResponse;
import com.aditya.shop.entity.Product;
import com.aditya.shop.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.PRODUCT_API)
public class ProductController {
    private final ProductService productService;
    private final ObjectMapper objectMapper;

//    @PostMapping
//    public ResponseEntity<CommonResponse<Product>>  createNewProduct(@RequestBody NewProductRequest request) {
//        Product newProduct = productService.create(request);
//        CommonResponse<Product> response = CommonResponse.<Product>builder()
//                .statusCode(HttpStatus.CREATED.value())
//                .message(ResponseMessage.SUCCESS_SAVE_DATA)
//                .data(newProduct)
//                .build();
////        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

//    @PutMapping
//    public ResponseEntity<CommonResponse<?>> updateProduct(@RequestBody Product request) {
//        Product product = productService.update(request);
//
//        CommonResponse<Product> response = CommonResponse.<Product>builder()
//                .statusCode(HttpStatus.OK.value())
//                .message(ResponseMessage.SUCCESS_UPDATE_DATA)
//                .data(product)
//                .build();
//        return ResponseEntity.ok(response);
//    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> createNewProductWithFile (
            @RequestPart(name = "product") String jsonProductRequest,
            @RequestPart(name = "image") MultipartFile productImage
            ) {
        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();

        try {
            NewProductRequest productRequest = objectMapper.readValue(jsonProductRequest, new TypeReference<>() {
            });

            productRequest.setImage(productImage);
            ProductResponse newProduct = productService.create(productRequest);

            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message("successfully save data");
            responseBuilder.data(newProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.build());

        } catch (Exception e) {
            responseBuilder.message("Internal Server Error");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }

    }

    @PutMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CommonResponse<?>> updateProduct(
            @RequestPart(name = "product") String jsonProductRequest,
            @RequestPart(name = "image", required = false) MultipartFile productImage
    ) {
        CommonResponse.CommonResponseBuilder<ProductResponse> responseBuilder = CommonResponse.builder();

        try{
            UpdateProduct productRequest = objectMapper.readValue(jsonProductRequest, new TypeReference<>() {});

            productRequest.setImage(productImage);
            ProductResponse updateProduct = productService.update(productRequest);

            responseBuilder.statusCode(HttpStatus.CREATED.value());
            responseBuilder.message("successfully update data");
            responseBuilder.data(updateProduct);

            return ResponseEntity.status(HttpStatus.CREATED).body(responseBuilder.build());

        } catch (Exception e) {
            responseBuilder.message("Internal Server Errorrr");
            responseBuilder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBuilder.build());
        }
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
