package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.MemberEntity;
import sns.demo.domain.Role;
import sns.demo.domain.dto.JoinDTO;
import sns.demo.domain.dto.UpdatePasswordDTO;
import sns.demo.web.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/new")
    public String createMember(Model model) {
        model.addAttribute("joinDTO", new JoinDTO());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@Validated @ModelAttribute JoinDTO form,
                               BindingResult result) {

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "members/createMemberForm";
        }

        // 1. 비밀번호 확인 과정
        if (form.getPassword1() != null && form.getPassword2() != null
                && !form.getPassword1().equals(form.getPassword2())) {
            result.rejectValue("password2", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "members/createMemberForm";
        }

        // 2. 성공 로직
        // 2-1. 비밀번호 인코딩
        String encodedPwd = passwordEncoder.encode(form.getPassword1());

        MemberEntity memberEntity = MemberEntity.builder()
                .username(form.getUsername())
//                .password(form.getPassword1())
                .password(encodedPwd)
                .email(form.getEmail())
                .role(Role.USER.name())
                .build();


        //유저 네임 중복의 경우 처리
        try {
            memberService.join(memberEntity);
        } catch (IllegalStateException | InvalidDataAccessApiUsageException e) {
            result.rejectValue("username", "duplicatedUsername", e.getMessage());
            return "members/createMemberForm";
        }

        return "redirect:/login";
    }

    @GetMapping("/update/password")
    public String updateMember(Model model) {
        model.addAttribute("updatePasswordDTO", new UpdatePasswordDTO());
        return "members/updateMemberPasswordForm";
    }

    @PostMapping("/update/password")
    public String updateMemberPassword(@Validated @ModelAttribute UpdatePasswordDTO form,
                                       BindingResult result, Authentication a) {
        CustomUserDetails userDetails = (CustomUserDetails) a.getPrincipal();
        log.info("principal = {}", userDetails);

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "members/updateMemberPasswordForm";
        }

        //기존 비밀번호 확인 과정
        if (!passwordEncoder.matches(form.getCurrentPassword(), userDetails.getPassword())) {
            result.rejectValue("currentPassword", "previousPasswordIncorrect", "원래 비밀번호와 일치하지 않습니다.");
            return "members/updateMemberPasswordForm";
        }

        //비밀번호 확인 과정
        if (!form.getNewPassword().equals(form.getCheckPassword())) {
            result.rejectValue("checkPassword", "passwordIncorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "members/updateMemberPasswordForm";
        }

        log.info("뭐가 문제니?");

        String newPassword = passwordEncoder.encode(form.getNewPassword());
        memberService.updateMemberPassword(userDetails.getMemberEntity(), newPassword);

        return "redirect:/";
    }
}
