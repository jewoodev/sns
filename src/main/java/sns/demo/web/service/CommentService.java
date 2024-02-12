package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.dto.CommentDTO;
import sns.demo.domain.entity.Comment;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.CommentRepository;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardService boardService;

    @Transactional
    public void write(CommentDTO dto, Long boardId, Member member) {
        Comment comment = Comment.builder()
                .board(boardService.findById(boardId))
                .body(dto.getBody())
                .member(member)
                .build();

        commentRepository.save(comment);
    }


    public List<Comment> findByBoardId(Long boardId) {

        return commentRepository.findByBoard(boardService.findById(boardId));
    }

}
