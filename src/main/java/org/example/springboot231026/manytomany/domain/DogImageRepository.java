package org.example.springboot231026.manytomany.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DogImageRepository extends JpaRepository<DogImage, Long> {
}
