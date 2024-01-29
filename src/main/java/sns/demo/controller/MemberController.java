package sns.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sns.demo.argumentresolver.Login;
import sns.demo.domain.Member;
import sns.demo.dto.MemberForm;
import sns.demo.dto.UpdateMemberPasswordForm;
import sns.demo.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/new")
    public String createMember(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@Validated @ModelAttribute MemberForm form,
                               BindingResult result) {

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
        Member member = Member.builder()
                .username(form.getUsername())
                .password(form.getPassword1())
                .email(form.getEmail())
                .build();


        //유저 네임 중복의 경우 처리
        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            result.rejectValue("username", "duplicatedUsername", e.getMessage());
            return "members/createMemberForm";
        }

        return "redirect:/";
    }

    @GetMapping("/update/password")
    public String updateMember(Model model) {
        model.addAttribute("updateMemberPasswordForm", new UpdateMemberPasswordForm());
        return "members/updateMemberPasswordForm";
    }

    @PostMapping("/update/password")
    public String updateMemberPassword(@Validated @ModelAttribute UpdateMemberPasswordForm form,
                               BindingResult result, @Login Member loginMember) {
        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "updateMemberPasswordForm";
        }

        //기존 비밀번호 확인 과정
        if (!form.getCurrentPassword().equals(loginMember.getPassword())) {
            result.rejectValue("currentPassword", "previousPasswordIncorrect", "원래 비밀번호와 일치하지 않습니다.");
            return "updateMemberPasswordForm";
        }

        //비밀번호 확인 과정
        if (!form.getNewPassword().equals(form.getCheckPassword())) {
            result.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "updateMemberPasswordForm";
        }

        String newPassword = form.getNewPassword();
        memberService.updateMemberPassword(loginMember, newPassword);

        return "redirect:/";
    }
}
