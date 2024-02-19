package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Comment;
import sns.demo.domain.entity.Member;
import sns.demo.domain.Role;
import sns.demo.domain.dto.JoinDTO;
import sns.demo.domain.dto.UpdatePasswordDTO;
import sns.demo.web.service.BoardService;
import sns.demo.web.service.CommentService;
import sns.demo.web.service.MemberService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@RequestMapping("/members")
@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/new")
    public String createMember(Model model) {
        model.addAttribute("joinDTO", new JoinDTO());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
    public String createMember(@Validated @ModelAttribute JoinDTO form,
                               BindingResult result, Errors errors) {

        System.out.println("dto is " + form);

        if (result.hasErrors()) {
            log.info("errors={}", errors);
            return "members/createMemberForm";
        }

        // 1. 비밀번호 확인 과정
        if (form.getPassword1() != null && form.getPassword2() != null
                && !form.getPassword1().equals(form.getPassword2())) {
            result.rejectValue("password2", "passwordIncorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "members/createMemberForm";
        }

        // 2. 이메일 중복 검증
        try {
            memberService.existsByEmail(form.getEmail());
        } catch (IllegalStateException | InvalidDataAccessApiUsageException e) {
            result.rejectValue("email", "duplicatedEmail", e.getMessage());
            return "members/createMemberForm";
        }


        // 3. 유저 네임 중복의 경우 처리
        try {
            memberService.existByUsername(form.getUsername());
        } catch (IllegalStateException | InvalidDataAccessApiUsageException e) {
            result.rejectValue("username", "duplicatedUsername", e.getMessage());
            return "members/createMemberForm";
        }

        // 4. 성공 로직
        // 4-1. 비밀번호 인코딩
        String encodedPwd = passwordEncoder.encode(form.getPassword1());

        Member member = Member.builder()
                .username(form.getUsername())
//                .password(form.getPassword1())
                .password(encodedPwd)
                .email(form.getEmail())
                .role(Role.USER)
                .build();

        memberService.join(member);

        return "redirect:/login";
    }

    @GetMapping("/update/password")
    public String updateMember(Model model) {
        model.addAttribute("updatePasswordDTO", new UpdatePasswordDTO());
        return "members/updateMemberPasswordForm";
    }

    @PatchMapping("/update/password")
    public String updateMemberPassword(@Validated @ModelAttribute UpdatePasswordDTO form,
                                       BindingResult result, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
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

            String newPassword = passwordEncoder.encode(form.getNewPassword());
        memberService.updateMemberPassword(userDetails.getMember(), newPassword);

        return "redirect:/";
    }

    @GetMapping("/menu")
    public String menu(Authentication authentication, Model model) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Stream<String> roleStream = authorities.stream().map(GrantedAuthority::getAuthority);
        Optional<String> admin = roleStream.filter(r -> r.equals("ROLE_ADMIN")).findFirst();
        model.addAttribute("isAdmin", admin.isPresent());
        return "members/additionalMenu";
    }

    @GetMapping("/boards")
    public String referMembersBoards(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member loginMember = userDetails.getMember();

        List<Board> boards = boardService.findByMember(loginMember);
        model.addAttribute("boards", boards);

        return "board/usersBoards";
    }

    @GetMapping("/comments")
    public String referMembersComments(Authentication authentication, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member loginMember = userDetails.getMember();

        List<Comment> comments = commentService.findByMember(loginMember);
        model.addAttribute("comments", comments);

        return "members/usersComments";
    }

    @DeleteMapping("/delete")
    public String delete(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        memberService.delete(userDetails.getMember());

        return "redirect:/logout";
    }
}
