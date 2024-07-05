package com.aditya.shop.controller;

import com.aditya.shop.constant.APIUrl;
import com.aditya.shop.entity.Image;
import com.aditya.shop.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class testUpload {
    private final ImageService imageService;

    @PostMapping(path = "/api/v1/test-upload")
    public ResponseEntity<?> testUploadImage(
            @RequestPart(name = "image") MultipartFile multipartFile
    ){
        Image image = imageService.create(multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    @GetMapping(path = "/api/v1/products/image/{imageId}")
    public ResponseEntity<?> downloadImage(
            @PathVariable(name = "imageId") String id
    ) {
        Resource image = imageService.getById(id);

//        "attachment; filename="filename.jpg"
        String headerValue = String.format("attachment; filename=%s",image.getFilename());

//        Content-Disposition;"attachment; filename="filename.jpg"
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(image);
    }

    @DeleteMapping(path = "/api/v1/products/image"+ APIUrl.PATH_VAR_ID)
    public ResponseEntity<?> deleteImage(@PathVariable String id) {
        imageService.deleteById(id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(null);
    }

}
