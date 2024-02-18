package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Member;
import sns.demo.web.service.BoardService;
import sns.demo.web.service.MemberService;

import java.util.List;

@Slf4j
@RequestMapping("/admin")
@RequiredArgsConstructor
@Controller
public class AdminController {

    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping("/member/list")
    public String memberList(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }

    @DeleteMapping("/member/{id}/delete")
    public String deleteMember(@PathVariable(name = "id") Long id) {
        memberService.delete(memberService.findById(id).get());
        return "redirect:/admin/member/list";
    }

    @GetMapping("/board/list")
    public String boardList(Model model) {
        List<Board> boards = boardService.findBoards();
        model.addAttribute("boards", boards);

        return "board/boardList";
    }

    @DeleteMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable(name = "id") Long id) {
        boardService.delete(boardService.findById(id));
        return "redirect:/admin/board/list";
    }
}
