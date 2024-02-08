package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.BoardEntity;
import sns.demo.domain.repository.BoardRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public Long register(BoardEntity boardEntity) {
        return boardRepository.save(boardEntity);
    }


    public BoardEntity findOne(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }


    public List<BoardEntity> findBoards() {
        return boardRepository.findAll();
    }


    public void deleteOne(int boardId) {

    }

//    @Transactional
//    public void uploadBoardImage(String)
}
