package sns.demo.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.Board;
import sns.demo.domain.Member;
import sns.demo.session.SessionManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SnsBoardRepositoryTest {

    @Autowired
    BoardRepository repository;

    @Autowired
    SessionManager sessionManager;

    @Test
    @Transactional
    @Rollback
    void save() {
        Board board = new Board(null, "제우식당", "맛집은 세상을 아름답게해", new Member(null, "신제우", "1234", "asd@naver.com", null), null, null);

        repository.save(board);

        Board foundOne = repository.findById(board.getBoardId()).get();
        assertThat(foundOne.getBoardId()).isEqualTo(board.getBoardId());
        assertThat(foundOne.getMember()).isEqualTo(board.getMember());
        assertThat(foundOne.getContent()).isEqualTo(board.getContent());
        assertThat(foundOne.getRegDate()).isEqualTo(board.getRegDate());
        assertThat(foundOne).isEqualTo(board);
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void delete() {
    }
}