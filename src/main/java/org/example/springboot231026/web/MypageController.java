package org.example.springboot231026.web;


import org.example.springboot231026.dto.dogsell.DogSellReadDTO;
import org.example.springboot231026.dto.dogsell.cart.WishNumDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.message.MessageDTO;
import org.example.springboot231026.dto.post.PostsResponseDto;
import org.example.springboot231026.service.dogsell.WishNumService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.example.springboot231026.service.message.MessageService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.util.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    @Autowired
    private MessageService ms;

    @Autowired
    private WishNumService wns;

    @Autowired
    private GuestbookService gs;

    @Autowired
    private PostsService ps;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/message")
    public void list(Model model, @AuthenticationPrincipal MemberDTO memberDTO){
        System.out.println("컨트롤러 MessageController list() 진입");

        List<MessageDTO> senderResult = ms.senderList(memberDTO.getEmail());
        if (senderResult != null && senderResult.size() > 0) {
            System.out.println("컨트롤러 MessageController list() 진입 보낸쪽지 개수 -> "+ senderResult.size());
            model.addAttribute("seDtoList",senderResult);
            model.addAttribute("countSender",senderResult.size());

        }

        List<MessageDTO> recipientResult = ms.recipientList(memberDTO.getEmail());
        if (recipientResult != null && recipientResult.size() > 0) {

            System.out.println("컨트롤러 MessageController list() 진입 받은쪽지 개수 -> "+ recipientResult.size());
            model.addAttribute("reDtoList",recipientResult);
            model.addAttribute("countRecipient",recipientResult.size());
        }

        if(memberDTO !=null){
            System.out.println("컨트롤러 MessageController list() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/post")
    public void post(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        System.out.println("컨트롤러 MessageController post() 진입");
        List<PostsResponseDto> postsResponseDtoList = ps.listMyPage(memberDTO.getName());

        if(memberDTO !=null){
            System.out.println("컨트롤러 MessageController guestbook() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }

        if(postsResponseDtoList !=null && postsResponseDtoList.size() > 0) {
            model.addAttribute("pDtoList", postsResponseDtoList);
        }

    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/guestbook")
    public void guestbook(Model model, @AuthenticationPrincipal MemberDTO memberDTO) {
        System.out.println("컨트롤러 MessageController guestbook() 진입");
        List<GuestbookDTO> guestbookDTOList = gs.getListMyPage(memberDTO.getName());


        if(memberDTO !=null){
            System.out.println("컨트롤러 MessageController guestbook() 진입- MemberDTO 존재할때 진입");
            model.addAttribute("memberDTO",memberDTO);
        }

        if(guestbookDTOList !=null && guestbookDTOList.size() > 0){
            System.out.println("컨트롤러 MessageController guestbook() 진입- List<GuestbookDTO> 존재할때 진입");
            model.addAttribute("gDtoList",guestbookDTOList);
        }
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/wishlist")
    public void wishnumlist(Model model, @AuthenticationPrincipal MemberDTO memberDTO){
        if(memberDTO !=null) {
            System.out.println("컨트롤러 MessageController wishnumlist() 진입 -> " + memberDTO.getName());
            System.out.println("컨트롤러 MessageController wishnumlist() 진입 -> " + memberDTO.toString());

        }
        Map<String, Object> map = wns.wishNumListGet(memberDTO.getName());
        if(!MapUtils.isEmpty(map)) {
            System.out.println("컨트롤러 MessageController wishnumlist() 진입 -Map<String,Object> 존재할경우 진입");

            List<WishNumDTO> wishNumDTOList = (List<WishNumDTO>) map.get("wishNumDTOList");
            model.addAttribute("wishNumDTOList", wishNumDTOList);

            Long countWishNum = (Long) map.get("countWishNum");
            model.addAttribute("countWishNum", countWishNum);

            model.addAttribute("memberDTO", memberDTO);

            List<DogSellReadDTO> dogSellReadDTOList = (List<DogSellReadDTO>) map.get("dogSellReadDTOList");
            model.addAttribute("dogSellReadDTOList", dogSellReadDTOList);
            //Long dno; double weight, age, price;  String writer, content,title, gender, type, name, health;
            //LocalDateTime createdDate, modifiedDate;
            // List<DogSellImageDTO> dsiDtoList;
        }else{

            model.addAttribute("memberDTO", memberDTO);

        }

    }


}
