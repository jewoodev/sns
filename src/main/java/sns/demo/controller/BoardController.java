package sns.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sns.demo.argumentresolver.Login;
import sns.demo.domain.Board;
import sns.demo.domain.Member;
import sns.demo.domain.UploadFile;
import sns.demo.dto.BoardForm;
import sns.demo.service.BoardService;
import sns.demo.session.SessionManager;
import sns.demo.upload.file.FileStore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardController {
    private final BoardService boardService;
    private final FileStore fileStore;

    @GetMapping("/board/new")
    public String createBoard(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "board/createBoardForm";
    }

    @PostMapping("/board/new")
    public String createBoard(@Valid @ModelAttribute BoardForm form, @Login Member loginMember, BindingResult result, RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        List<UploadFile> uploadFiles = fileStore.storeFiles(form.getImageFiles());

        Board board = Board.builder()
                .boardId(null)
                .title(form.getTitle())
                .content(form.getContent())
                .member(loginMember)
                .boardImages(uploadFiles)
                .build();


        Long id = boardService.register(board);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}")
    public String referBoard(@PathVariable(name = "id") Long id, Model model) {
        Board board = boardService.findOne(id);
        log.info("Board = {}", board);
        model.addAttribute("board", board);
        return "board/referBoard";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }
}
