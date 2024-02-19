package sns.demo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import sns.demo.domain.dto.CustomUserDetails;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Likes;
import sns.demo.domain.entity.Member;
import sns.demo.web.service.BoardService;
import sns.demo.web.service.LikeService;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class LikeController {

    private final LikeService likeService;
    private final BoardService boardService;

    @PatchMapping("/board/{id}/likes")
    public String increase(@PathVariable(name = "id") Long id,
                           Authentication authentication, Model model) {
        Board board = boardService.findById(id);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Member member = userDetails.getMember(); // 로그인 된 유저

        Optional<Likes> optional = likeService.findByBoardAndMember(board, member);
        if (optional.isPresent()) {
            Likes likes = optional.get();

            // 해당 게시물의 총 좋아요 수 처리, 유저가 좋아요를 눌렀을 때 하트 변화 기능
            if (likes.isDoLike()) {
                boardService.decreaseLikes(board);
                likeService.changeLike(likes);
            } else {
                boardService.increaseLikes(board);
                likeService.changeLike(likes);
            }
        }

        return "redirect:/board/{id}";
    }
}
