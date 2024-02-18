package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sns.demo.domain.entity.Member;
import sns.demo.web.service.MemberService;

import java.util.List;

@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final MemberService memberService;

    @GetMapping("/member/list")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/referMemberLists";
    }
}
