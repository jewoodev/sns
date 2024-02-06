package sns.demo.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class UpdateMemberPasswordForm {
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "변경 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "변경 비밀번호를 한번 더 입력해주세요.")
    private String checkPassword;
}
