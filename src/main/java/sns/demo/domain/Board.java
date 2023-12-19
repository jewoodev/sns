package sns.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    @Column(length = 20, nullable = false)
    private String writer;
    @Column(length = 20, nullable = false)
    private String content;
    @CreationTimestamp
    private LocalDateTime regDate;
}
