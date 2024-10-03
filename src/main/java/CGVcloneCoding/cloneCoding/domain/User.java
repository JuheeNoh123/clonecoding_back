package CGVcloneCoding.cloneCoding.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public boolean checkPassword(String rawpassword) {
        return this.password.equals(rawpassword);
    }
}
