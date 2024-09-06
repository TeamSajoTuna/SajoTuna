package com.sajoproject.sajotuna.feed.entity;

import com.sajoproject.sajotuna.comment.entity.Comment;
import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.following.entity.Follow;
import com.sajoproject.sajotuna.likes.entity.Likes;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Feed")
@NoArgsConstructor
public class Feed extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")  // 컬럼 이름 명시
    private Long feedId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comment = new ArrayList<>();


    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    // 조회수 필드
    @Column(name = "view_count", nullable = false)
    private int viewCount=0;

    public Feed(FeedCreateDtoRequest requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user; // User 객체 설정
    }

    public void feedUpdate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Feed(Long feedId) {
        this.feedId = feedId;
    }
}
