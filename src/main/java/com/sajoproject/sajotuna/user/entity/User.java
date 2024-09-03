package com.sajoproject.sajotuna.user.entity;

import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.following.entity.follow;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // 컬럼 이름 명시
    private Long userId;

    @Column(name = "pw")
    private String pw;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "email")
    private String email;

    // Enum 을 내부적으로 STRING 형태로 돌 수 있도록 설정
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feed = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<follow> followed = new ArrayList<>();


    public User (String nickname, String email, String pw, UserRole userRole){
        this.nickname = nickname;
        this.email = email;
        this.pw = pw;
        this.userRole = userRole;
    }
}
