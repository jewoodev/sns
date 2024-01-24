package sns.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sns.demo.domain.Board;
import sns.demo.domain.Member;
import sns.demo.dto.BoardForm;
import sns.demo.service.BoardService;
import sns.demo.session.SessionManager;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@Transactional
public class BoardController {
    private final BoardService boardService;
    private final SessionManager sessionManager;

    @GetMapping("/board/new")
    public String createBoard(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "board/createBoardForm";
    }

    @PostMapping("/board/new")
    public String createBoard(@Valid BoardForm form, BindingResult result, HttpServletRequest request) {
        log.info(form.toString());

        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        String title = form.getTitle();
        String content = form.getContent();
        Member member = (Member) sessionManager.getSession(request);
        Board board = new Board(null, title, content, member,null, null);
        Long Id = boardService.register(board);
        return "redirect:/board/{Id}";
    }
}
