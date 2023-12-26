package sns.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sns.demo.domain.Board;
import sns.demo.dto.BoardForm;
import sns.demo.service.BoardService;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/")
@Transactional
public class BoardController {
    private final BoardService boardService;
    @GetMapping("/")
    public String home(Model model) {
        List<Board> boardList = boardService.findBoards();
        model.addAttribute("boardList", boardList);
        return "home";
    }

    @GetMapping("/board/new")
    public String createBoard(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "board/createBoardForm";
    }

    @PostMapping("/board/new")
    public String createBoard(@Valid BoardForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        Board board = new Board();
        board.setWriter(form.getWriter());
        board.setContent(form.getContent());
        boardService.register(board);
        return "redirect:/";
    }
}
