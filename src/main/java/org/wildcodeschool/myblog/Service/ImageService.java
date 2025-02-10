package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        this.imageMapper = new ImageMapper();
    }

    public List<ImageDTO> getImages() {
        List<Image> images = imageRepository.findAll();
        if (images.isEmpty()) {
            throw new ResourceNotFoundException("No images found");
        }
        return images.stream().map(imageMapper::convertToDTO).collect(Collectors.toList());
    }

    public ImageDTO getImageById(Long id) {
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found for id " + id));
        return this.imageMapper.convertToDTO(foundImage);
    }

    public ImageDTO createImage(Image image) {
        Image createdImage = imageRepository.save(image);
        return this.imageMapper.convertToDTO(createdImage);
    }

    public ImageDTO updateImage(Long id, Image image) {
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found for id " + id));
        foundImage.setUrl(image.getUrl());
        Image updatedImage = imageRepository.save(foundImage);
        return this.imageMapper.convertToDTO(updatedImage);
    }

    public void deleteImage(Long id) {
        Image foundImage = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found for id " + id));
        imageRepository.delete(foundImage);
    }
}
