package org.example.springboot231026.domain.member;

import org.example.springboot231026.manytomany.domain.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository mr;

    @Autowired
    private PasswordEncoder pe;

    @Autowired
    private ReviewRepository rr;

    @Test
    public void insertReviews(){
        System.out.println("테스트시작 domain-member클래스 MemberRepositoryTest");

        IntStream.rangeClosed(2,12).forEach(i -> {


            Member m = Member.builder().email(i + "email@email.com")
                    .role(RoleType.USER)
                    .password(pe.encode("1111"))
                    .fromSocial(false)
                    .name(i + "username")
                    .build();
            mr.save(m);
        });//IntStream
    }

/*
    @Commit
    @Transactional
    @Test
    public void removeReviewsTest() {
        System.out.println("테스트시작 domain-member클래스 MemberRepositoryTest");

        //String eamil = "2email@email.com";
        String name ="5username";
        Member m = mr.findByName(name).orElseGet(() ->{return new Member();});
        rr.deleteByMember(m);
        mr.deleteById(m.getEmail());
    }
*/


//System.out.println("테스트시작 domain-member클래스 MemberRepositoryTest");
}