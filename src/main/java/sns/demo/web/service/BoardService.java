package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.entity.Board;
import sns.demo.domain.repository.BoardRepository;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Long register(Board board) {
        return boardRepository.save(board);
    }


    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElse(null);
    }


    public List<Board> findBoards() {
        return boardRepository.findAll();
    }


    @Transactional
    public void deleteById(Board board) {
        boardRepository.delete(board);
    }

    @Transactional
    public void increaseViews(Board board) {
        boardRepository.increaseViews(board);
    }

    @Transactional
    public void increaseLikes(Board board) {
        boardRepository.increaseLikes(board);
    }

    @Transactional
    public void decreaseLikes(Board board) {
        boardRepository.decreaseLikes(board);
    }
}
