package sns.demo.domain.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.File;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardResponseDTO {

    private String title;

    private String content;

    private List<File> boardImages;

    @Builder
    public BoardResponseDTO(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImages = board.getBoardImages();
    }
}
