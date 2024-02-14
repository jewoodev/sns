package sns.demo.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.annotations.Comments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import sns.demo.domain.dto.JoinDTO;
import sns.demo.domain.entity.Member;
import sns.demo.web.auth.CustomAuthFailureHandler;
import sns.demo.web.config.SecurityConfig;
import sns.demo.web.service.BoardService;
import sns.demo.web.service.CommentService;
import sns.demo.web.service.CustomUserDetailsService;
import sns.demo.web.service.MemberService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private BoardService boardService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 성공 - 유저네임, 이메일, 비밀번호 입력")
    void successSignup() throws Exception {
        //given
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", "jewoo");
        params.add("password1", "1234");
        params.add("password2", "1234");
        params.add("email", "jewoos15@naver.com");

        //then
        mockMvc.perform(post("/members/new")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .params(params))
                .andExpect(status().is3xxRedirection());
    }
}