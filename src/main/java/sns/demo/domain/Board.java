package sns.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @OrderBy("boardImageId asc")
    private List<BoardImage> boardImages;
}
