package sns.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sns.demo.domain.Board;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardForm {
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 2, max = 20, message = "아이디는 2 ~ 20자 사이로 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private List<MultipartFile> imageFiles;
}
