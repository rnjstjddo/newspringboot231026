package org.example.springboot231026.manytomany.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;



@RunWith(SpringRunner.class)
@SpringBootTest
public class DogRepositoryTest {

    @Autowired
    private DogRepository dr;

    @Autowired
    private DogImageRepository dir;

    @Commit
    @Transactional
    @Test
    public void insertDog() {
        System.out.println("테스트 시작 manytomany-domain클래스 DogRepositoryTest");

        IntStream.rangeClosed(1, 12).forEach(i -> {

            Dog entity = Dog.builder()
                    .age(4.2)
                    .content("밖에 나가 산책하는 것을 좋아합니다.")
                    .type("시츄")
                    .health("광견병")
                    .name(i + "츄츄")
                    .writer(i+"writer")
                    .title(i + "움직이는 것을 좋아하는 츄츄입니다.")
                    .weight(4.5)
                    .build();
            dr.save(entity);

            //DogImage 넣기
            int count = (int) (Math.random() * 5) + 1;
            for (int j = 0; j < count; j++) {
                DogImage di = DogImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .dog(entity)
                        .imgName(j + "dogimage.jpg")
                        .build();

                dir.save(di);
            }
        });
    }


    @Test
    public void getListPageTest() {
        System.out.println("테스트 시작 manytomany-domain클래스 getListPageTest");

        PageRequest p = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "dno"));

        Page<Object[]> result = dr.getListPage(p);

        for(Object [] o : result.getContent()){
            System.out.println("테스트 시작 manytomany-domain클래스 getListPageTest - Object [] 배열 결과컬럼 출력진입");

            System.out.println(Arrays.toString(o));
        }

    }

    @Test
    public void getDogWithAllTest() {
        System.out.println("테스트 시작 manytomany-domain클래스 getDogWithAllTest");

        List<Object []> list =dr.getDogWithAll(11L);

        for(Object [] arr: list){
            System.out.println("결과 Object [] arr-> "+ Arrays.toString(arr));
        }
    }
}