package org.example.springboot231026.domain.inquiry;

import org.example.springboot231026.domain.inquiry.search.InquirySearch;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquirySearch {


    //@Query("select count(i) from Inquiry i where i.modifiedDate like localDate%")
    //Long getCountLocalDate(@Param("localDate") LocalDate localDate);


    @Query("select count(g) from Inquiry g where g.modifiedDate >= :before and g.modifiedDate < :after")
    Long getCountLocalDate(@Param("before") LocalDateTime before, @Param("after") LocalDateTime after);



    //dto-> entity
    default Inquiry toEntity(InquiryDto inquiryDto){
        //System.out.println("repository-inquiry패키지 dto -> entity 변환 진입");

        Inquiry entity = Inquiry.builder()
                .email(inquiryDto.getEmail())
                .innum(inquiryDto.getInnum())
                .phone(inquiryDto.getPhone())
                .complete(inquiryDto.getComplete())
                .content(inquiryDto.getContent())
                .writer(inquiryDto.getWriter())
                .build();
        return entity;
    }

    //entity -> dto
    default InquiryDto toDto(Inquiry entity){
        //System.out.println("repository-inquiry패키지 entity -> dto 변환 진입");
        InquiryDto inquiryDto = new InquiryDto(
                entity.getInnum(),entity.getWriter(), entity.getContent(), entity.getPhone(),
                entity.getEmail(),entity.getComplete(), entity.getCreatedDate(), entity.getModifiedDate()
        );
        return inquiryDto;
    }


}
