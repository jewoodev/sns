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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sns.demo.argumentresolver.Login;
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
    public String createBoard(@Valid BoardForm form, @Login Member loginMember, BindingResult result, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        Board board = Board.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .member(loginMember)
                .build();


        Long id = boardService.register(board);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/board/{id}";
    }
}
