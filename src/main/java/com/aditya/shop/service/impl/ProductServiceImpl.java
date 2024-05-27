package com.aditya.shop.service.impl;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.constant.ResponseMessage;
import com.aditya.shop.dto.request.NewProductRequest;
import com.aditya.shop.dto.request.SearchProductRequest;
import com.aditya.shop.dto.request.UpdateProduct;
import com.aditya.shop.dto.response.ImageResponse;
import com.aditya.shop.dto.response.ProductResponse;
import com.aditya.shop.entity.Customer;
import com.aditya.shop.entity.Image;
import com.aditya.shop.entity.Product;
import com.aditya.shop.repository.ProductRepository;
import com.aditya.shop.service.ImageService;
import com.aditya.shop.service.ProductService;
import com.aditya.shop.specification.ProductSpecification;
import com.aditya.shop.utils.ValidationUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
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
    private final ImageService imageService;

//    @Autowired
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse create(NewProductRequest request) {
        validationUtil.validate(request);

        if(request.getImage().isEmpty()) {
            throw new ConstraintViolationException("image is required", null);
        }

        Image image = imageService.create(request.getImage());

        Product newProduct = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .image(image)
                .build();

      productRepository.saveAndFlush(newProduct);
      return parseProductToProductResponse(newProduct);
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


    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse update(UpdateProduct request) {
       validationUtil.validate(request);

       Product currentProduct = findByIdOrThrowNotFound(request.getId());

       Image imageRemove = currentProduct.getImage();

        currentProduct = Product.builder()
                .id(request.getId())
                .name(request.getName())
                .price(request.getPrice())
                .stock(request.getStock())
                .image(currentProduct.getImage())
                .build();

        if (request.getImage() != null) {
            Image newImage = imageService.create(request.getImage());
            currentProduct = Product.builder()
                    .id(request.getId())
                    .name(request.getName())
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .image(newImage)
                    .build();

            productRepository.saveAndFlush(currentProduct);
            imageService.deleteById(imageRemove.getId());
            return parseProductToProductResponse(currentProduct);
        }

        productRepository.saveAndFlush(currentProduct);
        return parseProductToProductResponse(currentProduct);

    }

    @Override
    public void delete(String id) {
        Product currentProduct = getById(id);
        productRepository.delete(currentProduct);
    }

    private ProductResponse parseProductToProductResponse (Product product) {
        String imageId;
        String name;
        if(product == null) {
            imageId = null;
            name = null;
        } else  {
            imageId = product.getId();
            name = product.getName();
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageResponse(ImageResponse.builder()
                        .url(APIUrl.PRODUCT_IMAGE_DOWNLOAD_API + imageId)
                        .name(name)
                        .build())
                .build();
    }

    private Product findByIdOrThrowNotFound(String id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
