package com.sajoproject.sajotuna.user.entity;

import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.enums.UserRole;
import com.sajoproject.sajotuna.following.entity.Follow;
import com.sajoproject.sajotuna.likes.entity.Likes;
import com.sajoproject.sajotuna.message.entity.Message;
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

    @Column(nullable = false)
    private Boolean isDeleted = false;

    // Enum 을 내부적으로 STRING 형태로 돌 수 있도록 설정
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feed> feed = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followed = new ArrayList<>();

    @OneToMany(mappedBy = "receiverId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> receiverId = new ArrayList<>();

    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Message> senderId = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    public User(String nickname, String email, String pw, UserRole userRole) {
        this.nickname = nickname;
        this.email = email;
        this.pw = pw;
        this.userRole = userRole;
    }

    public User(Long followingId) {
        this.userId = followingId;
    }

    //    업데이트 비밀번호, 닉네임, 이메일
    public void updatePw(String pw) {
        this.pw = pw;
    }
    public void updateProfile(String nickname, String email) {
        if (nickname != null && !nickname.isEmpty()) {
            this.nickname = nickname;
        }
        if (email != null && !email.isEmpty()) {
            this.email = email;
        }
    }
}
