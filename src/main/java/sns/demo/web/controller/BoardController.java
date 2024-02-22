package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import sns.demo.domain.dto.board.BoardResponseDTO;
import sns.demo.domain.entity.*;
import sns.demo.domain.dto.board.BoardRequestDTO;
import sns.demo.web.service.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping("/board")
@RequiredArgsConstructor
@Transactional
@Controller
public class BoardController {

    private final BoardService boardService;
    private final FileService fileService;
    private final CustomUserDetailsService userDetailsService;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/new")
    public String createBoard(Model model) {
        model.addAttribute("boardRequestDTO", new BoardRequestDTO());
        return "board/createBoardForm";
    }

    @PostMapping("/new")
    public String createBoard(@Validated @ModelAttribute BoardRequestDTO form,
                              BindingResult result, Authentication a,
                              RedirectAttributes redirectAttributes) throws IOException {
        if (result.hasErrors()) {
            return "board/createBoardForm";
        }

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(a.getName());

        List<File> images = fileService.uploadFiles(form.getBoardImages());

        Board board = Board.builder()
                            .title(form.getTitle())
                            .content(form.getContent())
                            .member(userDetails.getMember())
                            .boardImages(images)
                            .likeCount(0L)
                            .viewCount(0L)
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

    @GetMapping("/{id}")
    public String referBoard(@PathVariable(name = "id") Long id, Authentication authentication,
                             @RequestParam(value = "exception", required = false) String exception,
                             CommentDTO commentDTO, Model model) {
        Board board = boardService.findById(id);
        List<File> boardImages = fileService.findAllByBoardId(id);

        // 1. 조회수 기능 구현
        boardService.increaseViews(board);
        model.addAttribute("viewCount", board.getViewCount());

        // 2. 해당 게시물, 게시물의 이미지들, 댓글 작성란에 기재되는 유저네임
        model.addAttribute("board", board);
        model.addAttribute("boardImages", boardImages);
        model.addAttribute("username", authentication.getName());

        // 3. 해당 게시물의 댓글 조회와 댓글 작성 기능 구현
        List<Comment> comments = commentService.findByBoardId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("commentDTO", commentDTO);

        // 4. 해당 게시물 좋아요 기능 구현
        Long likeCount = board.getLikeCount();
        model.addAttribute("likeCount", likeCount); // 게시물의 좋아요 총 갯수

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember(); // 로그인 한 유저


        Likes likes = likeService.findByBoardAndMember(board, member);
        boolean doLike = likes.isDoLike();
        model.addAttribute("doLike", doLike); // 로그인 되어있는 유저가 게시물에 좋아요를 눌렀는지의 여부

        model.addAttribute("likeCount", board.getLikeCount()); // 해당 게시물 좋아요 수
        model.addAttribute("viewCount", board.getViewCount()); // 해당 게시물 조회 수

        model.addAttribute("exception", exception); // 게시물 수정 에러 메세지 전달

        return "board/referBoard";
    }

    @GetMapping("/{id}/update")
    public String updateBoardForm(@PathVariable(name = "id") Long id, Authentication authentication,
                              Model model) {
        Board board = boardService.findById(id);
        String exceptionMessage;
        if (!board.getMember().getUsername().equals(authentication.getName())) { // 인증 없는 수정 요청 거부
            exceptionMessage = "본인의 게시물이 아니면 수정할 수 없습니다.";
            return "redirect:/board/{id}?exception=" + URLEncoder.encode(exceptionMessage, StandardCharsets.UTF_8);
        }

        BoardResponseDTO responseDTO = boardService.boardDetail(id);
        model.addAttribute("dto", responseDTO);
        model.addAttribute("id", id);

        return "board/updateBoard";
    }

    @PatchMapping("/{id}/update")
    public String updateBoard(@PathVariable(name = "id") Long id,
                              @Validated @ModelAttribute BoardRequestDTO form, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "board/updateBoard";
        }

        List<File> images;
        try {
            images = fileService.uploadFiles(form.getBoardImages());
        } catch (NullPointerException e) {
            images = Collections.emptyList();
        }

        Board board = boardService.findById(id);
        boardService.update(id, form, images);

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

        return "redirect:/";
    }

    @DeleteMapping("/{id}/delete")
    public String deleteBoard(@PathVariable(name = "id") Long id) {
        boardService.delete(boardService.findById(id));

        return "redirect:/";
    }
}
