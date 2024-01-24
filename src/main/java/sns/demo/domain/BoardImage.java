package sns.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BoardImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardImageId;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
