package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sns.demo.domain.Board;
import sns.demo.domain.Member;
import sns.demo.domain.UploadFile;
import sns.demo.domain.dto.BoardForm;
import sns.demo.domain.repository.FileRepository;
import sns.demo.web.argumentresolver.Login;
import sns.demo.web.service.BoardService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardController {
    private final BoardService boardService;
    private final FileRepository fileRepository;

    @GetMapping("/board/new")
    public String createBoard(Model model) {
        model.addAttribute("boardForm", new BoardForm());
        return "board/createBoardForm";
    }

    @PostMapping("/board/new")
    public String createBoard(@Validated @ModelAttribute BoardForm form, BindingResult result, @Login Member loginMember, RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        List<UploadFile> images = fileRepository.storeFiles(form.getImageFiles());

        Board board = Board.builder()
                            .title(form.getTitle())
                            .content(form.getContent())
                            .member(loginMember)
                            .boardImages(images)
                            .build();

        if (!images.isEmpty()) {
            for (UploadFile image : images) {
                UploadFile file = UploadFile.builder()
                                                    .board(board)
                                                    .filename(image.getFilename())
                                                    .filepath(image.getFilepath())
                                                    .build();
                fileRepository.save(file);
            }
        }

        Long id = boardService.register(board);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}")
    public String referBoard(@PathVariable(name = "id") Long id, @Login Member loginMember, Model model) {
        Board board = boardService.findOne(id);
        log.info("Board = {}", board);
        List<UploadFile> boardImages = fileRepository.findAllByBoardId(id);

        for (UploadFile boardImage : boardImages) {
            log.info("BoardImagePath = {}", boardImage.getFilepath());
        }

        model.addAttribute("board", board);
        model.addAttribute("boardImages", boardImages);
        model.addAttribute("loginMember", loginMember);
        return "board/referBoard";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable(name = "filename") String filename) throws MalformedURLException {
        UploadFile boardImage = null;
        if (fileRepository.findByFileName(filename).isPresent()) {
             boardImage = fileRepository.findByFileName(filename).get();
        }
        log.info("boardImage.url = {}", fileRepository.getFullPath(boardImage.getFilepath()));
        return new UrlResource("file:" + fileRepository.getFullPath(boardImage.getFilepath()));
    }
}
