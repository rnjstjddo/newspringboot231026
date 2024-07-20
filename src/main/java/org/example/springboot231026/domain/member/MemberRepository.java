package org.example.springboot231026.domain.member;

import org.example.springboot231026.domain.member.search.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, MemberSearch {


    @Query("select m from Member m where m.email= :email and m.fromSocial =:fromSocial")
    Optional<Member> findByUsername(@Param("email") String email, @Param("fromSocial") boolean fromSocial);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByName(String name);


}
