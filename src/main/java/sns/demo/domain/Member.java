package sns.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String username;
    private String password;

    @Column(unique = true)
    private String email;

//    @OneToMany(mappedBy = "m", cascade = CascadeType.ALL)
//    private List<Board> boardList = new ArrayList<>();

    public void updatePassword(String password) {
        this.password = password;
    }
}
