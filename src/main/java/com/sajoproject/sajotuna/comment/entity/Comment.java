package com.sajoproject.sajotuna.comment.entity;


import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
