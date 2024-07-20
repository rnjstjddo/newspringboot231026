package org.example.springboot231026.manytomany.domain;


import lombok.*;
import org.example.springboot231026.domain.member.Member;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"dog","member"})
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    @ManyToOne(fetch = FetchType.LAZY)
    private Dog dog;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false, length = 2000)
    private String text;

}
