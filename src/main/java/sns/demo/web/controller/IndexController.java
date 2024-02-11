package sns.demo.web.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sns.demo.domain.entity.Board;
import sns.demo.web.service.BoardService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IndexController {

    private final BoardService boardService;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {

        if (authentication == null) {
            return "home";
        }

        List<Board> boards = boardService.findBoards();

        model.addAttribute("boardList", boards);
        model.addAttribute("username", authentication.getName());
        return "loginHome";
    }

}
