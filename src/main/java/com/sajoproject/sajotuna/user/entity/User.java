package com.sajoproject.sajotuna.user.entity;

import com.sajoproject.sajotuna.board.entity.Board;
import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.following.entity.follow;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> board = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<follow> following = new ArrayList<>();

    @OneToMany(mappedBy = "followed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<follow> followed = new ArrayList<>();

}
