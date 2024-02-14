package sns.demo.web.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sns.demo.domain.dto.CommentDTO;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.*;
import sns.demo.web.service.BoardService;
import sns.demo.web.service.CommentService;
import sns.demo.web.service.FileService;
import sns.demo.web.service.LikeService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/{id}/comments")
    public String write(@PathVariable(name = "id") Long id, CommentDTO dto, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        commentService.write(dto, id, userDetails.getMember());

        return "redirect:/board/{id}";
    }

}
