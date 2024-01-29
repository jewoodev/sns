package sns.demo.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "upload_file")
public class UploadFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_file_id")
    private Long id;

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
