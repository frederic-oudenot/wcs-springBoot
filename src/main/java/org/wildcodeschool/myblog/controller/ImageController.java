package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.Service.ImageService;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageRepository imageRepository;
    private final ArticleRepository articleRepository;
    private final ImageService imageService;

    public ImageController(ImageRepository imageRepository, ArticleRepository articleRepository, ImageService imageService) {
        this.imageRepository = imageRepository;
        this.articleRepository= articleRepository;
        this.imageService = imageService;
    }

    /*
    * GET ALL IMAGES
    * */
    @GetMapping()
    public ResponseEntity<List<ImageDTO>> getAllImages(){
        List<ImageDTO> images = imageService.getImages();
        return ResponseEntity.status(HttpStatus.OK).body(images);
    }

    /*
     * GET ONE IMAGE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id){
        ImageDTO foundImageDTO = imageService.getImageById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foundImageDTO);
    }

    /*
     * POST ONE IMAGE
     * */
    @PostMapping()
    public ResponseEntity<ImageDTO> createImage(@RequestBody Image image){
        if(image.getUrl() == null){
            throw new ResourceNotFoundException("Missing image url is not null");
        }
        ImageDTO createdImageDTO = imageService.createImage(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdImageDTO);
    }

    /*
     * UPDATE ONE IMAGE
     * */
    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@RequestBody Image image, @PathVariable Long id){
        if(image.getUrl() == null){
            throw new ResourceNotFoundException("Missing image url is not null");
        }
        ImageDTO updatedImageDTO = imageService.updateImage(id, image);
        return ResponseEntity.status(HttpStatus.OK).body(updatedImageDTO);
    }

    /*
     * DELETE ONE IMAGE
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id){
        imageService.deleteImage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }