package org.example.springboot231026.web;

import org.example.springboot231026.domain.member.RoleType;
import org.example.springboot231026.dto.ajax.ResponseDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class MemberController {

    @Autowired
    private MemberService ms;

    @GetMapping("/member/login")
    public void login(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model){
        System.out.println("컨트롤러클래스 MemberController login() 진입");

    }

    @GetMapping("/member/join")
    public void joinGet(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model){
        System.out.println("컨트롤러클래스 MemberController joinGet() 진입");

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/updateJoin")
    public void updateJoin(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                           @AuthenticationPrincipal MemberDTO memberDTO){
        System.out.println("컨트롤러클래스 MemberController updateJoin() 진입");
        model.addAttribute("memberDTO", memberDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/member/updateSocialJoin")
    public String updateSocialJoin(@ModelAttribute("pageRequestDto") PageRequestDTO requestDTO, Model model,
                           @AuthenticationPrincipal MemberDTO memberDTO){
        System.out.println("컨트롤러클래스 MemberController updateSocialJoin() 진입");
        model.addAttribute("memberDTO", memberDTO);

        return "member/updateSocialJoin";
    }

}
