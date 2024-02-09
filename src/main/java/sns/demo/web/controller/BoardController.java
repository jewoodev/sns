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
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.BoardEntity;
import sns.demo.domain.entity.FileEntity;
import sns.demo.domain.dto.BoardDTO;
import sns.demo.web.service.BoardService;
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

        List<FileEntity> images = fileService.uploadFiles(form.getImageFiles());

        BoardEntity boardEntity = BoardEntity.builder()
                            .title(form.getTitle())
                            .content(form.getContent())
                            .memberEntity(userDetails.getMemberEntity())
                            .boardImages(images)
                            .build();

        if (!images.isEmpty()) {
            for (FileEntity image : images) {
                FileEntity file = FileEntity.builder()
                                                    .boardEntity(boardEntity)
                                                    .filename(image.getFilename())
                                                    .filepath(image.getFilepath())
                                                    .build();
                fileService.upload(file);
            }
        }

        Long id = boardService.register(boardEntity);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}")
    public String referBoard(@PathVariable(name = "id") Long id, Authentication a, Model model) {
        BoardEntity boardEntity = boardService.findOne(id);
        log.info("Board = {}", boardEntity);
        List<FileEntity> boardImages = fileService.findAllByBoardId(id);

        for (FileEntity boardImage : boardImages) {
            log.info("BoardImagePath = {}", boardImage.getFilepath());
        }

        model.addAttribute("board", boardEntity);
        model.addAttribute("boardImages", boardImages);
        model.addAttribute("username", a.getName());
        return "board/referBoard";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable(name = "filename") String filename) throws MalformedURLException {
        FileEntity boardImage = null;
        if (fileService.findByFileName(filename).isPresent()) {
             boardImage = fileService.findByFileName(filename).get();
        }
        log.info("boardImage.url = {}", fileService.getFullPath(boardImage.getFilepath()));
        return new UrlResource("file:" + fileService.getFullPath(boardImage.getFilepath()));
    }
}
