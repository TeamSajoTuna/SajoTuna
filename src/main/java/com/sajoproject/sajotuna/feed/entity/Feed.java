package com.sajoproject.sajotuna.feed.entity;

import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.feed.dto.feedCreateDto.FeedCreateDtoRequest;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @ManyToOne(fetch = FetchType.LAZY)


    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    // feedCreate 메서드에 사용
    public Feed(FeedCreateDtoRequest requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        User user = new User();
        user.setUserId(requestDto.getUserId()); // User 객체의 ID 설정
        this.user = user;
    }

    public void feedUpdate(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
