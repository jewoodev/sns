package sns.demo.domain.dto.board;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import sns.demo.domain.entity.Board;
import sns.demo.domain.entity.Comment;
import sns.demo.domain.entity.File;
import sns.demo.domain.entity.Member;

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
