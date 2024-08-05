package org.example.springboot231026.service.inquiry;


import lombok.AllArgsConstructor;
import org.example.springboot231026.domain.inquiry.Inquiry;
import org.example.springboot231026.domain.inquiry.InquiryReply;
import org.example.springboot231026.domain.inquiry.InquiryReplyRepository;
import org.example.springboot231026.domain.inquiry.InquiryRepository;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.inquiry.InquiryReplyDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.example.springboot231026.domain.inquiry.QInquiryReply.inquiryReply;

@Service
@AllArgsConstructor
public class InquiryReplyService {


    private final InquiryReplyRepository irerepo;
    private final InquiryRepository irepo;

    private final ModelMapper mm;


    @Transactional
    public Boolean registerInquiryReply(InquiryReplyDto inquiryReplyDto) {
        System.out.println("service-inquiry패키지 InquiryReplyService registerInquiryReply() 진입");

        Optional<Inquiry> o = irepo.findById(inquiryReplyDto.getInnum());

        InquiryReply inquriyReply = null;

        if (o.isPresent()) {
            inquiryReplyDto.setInquiryDto(mm.map(o.get(), InquiryDto.class));

            inquriyReply = mm.map(inquiryReplyDto, InquiryReply.class);
            if (irerepo.save(inquriyReply).getInrenum() > 0) {
                System.out.println("service-inquiry패키지 InquiryReplyService registerInquiryReply() 진입 답변등록 성공진입");

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public InquiryReplyDto getInquiryReply(Long innum) {
        System.out.println("service-inquiry패키지 InquiryReplyService getInquiryReply() 진입");

        Optional<InquiryReply> o = irerepo.getInquiryReply(innum);

        if(o.isPresent()){
            System.out.println("service-inquiry패키지 InquiryReplyService getInquiryReply() 진입 "+
                    " 해당 문의글에 대한 답변이 존재할 경우 진입");

            InquiryReply inquiryReply =o.get();

            return mm.map(inquiryReply, InquiryReplyDto.class);
        };
        return null;
    }
}