package org.example.springboot231026.service.dogsell;
/*
import org.example.springboot231026.domain.dog.*;
import org.example.springboot231026.domain.member.Member;
import org.example.springboot231026.domain.member.MemberRepository;
import org.example.springboot231026.dto.dogsell.DogSellDTO;
import org.example.springboot231026.dto.dogsell.DogSellImageDTO;
import org.example.springboot231026.dto.dogsell.cart.DogSellCartDTO;
import org.example.springboot231026.dto.member.MemberDTO;
import org.example.springboot231026.manytomany.domain.Dog;
import org.example.springboot231026.service.member.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;



@Service
public class DogSellCartService {

    @Autowired
    private DogSellRepository dsr;

    @Autowired
    private DogSellImageRepository dsir;

    @Autowired
    private MemberRepository mr;

    @Autowired
    private MemberService ms;

    @Autowired
    private DogSellCartRepository dscr;

    @Autowired
    private ModelMapper mm;


    //가져오기
    @Transactional
    public DogSellCartDTO getWishNum(String email, String name) {

        System.out.println("service-dogsell클래스 DogSellCartService insertWithNum() 진입 파라미터 email -> " + email);
        System.out.println("service-dogsell클래스 DogSellCartService insertWithNum() 진입 파라미터 name -> " + name);

        Optional<Member> om = mr.findById(email);
        Member mEntity = om.get();
        MemberDTO mDto = ms.toMemberDto(mEntity);


        List<DogSellDTO> dogSellDTOList = new ArrayList<>();
        List<WishNum> wishNumList= mEntity.getWishNumList();

        for(WishNum wishnum : wishNumList) {

            Optional<DogSell> dogSell = dsr.findById(wishnum.getWishnum());

            List<DogSellImage> dogSellImage = dsir.findByDno(wishnum.getWishnum());
            List<DogSellImageDTO> dogSellImageDTOList = dogSellImage.stream()
                    .map(entity -> mm.map(entity, DogSellImageDTO.class))
                    .collect(Collectors.toList());

            DogSellDTO dogSellDTO = mm.map(dogSell, DogSellDTO.class);
            dogSellDTO.setDsiDtoList(dogSellImageDTOList);

            dogSellDTOList.add(dogSellDTO);
        }//for문


        //DogSellCartDTO -> MemberDTO mDto; / List<Long> wishnumList; /List<DogSellDTO>
        //반환객체 미리생성
        DogSellCartDTO dogSellCartDTO =new DogSellCartDTO(mDto);
        dogSellCartDTO.setDsDtoList(dogSellDTOList);
        dogSellCartDTO.setWishnumList();

        return dogSellCartDTO;
    }


    public void insertWishNum(Long num, String email) {

        System.out.println("service-dogsell클래스 DogSellCartService insertWithNum() 진입 파라미터 email -> " + email);

        Optional<Member> om = mr.findById(email);
        Member mentity = om.get();
        System.out.println("service-dogsell클래스 DogSellCartService insertWithNum() 진입 Member -> "+mentity.toString());

        Optional<DogSell> dso= dsr.findById(num);

        DogSellCart dogSellCart =DogSellCart.builder()
                .dogSell(dso.get())
                .member(mentity)
                .build();
        dogSellCart.addWishnumList(num);

        Long cartnum = dscr.save(dogSellCart).getCartnum();

        Optional<DogSellCart> o = dscr.findById(cartnum);
        System.out.println("service-dogsell클래스 DogSellCartService insertWithNum() 진입 생성한 엔티티 DogSellCart -> "+o.get().toString());

    }

}

 */