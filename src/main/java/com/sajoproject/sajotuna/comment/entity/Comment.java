package com.sajoproject.sajotuna.comment.entity;

import com.sajoproject.sajotuna.comment.dto.commentUpdateDto.CommentUpdateRequestDto;
import com.sajoproject.sajotuna.comment.dto.postCommentDto.PostCommentDtoRequest;
import com.sajoproject.sajotuna.feed.entity.Feed;
import com.sajoproject.sajotuna.common.Timestamped;
import com.sajoproject.sajotuna.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "replyComment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> replies = new ArrayList<>();

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_comment_id")
    private Comment replyComment;

    public Comment(PostCommentDtoRequest reqDto, Comment replyComment){
        this.content=reqDto.getContent();
        Feed newfeed = new Feed();
        newfeed.setFeedId(reqDto.getFeedId());
        User newuser = new User();
        newuser.setUserId(reqDto.getUserId());

        this.feed=newfeed;
        this.user=newuser;
        this.replyComment = replyComment;
    }

    public void update(CommentUpdateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
