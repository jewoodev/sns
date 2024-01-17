package sns.demo.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import sns.demo.dto.MemberForm;
import sns.demo.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class CheckEmailValidator extends AbstractValidator<MemberForm> {
    private final MemberRepository memberRepository;

    @Override
    protected void doValidate(MemberForm dto, Errors errors) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            errors.rejectValue("email", "이메일 중복 오류", "이미 사용중인 이메일 입니다.");
        }
    }
}
