package com.sajoproject.sajotuna.feed.entity;

import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.feed.dto.createFeedDto.CreateFeedRequestDto;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // createFeed 메서드에 사용
    public Feed(CreateFeedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        User user = new User();
        user.setUserId(requestDto.getUserId()); // User 객체의 ID 설정
        this.user = user;
    }
}
