package sns.demo.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import sns.demo.domain.dto.board.BoardRequestDTO;

import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<File> boardImages;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @ColumnDefault("0")
    private Long viewCount;

    @ColumnDefault("0")
    private Long likeCount;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Likes> likes;

    public void update(BoardRequestDTO board, List<File> boardImages) {
        this.title = board.getTitle();
        this.content = board.getContent();
        this.boardImages = boardImages;
    }

    public void increaseViews() {
        ++this.viewCount;
    }

    public void increaseLikes() {
        ++this.likeCount;
    }

    public void decreaseLikes() {
        --this.likeCount;
    }
}
