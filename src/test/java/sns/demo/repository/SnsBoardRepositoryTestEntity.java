package sns.demo.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.BoardRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SnsBoardRepositoryTestEntity {

    @Autowired
    BoardRepository repository;

    @Test
    @Transactional
    @Rollback
    void save() {
        Board board = Board.builder()
                .title("제우식당")
                .content("맛집은 세상을 아름답게해")
                .member(Member.builder()
                        .username("신제우")
                        .password("1234")
                        .email("asd@naver.com")
                        .build())
                .build();

        repository.save(board);

        Board foundOne = repository.findById(board.getId()).get();
        assertThat(foundOne.getId()).isEqualTo(board.getId());
        assertThat(foundOne.getMember()).isEqualTo(board.getMember());
        assertThat(foundOne.getContent()).isEqualTo(board.getContent());
        assertThat(foundOne.getCreatedDate()).isEqualTo(board.getCreatedDate());
        assertThat(foundOne).isEqualTo(board);
    }

}