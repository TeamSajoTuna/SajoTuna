package com.sajoproject.sajotuna.likes.entity;

import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long likesId;

    @ManyToOne
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public Likes(User user, Feed feed) {
        this.user = user;
        this.feed = feed;

    }
}
