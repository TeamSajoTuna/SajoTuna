package com.sajoproject.sajotuna.refresh.entity;

import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "RefreshToken")
public class RefreshToken extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long userId;

    private String token;

    private String nickName;
    private String email;
    // Enum 을 내부적으로 STRING 형태로 돌 수 있도록 설정
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public RefreshToken(Long userId, String nickname, String email, UserRole userRole, String refreshToken) {
        this.userId = userId;
        this.nickName = nickname;
        this.email = email;
        this.userRole = userRole;
        this.token = refreshToken;
    }
}
