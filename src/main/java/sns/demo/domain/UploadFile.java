package sns.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;


    private String filename;
    private String filepath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public UploadFile(String filename, String filepath) {
        this.filename = filename;
        this.filepath = filepath;
    }
}
