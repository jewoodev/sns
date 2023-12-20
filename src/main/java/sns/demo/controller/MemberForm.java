package sns.demo.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
public class MemberForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 2, max = 20, message = "아이디는 2 ~ 20자 사이로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password1;

    @NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
    private String password2;
}
