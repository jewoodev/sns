package sns.demo.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sns.demo.domain.dto.board.BoardRequestDTO;
import sns.demo.domain.dto.board.BoardResponseDTO;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.File;
import sns.demo.domain.entity.Member;
import sns.demo.domain.repository.BoardRepository;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public Long register(Board board) {
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 게시글입니다."));
    }

    @Transactional(readOnly = true)
    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    @Transactional
    public void delete(Board board) {
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public List<Board> findByMember(Member member) {
        return boardRepository.findByMember(member);
    }

    public void update(Long id, BoardRequestDTO dto, List<File> boardImages) {
        boardRepository.update(id, dto, boardImages);
    }

    @Transactional(readOnly = true)
    public BoardResponseDTO boardDetail(Long id) {
        try {
            Board board = findById(id);
            return BoardResponseDTO.builder()
                    .board(board)
                    .build();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("존재하지 않는 게시글입니다.");
        }
    }

    public void increaseViews(Board board) {
        boardRepository.increaseViews(board);
    }

    public void increaseLikes(Board board) {
        boardRepository.increaseLikes(board);
    }

    public void decreaseLikes(Board board) {
        boardRepository.decreaseLikes(board);
    }
}
