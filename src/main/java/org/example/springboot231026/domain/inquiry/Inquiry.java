package org.example.springboot231026.domain.inquiry;


import lombok.*;
import org.example.springboot231026.domain.posts.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Inquiry extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long innum;

    @Column(name="writer", nullable = false)
    private String writer;

    @Column(name = "content",nullable = false)
    private String content;

    @Column(name="phone")
    private String phone;

    @Column(name="email", nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(100) default 'false'", name="complete",nullable = false)
    @Builder.Default
    //private Boolean complete = false;
    private String complete = "false";
}
