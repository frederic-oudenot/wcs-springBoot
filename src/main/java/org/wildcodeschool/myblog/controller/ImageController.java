package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ImageController(ImageRepository imageRepository, ArticleRepository articleRepository) {
        this.imageRepository = imageRepository;
        this.articleRepository= articleRepository;
    }

    private ImageDTO convertToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setUrl(image.getUrl());
        if (image.getArticles() != null) {
            imageDTO.setArticleIds(image.getArticles().stream().map(Article::getId).collect(Collectors.toList()));
        }
        return imageDTO;
    }

    /*
    * GET ALL IMAGES
    * */
    @GetMapping()
    public ResponseEntity<List<ImageDTO>> getAllImages(){
        List<Image> foundImages = imageRepository.findAll();
        if (foundImages.isEmpty()) {
            throw new ResourceNotFoundException("No images found");
        }
        List<ImageDTO> imageDTOs = foundImages.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(imageDTOs);
    }

    /*
     * GET ONE IMAGE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id){
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found for id " + id));
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(foundImage));
    }

    /*
     * POST ONE IMAGE
     * */
    @PostMapping()
    public ResponseEntity<ImageDTO> createImage(@RequestBody Image image){
        if(image.getUrl() == null){
            throw new ResourceNotFoundException("Missing image url is not null");
        }
        Image createdImage = imageRepository.save(image);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdImage));
    }

    /*
     * UPDATE ONE IMAGE
     * */
    @PutMapping("/{id}")
    public ResponseEntity<ImageDTO> updateImage(@RequestBody Image image, @PathVariable Long id){
        if(image.getUrl() == null){
            throw new ResourceNotFoundException("Missing image url is not null");
        }
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found for id " + id));
        foundImage.setUrl(image.getUrl());
        Image updatedImage = imageRepository.save(foundImage);
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(updatedImage));
    }

    /*
     * DELETE ONE IMAGE
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id){
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found for id " + id));
        imageRepository.delete(foundImage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }