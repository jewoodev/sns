package sns.demo.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.BoardEntity;
import sns.demo.domain.entity.MemberEntity;
import sns.demo.domain.repository.BoardRepository;
import sns.demo.web.session.SessionManager;

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
        BoardEntity boardEntity = BoardEntity.builder()
                .title("제우식당")
                .content("맛집은 세상을 아름답게해")
                .memberEntity(MemberEntity.builder()
                        .username("신제우")
                        .password("1234")
                        .email("asd@naver.com")
                        .build())
                .build();

        repository.save(boardEntity);

        BoardEntity foundOne = repository.findById(boardEntity.getId()).get();
        assertThat(foundOne.getId()).isEqualTo(boardEntity.getId());
        assertThat(foundOne.getMemberEntity()).isEqualTo(boardEntity.getMemberEntity());
        assertThat(foundOne.getContent()).isEqualTo(boardEntity.getContent());
        assertThat(foundOne.getCreatedDate()).isEqualTo(boardEntity.getCreatedDate());
        assertThat(foundOne).isEqualTo(boardEntity);
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