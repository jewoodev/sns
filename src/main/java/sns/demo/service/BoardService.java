package sns.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.Board;
import sns.demo.repository.BoardRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void register(Board board) {
        boardRepository.save(board);
    }


    public Board findOne(Long boardId) {
        return boardRepository.findById(boardId).get();
    }


    public List<Board> findBoards() {
        return boardRepository.findAll();
    }


    public void deleteOne(int boardId) {

    }
}
