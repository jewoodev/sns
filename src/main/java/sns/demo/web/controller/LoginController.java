package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sns.demo.domain.dto.LoginDTO;
import sns.demo.domain.entity.BoardEntity;
import sns.demo.web.service.BoardService;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

//    private final MemberService memberService;
    private final BoardService boardService;
//    private final BCryptPasswordEncoder passwordEncoder;

//    @PostMapping("/login")
//    public String login(@Validated @ModelAttribute(name = "loginForm") LoginForm form,
//                        BindingResult result, HttpServletRequest request) {
//        if (result.hasErrors()) {
//            return "login/loginForm";
//        }
//
//        Member loginMember = memberService.login(form.getUsername(), form.getPassword());
//        log.info("login? {}", loginMember);
//
//        if (loginMember == null) {
//            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//            return "login/loginForm";
//        }
//
//        //로그인 성공 처리
//        HttpSession session = request.getSession();
//
//        //세션에 로그인 회원 정보 보관
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//        return "redirect:/";
//    }

    @GetMapping("/login")
    public String loginForm(
            @ModelAttribute("loginForm") LoginDTO form,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "exception", required = false) String exception, Model model) {
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
        model.addAttribute("loginForm", form);
        return "/login/loginForm";
    }


//    @GetMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//
//        if (session != null) {
//            session.invalidate();
//        }
//
//        return "redirect:/";
//    }

//    private void expireCookie(HttpServletResponse response, String cookieName) {
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//    }
}
