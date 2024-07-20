package org.example.springboot231026.domain.dog;


import lombok.*;
import org.example.springboot231026.domain.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@ToString(exclude = "member")
@ToString
public class WishNum {

    @Id
    //@Column(name="wish_num_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long wishnum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="member")
    private Member member;


}
