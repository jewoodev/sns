package sns.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMemberForm {
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String currentPassword;

    @NotBlank(message = "변경 비밀번호를 입력해주세요.")
    private String newPassword;

    @NotBlank(message = "변경 비밀번호를 한번 더 입력해주세요.")
    private String checkPassword;
}
