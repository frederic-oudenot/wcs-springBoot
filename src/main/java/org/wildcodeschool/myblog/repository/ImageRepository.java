package org.wildcodeschool.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.wildcodeschool.myblog.model.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
