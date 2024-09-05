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

//    @ManyToOne (fetch = FetchType.LAZY)
//    @JoinColumn(name = "parent_comment_id")
//    private Comment parentComment;
//
//    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> childComments = new ArrayList<>();
//
//    public Comment(PostCommentDtoRequest requestDto, User user, Feed feed) {
//        this.content = requestDto.getContent();
//        this.feed = feed;
//        this.user = user;
//    }
//
//    public Comment(PostCommentDtoRequest requestDto, User  user, Feed feed,Comment parentComment) {
//        this.content = requestDto.getContent();
//        this.feed = feed;
//        this.user = user;
//        this.parentComment = parentComment;
//    }

    // 이부분 확인
    public Comment(PostCommentDtoRequest reqDto){
        this.content=reqDto.getContent();

        Feed newfeed = new Feed();
        newfeed.setFeedId(reqDto.getFeedId());
        User newuser = new User();
        newuser.setUserId(reqDto.getUserId());

        this.feed=newfeed;
        this.user=newuser;
    }

    public void update(CommentUpdateRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}
