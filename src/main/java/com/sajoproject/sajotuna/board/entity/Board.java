package com.sajoproject.sajotuna.board.entity;

import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Board")
@NoArgsConstructor
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")  // 컬럼 이름 명시
    private Long boardId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
