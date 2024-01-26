package sns.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import sns.demo.argumentresolver.Login;
import sns.demo.domain.Board;
import sns.demo.domain.Member;
import sns.demo.dto.LoginForm;
import sns.demo.service.BoardService;
import sns.demo.service.MemberServiceImpl;

import java.util.List;


@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberServiceImpl memberServiceImpl;
    private final BoardService boardService;


    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute(name = "loginForm") LoginForm form,
                        BindingResult result, HttpServletRequest request) {
        if (result.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = memberServiceImpl.login(form.getUsername(), form.getPassword());
        log.info("login? {}", loginMember);

        if (loginMember == null) {
            result.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }

    @GetMapping("/")
    public String login(@Login Member loginMember, Model model) {
        if (loginMember == null) {
            return "home";
        }

        List<Board> boardList = boardService.findBoards();

        model.addAttribute("boardList", boardList);
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

//    private void expireCookie(HttpServletResponse response, String cookieName) {
//        Cookie cookie = new Cookie(cookieName, null);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//    }
}
