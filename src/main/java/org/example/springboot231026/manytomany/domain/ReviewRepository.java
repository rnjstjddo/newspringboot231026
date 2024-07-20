package org.example.springboot231026.manytomany.domain;

import org.example.springboot231026.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"member"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Review> findByDog(Dog dog);

    //삭제는 연결테이블이 먼저 이루어진다. 동사부터
    @Modifying
    @Query("delete from Review r where r.member =:member")
    void deleteByMember(@Param("member") Member member);



}
