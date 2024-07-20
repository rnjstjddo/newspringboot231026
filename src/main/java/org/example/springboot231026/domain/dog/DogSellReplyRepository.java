package org.example.springboot231026.domain.dog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DogSellReplyRepository extends JpaRepository<DogSellReply, Long> {


    @Query("select r.drno from DogSellReply r where r.replyer=:replyer")
    List<Long> getDogSellReplyDrnoByWriter(@Param("replyer") String replyer);

}
