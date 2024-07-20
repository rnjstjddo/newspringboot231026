package org.example.springboot231026.manytomany.domain;


import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Dog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dno;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false, length = 2000)
    private String content;


    @Column(nullable = false, length = 200)
    private String writer;


    @Column(nullable = false, length = 200)
    private String type;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private Double age;

    @Column(nullable = false, length = 2000)
    private String health;

    @Column(length = 2000)
    private String other;

}
