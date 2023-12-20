package sns.demo.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 2, max = 20, message = "아이디는 2 ~ 20자 사이로 입력해주세요.")
    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
}
