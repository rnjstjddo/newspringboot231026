package org.example.springboot231026.manytomany.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {

    //Dog와 Review Outer Join
   /* @Query("select d, avg(coalesce(r.grade, 0)), count(distinct r) from Dog d left outer join Review "+
    " r on r.dog = d group by d")
    Page<Object []> getListPage(Pageable pageable);
*/
    
    //위 쿼리메소드에서 DogImage추가 목록에서 보이도록 결과에서 컬럼이 추가된다. 
    // = 즉 강아지목록페이지에 강아지, 강아지이미지, 평점, 리뷰수까지 출력
    @Query("select d, di, avg(coalesce(r.grade, 0)), count(distinct r) from Dog d left outer join Review "+
            " r on r.dog = d left outer join DogImage di on di.dog = d group by d")
    Page<Object []> getListPage(Pageable pageable);


    //특정 강아지의 모든 이미지와 평균평점 리뷰개수 출력 = 즉, 강아지조회페이지 여기서는 페이지 필요없다. List<Object []>
    //@Query("select d,di from Dog d left outer join DogImage di on di.dog = d where d.dno=:dno")
    //위에쿼리에서 결과로 강아지이미지와 평점과 리뷰수를 강아지 이미지별로 그룹핑시킨다.
    @Query("select d, di, avg(coalesce(r.grade,0)), count(r) from Dog d left outer join DogImage di "+
    " on di.dog =d left outer join Review r on r.dog = d where d.dno =:dno group by di ")
    List<Object []> getDogWithAll(@Param("dno") Long dno);
}
