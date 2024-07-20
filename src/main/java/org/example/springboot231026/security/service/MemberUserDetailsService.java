package org.example.springboot231026.security.service;

import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.dto.member.MemberDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import static org.example.springboot231026.dto.member.MemberDTO.setAuthorities;


@Service
public class MemberUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository mr;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 파라미터 username -> "+ username );

        Member entity = mr.findByEmail(username).orElseGet(() ->{ return new Member();});

        System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 파라미터 username으로 DB엔티티 가져옴 Member ->"+entity.toString() );

        //Member entity = mr.findByUsername(username,false).orElseThrow(() ->{return new UsernameNotFoundException("회원이 존재하지 않습니다.");});


        if((entity != null && entity.isFromSocial() == true) || (entity != null && entity.isFromSocial() == false)) {

            System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 " +
                    " Member엔티티가 존재하고 fromSocial 값이 true/false인 경우 진입 entity -> "+ entity.toString());

            MemberDTO dto = new MemberDTO(
                    entity.getEmail(),
                    entity.getPassword(),
                    entity.isFromSocial(),
                    setAuthorities(entity.getRole().toString())

            );
            dto.setName(entity.getName());
            dto.setModifiedDate(entity.getModifiedDate());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setAttrs(null);
            dto.setRole(entity.getRole());

            System.out.println("security-service MemberUserDetailsService 오버라이딩 loadUserByUsername() 진입 MemberDTO(UserDetails반환타입) ->" + dto.toString());

            return dto;
        }//if문
        return null;
    }
}
