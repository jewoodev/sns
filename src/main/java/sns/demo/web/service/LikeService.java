package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Likes;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.LikeRepository;

import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Transactional
    public Likes init(Member member, Board board) {
        Likes likes = Likes.builder()
                .board(board)
                .member(member)
                .build();
        return likeRepository.save(likes);
    }

    public Optional<Likes> findByBoardAndMember(Board board, Member member) {
        return likeRepository.findByBoardAndMember(board, member);
    }

    @Transactional
    public void changeLike(Likes likes) {
        likeRepository.changeLike(likes);
    }
}
