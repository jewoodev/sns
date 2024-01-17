package sns.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sns.demo.domain.Member;
import sns.demo.dto.LoginForm;
import sns.demo.dto.MemberForm;
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
        Member member = new Member(null, username, password, email);
        Long joinedId = memberServiceImpl.join(member);
        ra.addAttribute("memberId", joinedId);
        ra.addAttribute("status", true);
        return "redirect:/members/{memberId}";
    }
}
