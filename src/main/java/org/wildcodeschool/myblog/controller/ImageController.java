package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ImageDTO;
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
        try {
            List<Image> foundImages = imageRepository.findAll();
            if (foundImages.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ImageDTO> imageDTOs = foundImages.stream().map(this::convertToDTO).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(imageDTOs);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * GET ONE IMAGE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<ImageDTO> getImageById(@PathVariable Long id){
        try {
            Image foundImage = imageRepository.findById(id).orElse(null);
            if (foundImage == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(foundImage));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*
     * POST ONE IMAGE
     * */
    @PostMapping()
    public ResponseEntity<ImageDTO> createImage(@RequestBody Image image){
        try{
            Image createdImage = imageRepository.save(image);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(createdImage));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    /*
     * UPDATE ONE IMAGE
     * */
        @PutMapping("/{id}")
        public ResponseEntity<ImageDTO> updateImage(@RequestBody Image image, @PathVariable Long id){

        Image foundImage = imageRepository.findById(id).orElse(null);
        if (foundImage == null) {
            return ResponseEntity.notFound().build();
        }

        foundImage.setUrl(image.getUrl());
        Image updatedImage = imageRepository.save(foundImage);
        return ResponseEntity.status(HttpStatus.OK).body(convertToDTO(updatedImage));
        }

    /*
     * DELETE ONE IMAGE
     * */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteImage(@PathVariable Long id){
        Image foundImage = imageRepository.findById(id).orElse(null);
        if (foundImage == null) {
            return ResponseEntity.notFound().build();
        }
        imageRepository.delete(foundImage);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }