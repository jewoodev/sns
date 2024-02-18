package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sns.demo.domain.dto.CommentDTO;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.web.service.CommentService;

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
