package com.sajoproject.sajotuna.comment.entity;


import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateRequestDto;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoRequest;
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
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Comment(PostCommentDtoRequest reqDto){
        this.content=reqDto.getContent();
        Feed newfeed = new Feed();
        newfeed.setFeedId(reqDto.getFeedId());
        User newuser = new User();
        newuser.setUserId(reqDto.getUserId());
        this.user=newuser;
        this.feed=newfeed;
    }

    public void update(CommentUpdateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
