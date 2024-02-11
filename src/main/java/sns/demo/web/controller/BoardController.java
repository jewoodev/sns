package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sns.demo.domain.dto.CommentDTO;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Comment;
import sns.demo.domain.entity.File;
import sns.demo.domain.dto.BoardDTO;
import sns.demo.web.service.BoardService;
import sns.demo.web.service.CommentService;
import sns.demo.web.service.CustomUserDetailsService;
import sns.demo.web.service.FileService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final CustomUserDetailsService userDetailsService;
    private final CommentService commentService;

    @GetMapping("/board/new")
    public String createBoard(Model model) {
        model.addAttribute("boardDTO", new BoardDTO());
        return "board/createBoardForm";
    }

    @PostMapping("/board/new")
    public String createBoard(@Validated @ModelAttribute BoardDTO form,
                              BindingResult result, Authentication a,
                              RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(a.getName());

        List<File> images = fileService.uploadFiles(form.getImageFiles());

        Board board = Board.builder()
                            .title(form.getTitle())
                            .content(form.getContent())
                            .member(userDetails.getMember())
                            .boardImages(images)
                            .build();

        if (!images.isEmpty()) {
            for (File image : images) {
                File file = File.builder()
                                    .board(board)
                                    .filename(image.getFilename())
                                    .filepath(image.getFilepath())
                                    .build();
                fileService.upload(file);
            }
        }

        Long id = boardService.register(board);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}")
    public String referBoard(@PathVariable(name = "id") Long id, Authentication a,
                             CommentDTO commentDTO, Model model) {
        Board board = boardService.findOne(id);
        log.info("Board = {}", board);
        List<File> boardImages = fileService.findAllByBoardId(id);

        for (File boardImage : boardImages) {
            log.info("BoardImagePath = {}", boardImage.getFilepath());
        }

        model.addAttribute("board", board);
        model.addAttribute("boardImages", boardImages);
        model.addAttribute("username", a.getName());

        // 해당 게시물의 댓글 조회와 댓글 작성 기능 구현
        List<Comment> comments = commentService.findByBoardId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDTO", commentDTO);

        return "board/referBoard";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable(name = "filename") String filename) throws MalformedURLException {
        File boardImage = null;
        if (fileService.findByFileName(filename).isPresent()) {
             boardImage = fileService.findByFileName(filename).get();
        }
        log.info("boardImage.url = {}", fileService.getFullPath(boardImage.getFilepath()));
        return new UrlResource("file:" + fileService.getFullPath(boardImage.getFilepath()));
    }
}
