package org.wildcodeschool.myblog.mapper;

import org.wildcodeschool.myblog.dto.ImageCreatedDTO;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Image;

import java.util.stream.Collectors;

public class ImageMapper {
    public ImageDTO convertToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setUrl(image.getUrl());
        if (image.getArticles() != null) {
            imageDTO.setArticleIds(image.getArticles().stream().map(Article::getId).collect(Collectors.toList()));
        }
        return imageDTO;
    }

    public Image convertToEntity(ImageCreatedDTO imageCreatedDTO) {
        Image image = new Image();
        image.setId(imageCreatedDTO.getId());
        image.setUrl(imageCreatedDTO.getUrl());
        if (imageCreatedDTO.getArticleIds() != null) {
            imageCreatedDTO.setArticleIds(imageCreatedDTO.getArticleIds());
        }
        return image;
    }
}
