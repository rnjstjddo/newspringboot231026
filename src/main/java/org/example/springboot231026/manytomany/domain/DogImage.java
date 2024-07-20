package org.example.springboot231026.manytomany.domain;

import lombok.*;

import javax.persistence.*;

@Entity@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude="dog")
public class DogImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch=FetchType.LAZY)
    private Dog dog;



}
