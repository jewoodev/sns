package sns.demo.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sns.demo.domain.Board;
import sns.demo.domain.Member;
import sns.demo.web.service.BoardService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(Authentication a, Model model) {
        if (a == null) {
            return "home";
        }

        List<Board> boardList = boardService.findBoards();

        model.addAttribute("boardList", boardList);
        model.addAttribute("username", a.getName());
        return "loginHome";
    }

}
