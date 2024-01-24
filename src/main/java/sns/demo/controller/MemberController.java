package sns.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sns.demo.domain.Member;
import sns.demo.dto.MemberForm;
import sns.demo.dto.UpdateMemberForm;
import sns.demo.service.MemberServiceImpl;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberServiceImpl memberServiceImpl;

    @GetMapping("/new")
    public String createMember(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@Validated @ModelAttribute MemberForm form,
                               BindingResult result, RedirectAttributes ra) {

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "members/createMemberForm";
        }

        //비밀번호 확인 과정
        if (form.getPassword1() != null && form.getPassword2() != null
                && !form.getPassword1().equals(form.getPassword2())) {
            result.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "members/createMemberForm";
        }

        //성공 로직
        String username = form.getUsername();
        String password = form.getPassword1();
        String email = form.getEmail();
        Member member = new Member(null, username, password, email, null);


        //유저 네임 중복의 경우 처리
        try {
            Long joinedId = memberServiceImpl.join(member);
            ra.addAttribute("memberId", joinedId);
            ra.addAttribute("status", true);
        } catch (IllegalStateException e) {
            result.rejectValue("username", "duplicatedUsername", e.getMessage());
            return "members/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/update")
    public String updateMember(Model model) {
        model.addAttribute("updateMemberForm", new UpdateMemberForm());
        return "members/updateMemberForm";
    }

//    @PostMapping("/update")
//    public String updateMember(@Validated @ModelAttribute UpdateMemberForm form,
//                               BindingResult result, RedirectAttributes ra) {
//        if (result.hasErrors()) {
//            log.info("errors={}", result);
//            return "members/updateMemberForm";
//        }
//
//        //기존 비밀번호 확인 과정
//        if (form.getCurrentPassword().equals())
//
//        //비밀번호 확인 과정
//        if (!form.getNewPassword().equals(form.getCheckPassword())) {
//            result.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
//            return "members/updateMemberForm";
//        }
//    }
}
