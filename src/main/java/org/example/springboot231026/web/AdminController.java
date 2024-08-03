package org.example.springboot231026.web;


import lombok.AllArgsConstructor;
import org.example.springboot231026.domain.Count;
import org.example.springboot231026.dto.dogsell.DogSellListDTO;
import org.example.springboot231026.dto.guestbook.GuestPageRequestDTO;
import org.example.springboot231026.dto.guestbook.GuestPageResultDTO;
import org.example.springboot231026.dto.guestbook.GuestbookDTO;
import org.example.springboot231026.dto.guestbook.GuestbookReplyDTO;
import org.example.springboot231026.dto.inquiry.InquiryDto;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.dto.page.PageRequestDTO;
import org.example.springboot231026.dto.page.PageResponseDTO;
import org.example.springboot231026.dto.post.PostReplyDTO;
import org.example.springboot231026.dto.post.PostsListResponseDto;
import org.example.springboot231026.service.dogsell.DogSellService;
import org.example.springboot231026.service.guestbook.GuestbookReplyService;
import org.example.springboot231026.service.guestbook.GuestbookService;
import org.example.springboot231026.service.inquiry.InquiryService;
import org.example.springboot231026.service.member.MemberService;
import org.example.springboot231026.service.posts.PostReplyService;
import org.example.springboot231026.service.posts.PostsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    //분양글
    private final DogSellService dogSellService;
    //방명록
    private final GuestbookService gs;
    private final GuestbookReplyService grs;
    //게시글
    private final PostsService ps;
    private final PostReplyService prs;

    //문의
    private final InquiryService is;
    //회원
    private final MemberService ms;


    //관리자페이지 메인
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/home/home") //admin/home/home
    public String adminHome(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                            @AuthenticationPrincipal MemberDTO memberDTO,
                            @RequestParam(required = false) String yearmonth,
                            @RequestParam(required = false) String tabtitle) {
        System.out.println("관리자컨트롤러 /admin/home/home 진입");

        if (tabtitle != null) {
            System.out.println("관리자컨트롤러 /admin/home/home 진입 tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        } else {
            System.out.println("관리자컨트롤러 /admin/home/home 진입 tabtitle 존재하지 않을때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", "guestbook");
        }

        if (yearmonth == null) {
            yearmonth = LocalDate.now().toString();
            System.out.println("관리자컨트롤러 /admin/home/home 진입 쿼리스트링 yearmonth 없을때 -> " + yearmonth);
        }

        //페이지처리 없는 방명록개수
        List<GuestbookDTO> guestbookDTOList = gs.findByAll();

        //페이지처리 없는 방명록댓글수
        List<GuestbookReplyDTO> guestbookReplyDTOList = grs.findAll();

        if ((guestbookDTOList != null && guestbookDTOList.size() > 0) ||
                (guestbookReplyDTOList != null && guestbookReplyDTOList.size() > 0)) {
            System.out.println("방명록개수 -> " + guestbookDTOList.size());
            System.out.println("방명록댓글개수 -> " + guestbookReplyDTOList.size());

            //방명록날짜만 중복안되게 들고오기
            List<LocalDateTime> guestbookDateList = new ArrayList<>();

            //방명록댓글 날짜만 중복안되게 들고오기
            List<LocalDateTime> guestbookReplyDateList = new ArrayList<>();

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                //달의 첫일자로 맞춘다
                model.addAttribute("yearmonth", afterYearMonth);

                LocalDate plusAfterYearMonth = afterYearMonth.plusMonths(1);
                System.out.println("VIEW컨트롤러 /home/home 원본받은 날짜 -> " + yearmonth + ", 시작날짜로 변경 -> " + afterYearMonth + ", 한달더한 날짜 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01
                //방명록날짜만 중복안되게 들고오기
                guestbookDateList = guestbookDTOList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(GuestbookDTO::getModifiedDate)
                        .distinct().collect(Collectors.toList());

                //방명록댓글 날짜만 중복안되게 들고오기
                guestbookReplyDateList = guestbookReplyDTOList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(GuestbookReplyDTO::getModifiedDate)
                        .distinct().collect(Collectors.toList());
            }

            //최종결과인 방명록날짜와 글수 Map만듬
            Map<LocalDate, Long> guestbookDateCount = new HashMap<>();


            //최종결과인 방명록댓글 날짜와 글수 Map만듬
            Map<LocalDate, Long> guestbookReplyDateCount = new HashMap<>();


            //방명록 날짜별 등록개수
            for (LocalDateTime guestbookDate : guestbookDateList) {

                LocalDate localDate = guestbookDate.toLocalDate();

                //System.out.println("방명록 LocalDateTime ->  " + guestbookDate+", LocalDate -> "+ localDate);
                //방명록
                Long guestCount = (Long) guestbookDTOList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                //System.out.println("방명록갯수 guestCount ->  " + guestCount);

                guestbookDateCount.put(localDate, guestCount);


            }//for문

            //방명록댓글 날짜별 등록개수
            for (LocalDateTime guestbookDate : guestbookReplyDateList) {

                LocalDate localDate = guestbookDate.toLocalDate();

                //System.out.println("방명록댓글 LocalDateTime ->  " + guestbookDate+", LocalDate -> "+ localDate);

                //방명록댓글
                Long guestReplyCount = (Long) guestbookReplyDTOList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                //System.out.println("방명록댓글갯수 guestReplyCount ->  " + guestReplyCount);

                guestbookReplyDateCount.put(localDate, guestReplyCount);
            }

            //방명록갯수 날짜별
            model.addAttribute("guestbookDateCount", guestbookDateCount);
            //guestbookDateCount.forEach((k, v) -> System.out.println("방명록 키 -> " +k +", 값 -> "+ v) );

            //방명록댓글갯수 날짜별
            model.addAttribute("guestbookReplyDateCount", guestbookReplyDateCount);
            //guestbookReplyDateCount.forEach((k, v) -> System.out.println("방명록댓글 키 -> " +k +", 값 -> "+ v) );
        }

        //게시판 ___________________________________________________________________________________

        //페이지처리 없는 게시판
        List<PostsListResponseDto> postsListResponseDtoList = ps.findAll();
        //페이지처리 없는 게시판댓글
        List<PostReplyDTO> postReplyDTOList = prs.findAll();


        if ((postsListResponseDtoList != null && postsListResponseDtoList.size() > 0) ||
                (postReplyDTOList != null && postReplyDTOList.size() > 0)) {
            System.out.println("게시판개수 -> " + guestbookDTOList.size());
            System.out.println("게시판댓글개수 -> " + guestbookReplyDTOList.size());

            //게시판날짜만 중복안되게 들고오기
            List<LocalDateTime> postsDateList = new ArrayList<>();

            //게시판댓글날짜만 중복안되게 들고오기
            List<LocalDateTime> postReplyDateList = new ArrayList<>();

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                ; //달의 첫일자로 맞춘다

                LocalDate plusAfterYearMonth = afterYearMonth.plusMonths(1);
                System.out.println("VIEW컨트롤러 /home/home 원본받은 날짜 -> " + yearmonth + ", 시작날짜로 변경 -> " + afterYearMonth + ", 한달더한 날짜 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01
                //게시판날짜만 중복안되게 들고오기
                postsDateList = postsListResponseDtoList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(PostsListResponseDto::getModifiedDate)
                        .distinct().collect(Collectors.toList());

                //게시판댓글 날짜만 중복안되게 들고오기
                postReplyDateList = postReplyDTOList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(PostReplyDTO::getModifiedDate)
                        .distinct().collect(Collectors.toList());
            }//if 전달되어온 yearmonth존재할때


            //최종결과인 게시판날짜와 글수 Map만듬
            Map<LocalDate, Long> postsDateCount = new HashMap<>();

            //최종결과인 게시판댓글 날짜와 글수 Map만듬
            Map<LocalDate, Long> postReplyDateCount = new HashMap<>();

            //게시판 날짜별 등록개수
            for (LocalDateTime postsDate : postsDateList) {

                LocalDate localDate = postsDate.toLocalDate();

                System.out.println("게시판 LocalDateTime ->  " + postsDate + ", LocalDate -> " + localDate);
                //게시판개수
                Long postCount = (Long) postsListResponseDtoList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                System.out.println("게시판갯수 guestCount ->  " + postCount);

                postsDateCount.put(localDate, postCount);

            }//for문

            //postsListResponseDtoList postReplyDTOList postsDateList  postReplyDateList postDateCount postReplyDateCount

            //게시판댓글 날짜별 등록개수
            for (LocalDateTime postReplyDate : postReplyDateList) {

                LocalDate localDate = postReplyDate.toLocalDate();

                System.out.println("게시판댓글 LocalDateTime ->  " + postReplyDate + ", LocalDate -> " + localDate);

                //게시판댓글수
                Long postReplyCount = (Long) postReplyDTOList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                System.out.println("게시판댓글수 postReplyCount ->  " + postReplyCount);

                postReplyDateCount.put(localDate, postReplyCount);

            }

            //방명록갯수 날짜별
            model.addAttribute("postsDateCount", postsDateCount);
            postsDateCount.forEach((k, v) -> System.out.println("게시판 키 -> " + k + ", 값 -> " + v));

            //방명록댓글갯수 날짜별
            model.addAttribute("postReplyDateCount", postReplyDateCount);
            postReplyDateCount.forEach((k, v) -> System.out.println("게시판댓글 키 -> " + k + ", 값 -> " + v));

        }//전체 게시판목록이 있을때

        //문의글 ___________________________________________________________________________________

        List<InquiryDto> inquiryDtoList = is.findAll();
        if (inquiryDtoList.size() > 0 && inquiryDtoList != null) {

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                //달의 첫일자로 맞춘다
                LocalDate plusAfterYearMonth = afterYearMonth.plusMonths(1);
                //달의 끝일자로 맞춘다.

                //System.out.println("VIEW컨트롤러 /home/home 원본받은 날짜 -> " + yearmonth + ", 시작날짜로 변경 -> " + afterYearMonth + ", 한달더한 날짜 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01
                //방명록날짜만 중복안되게 들고오기
                List<LocalDateTime> inquiryDateList = inquiryDtoList.stream()
                        .filter(r -> r.getModifiedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(r -> r.getModifiedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(InquiryDto::getModifiedDate)
                        .distinct().peek(System.out::println).collect(Collectors.toList());

                //Model객체
                Map<LocalDate, Long> inquiryDateCount = new HashMap<>();

                for (LocalDateTime inquiryDate : inquiryDateList) {

                    LocalDate localDate = inquiryDate.toLocalDate();

                    //System.out.println("문의글 LocalDateTime ->  " + inquiryDate + ", LocalDate -> " + localDate);

                    //일자별 문의글수
                    Long inquiryCount = (Long) inquiryDtoList.stream()
                            .filter(r -> r.getModifiedDate().toLocalDate().isEqual(localDate)).count();
                    System.out.println("문의글수 inquiryCount ->  " + inquiryCount);

                    inquiryDateCount.put(localDate, inquiryCount);

                }//for종료 문의글 날짜별
                model.addAttribute("inquiryDateCount", inquiryDateCount);
                inquiryDateCount.forEach((k, v) -> System.out.println("문의글 키 -> " + k + ", 값 -> " + v));

            }//if종료 yearmonth 존재할때
        }//전체문의글이 존재할때

        //회원___________________________________________________________________________________

        List<MemberDTO> memberDTOList = ms.findAll();
        if (memberDTOList.size() > 0 && memberDTOList != null) {

            if (yearmonth != null) {
                LocalDate afterYearMonth = LocalDate.parse(yearmonth).with(TemporalAdjusters.firstDayOfMonth());
                //달의 첫일자로 맞춘다
                LocalDate plusAfterYearMonth = afterYearMonth.plusMonths(1);
                //달의 끝일자로 맞춘다.

                System.out.println("VIEW컨트롤러 /home/home 원본받은 날짜 -> " + yearmonth + ", 시작날짜로 변경 -> " + afterYearMonth + ", 한달더한 날짜 -> " + plusAfterYearMonth);//받은 날짜 -> 2024-04-01, 한달더한 날짜 -> 2024-05-01

                //날짜별List만들기
                List<LocalDateTime> memberDateList = memberDTOList.stream().filter(i -> i.getCreatedDate().toLocalDate().isAfter(afterYearMonth))
                        .filter(i -> i.getCreatedDate().toLocalDate().isBefore(plusAfterYearMonth))
                        .map(MemberDTO::getCreatedDate).distinct().peek(System.out::println).collect(Collectors.toList());


                //Model객체
                Map<LocalDate, Long> memberDateCount = new HashMap<>();

                for (LocalDateTime memberDate : memberDateList) {

                    LocalDate localDate = memberDate.toLocalDate();
                    //해당일자에 가입한 회원수
                    Long memberCount = (Long) memberDTOList.stream()
                            .filter(r -> r.getCreatedDate().toLocalDate().isEqual(localDate)).count();
                    System.out.println("회원수 memberCount ->  " + memberCount);

                    memberDateCount.put(localDate, memberCount);

                }//for종료 문의글 날짜별
                model.addAttribute("memberDateCount", memberDateCount);
                memberDateCount.forEach((k, v) -> System.out.println("회원 Map담긴 키 -> " + k + ", 값 -> " + v));

            }//if yearmonth null 아닐경우

        }//if문 회원List존재할때

        return "redirect:/admin/admin_home";
    }


    //분양글
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dogsell/list")
    public String adminDogList(Model model, @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String yearmonth,
                               @RequestParam(required = false) String tabtitle) {
        System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 파라미터 날짜 String -> " + yearmonth);
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        List<DogSellListDTO> dogSellList = dogSellService.adminDogSellList();

        List<LocalDateTime> localDateTimeList = dogSellList.stream().map(DogSellListDTO::getModifiedDate)
                .collect(Collectors.toList());


        model.addAttribute("dogSellList", dogSellList);
        model.addAttribute("localDateTimeList", localDateTimeList);


        model.addAttribute("loginMember", memberDTO.getName());
        return "admin/admin_dog_list";
    }
    
    //관리자페이지에서 품종검색
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dogsell/list/search")
    public String adminDogListPost(Model model, @AuthenticationPrincipal MemberDTO memberDTO,
                               @RequestParam(required = false) String yearmonth,
                               @RequestParam(required = false) String tabtitle,
                                   @ModelAttribute("searchBreed") String searchBreed) {
        System.out.println("관리자컨트롤러 /admin/dogsell/list 진입 품종검색 searchBread-> " + searchBreed);
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        //List<DogSellListDTO> dogSellList = dogSellService.adminDogSellList();
        List<DogSellListDTO> dogSellList = dogSellService.searchBreed(searchBreed);

        List<LocalDateTime> localDateTimeList = dogSellList.stream().map(DogSellListDTO::getModifiedDate)
                .collect(Collectors.toList());


        model.addAttribute("dogSellList", dogSellList);
        model.addAttribute("localDateTimeList", localDateTimeList);


        model.addAttribute("loginMember", memberDTO.getName());
        return "admin/admin_dog_list";
        //return "redirect:/admin/dogsell/list";
    }


    //방명록글 특정날짜만 쿼리만들자.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbook/list")
    public String adminGuestbookList(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                     @RequestParam(required = false) String yearmonth,
                                     @AuthenticationPrincipal MemberDTO memberDTO,
                                     @RequestParam(required = false) String tabtitle,
                                     @ModelAttribute("count") Count count) {
        System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 String타입 날짜 -> " + yearmonth);
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            GuestPageResultDTO pResponseDto = gs.getListAdminModifiedDate(pageRequestDTO, localDate);
            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 " +
                        " GuestPageResultDTO getSize() -> " + pResponseDto.getPage() + ", getTotalPage() -> " + pResponseDto.getTotalPage());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getGuestcount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                System.out.println("Count출력 -> "+ count.toString());
            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/guestbook/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_guestbook_list";
    }

    //방명록댓글 특정날짜만 쿼리만들자.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/guestbookreply/list")
    public String adminGuestbookReplyList(@ModelAttribute("pageRequestDTO") GuestPageRequestDTO pageRequestDTO, Model model,
                                          @RequestParam(required = false) String yearmonth,
                                          @AuthenticationPrincipal MemberDTO memberDTO,
                                          @RequestParam(required = false) String tabtitle,
                                          @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 String타입 날짜 -> " + yearmonth);
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            GuestPageResultDTO pResponseDto = grs.getReplyListAdmin(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 " +
                        " GuestPageResultDTO getSize() -> " + pResponseDto.getSize() + ", getTotalPage() -> " + pResponseDto.getTotalPage());

                model.addAttribute("pResponseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getGuestreplycount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                System.out.println("Count출력 -> "+ count.toString());

            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/guestbookreply/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_guestbookreply_list";

    }

    //게시글 날짜처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/post/list")
    public String adminPostList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                                @AuthenticationPrincipal MemberDTO memberDTO,
                                @RequestParam(required = false) String yearmonth,
                                @RequestParam(required = false) String tabtitle,
                                @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러 /admin/post/list 진입");
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }
        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            PageResponseDTO pResponseDto = ps.getListAdminModifiedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/post/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize() + ", getTotal() -> " + pResponseDto.getTotal());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }

            if(count.getPostcount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                System.out.println("Count출력 -> "+ count.toString());

            }
        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/post/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_post_list";
    }


    //게시글댓글 날짜처리
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/postreply/list") //admin/postreply/list
    public String adminPostReplyList(@ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model,
                                     @AuthenticationPrincipal MemberDTO memberDTO,
                                     @RequestParam(required = false) String yearmonth,
                                     @RequestParam(required = false) String tabtitle,
                                     @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러 /admin/postreply/list 진입");
        if (tabtitle != null) {

            System.out.println("tabtitle 존재할때 진입 ->  " + tabtitle);
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            PageResponseDTO pResponseDto = prs.getListAdminModifiedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/postreply/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize() + ", getTotal() -> " + pResponseDto.getTotal());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }


            if(count.getPostreplycount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                System.out.println("Count출력 -> "+ count.toString());

            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/postreply/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_postreply_list";
    }


    //문의글
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/inquiry/list")
    public String adminInguiryList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                   @AuthenticationPrincipal MemberDTO memberDTO,
                                   @RequestParam(required = false) String yearmonth,
                                   @RequestParam(required = false) String tabtitle,
                                   @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러 /admin/inquiry/list 진입");

        model.addAttribute("tabtitle", tabtitle);
        model.addAttribute("localDate", yearmonth);

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            PageResponseDTO pResponseDto = is.getListAdminModifiedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }


            if(count.getInquirycount() ==null) {
                count = this.returnCount(localDate);
                model.addAttribute("count", count);
                System.out.println("Count출력 -> "+ count.toString());

            }

        }//yearmonth존재할경우
        else {
            System.out.println("관리자컨트롤러 /admin/inquiry/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }


        if (memberDTO != null) {
            model.addAttribute("loginMember", memberDTO.getName());
        }

        return "admin/admin_inquiry_list";
    }


    //회원
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/member/list")
    public String adminMemberList(Model model, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO,
                                  @AuthenticationPrincipal MemberDTO memberDTO,
                                  @RequestParam(required = false) String yearmonth,
                                  @RequestParam(required = false) String tabtitle,
                                  @ModelAttribute Count count) {
        System.out.println("관리자컨트롤러 /admin/member/list 진입-> " + pageRequestDTO.toString());

        if (tabtitle != null) {
            model.addAttribute("tabtitle", tabtitle);
        }

        if (yearmonth != null) {
            LocalDate localDate = LocalDate.parse(yearmonth);
            model.addAttribute("localDate", yearmonth);

            PageResponseDTO pResponseDto = ms.getListAdminModifiedDate(pageRequestDTO, localDate);

            if (pResponseDto.getDtoList().size() > 0 && pResponseDto.getEnd() != 0) {
                System.out.println("관리자컨트롤러 /admin/member/list 진입 " +
                        " PageResponseDTO getSize() -> " + pResponseDto.getSize());

                model.addAttribute("responseDtoList", pResponseDto.getDtoList());
                model.addAttribute("pResponseDto", pResponseDto);
            }
        }//if존재시
        else {
            System.out.println("관리자컨트롤러 /admin/member/list 진입 쿼리스트링으로 yearmonth 존재하지 않을때 진입");
            return "redirect:/admin/home/home";
        }

        return "admin/admin_member_list";
    }

    //일자에 맞는 개수반환 현재 회원은 오류로 제외시킴
    public Count returnCount(LocalDate localDate) {
        System.out.println("관리자컨트롤러 returnCount() 진입 Count 객체반환");

        //반환타입
        Count count = new Count();

        //문의글
        count.setInquirycount(is.getCountLocalDate(localDate));
        //게시글댓글
        count.setPostreplycount(prs.getCountLocalDate(localDate));
        //게시글
        count.setPostcount(prs.getCountLocalDate(localDate));
        //방명록
        count.setGuestcount(gs.getCountLocalDate(localDate));
        //방명록댓글
        count.setGuestreplycount(grs.getCountLocalDate(localDate));

        System.out.println("관리자컨트롤러 returnCount() 진입 Count 객체반환 -> "+ count.toString());

        return count;
    }

}