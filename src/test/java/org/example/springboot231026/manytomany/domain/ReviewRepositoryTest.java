package org.example.springboot231026.manytomany.domain;

import org.example.springboot231026.domain.member.MemberRepository;
import org.junit.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository rr;

    @Autowired
    private PasswordEncoder pe;

    @Autowired
    private MemberRepository mr;

    @Autowired
    private DogRepository dr;

    //@Transactional
   /* @Test
    public void insertReviews(){
        System.out.println("테스트시작 manytomany-domain클래스 ReviewRepositoryTest");

        IntStream.rangeClosed(1,12).forEach(i ->{

            Long dno = (long)(Math.random()*11)+1;

            Dog d = dr.findById(dno).orElseGet(() ->{return new Dog();});
            System.out.println("DB에서 가져온 Dog 엔티티출력 -> "+d.toString());

            Member m = mr.findByName(i+"username").orElseGet(()->{return new Member();});

            System.out.println("DB에서 가져온 Member 엔티티출력 -> "+m.toString());

            Review entity = Review.builder()
                    .dog(d)
                    .grade((int)(Math.random()*5)+1)//5까지
                    .text("귀엽네요")
                    .member(m)
                    .build();

            System.out.println("생성한 Review 엔티티출력 -> "+entity.toString());
            rr.save(entity);

        });
    }//데이터삽입
*/
    @Test
    public void getDogReviewsTest() {
        System.out.println("테스트시작 manytomany-domain클래스 ReviewRepositoryTest");

        Dog entity = dr.findById(9L).orElseGet(() -> {return new Dog();});

        List<Review> result = rr.findByDog(entity);

        result.forEach(review -> {
            System.out.println("결과 getGrade() ->"+ review.getGrade());
            System.out.println("결과 getText() ->"+ review.getText());
            System.out.println("결과 getMember().getEmail() ->"+ review.getMember().getName());
        });

      }




}