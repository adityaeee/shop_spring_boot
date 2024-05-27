package com.aditya.shop.service.impl;

import com.aditya.shop.entity.Image;
import com.aditya.shop.repository.ImageRepository;
import com.aditya.shop.service.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final Path directoryPath;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, @Value("${enigma_shop.multipart.path_location}") String directoryPath) {
        this.imageRepository = imageRepository;
        this.directoryPath = Paths.get(directoryPath);
    }

    @PostConstruct
    public void initDirectory(){
        if(!Files.exists(directoryPath)){
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }


    @Override
    public Image create(MultipartFile multipartFile) {
        try {
            // bair yg masuk hanya image aja
            if(!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType())){
                throw new ConstraintViolationException("invalid image format",null);
            }

            // simpan dulu di directory
            String uniqueFileName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

            // directory "/Users/idazanggara/Documents/Gambar Enigma S
            // hop/"+uniqueFileName
//		directoryPath + uniqueFileName
            Path filePath = directoryPath.resolve(uniqueFileName);
            // save image ke directory
            Files.copy(multipartFile.getInputStream(), filePath);

            // simpan pathnya di database
            Image image = Image.builder()
                    .name(uniqueFileName)
                    .size(multipartFile.getSize())
                    .contentType(multipartFile.getContentType())
                    .path(filePath.toString())
                    .build();
            imageRepository.saveAndFlush(image);

            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Resource getById(String id) {
       try {
           Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

           Path imagePath = Paths.get(image.getPath());

           if (!Files.exists(imagePath)) {
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "data not found");
           }

//           kirim objek gambarnya
           return new UrlResource(imagePath.toUri());
       } catch (IOException e) {
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
       }
    }

    @Override
    public void deleteById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not foundddd"));

            Path imagePath = Paths.get(image.getPath());

            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "data not foundd");
            }

//            ini hapus gambar yang ada di directory
            Files.delete(imagePath);

//            ini hapus gambar yang ada di database
            imageRepository.delete(image);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
