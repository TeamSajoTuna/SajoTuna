package com.sajoproject.sajotuna.feed.dto.feedPagingDto;

import com.sajoproject.sajotuna.comment.entity.Comment;
import com.sajoproject.sajotuna.feed.entity.Feed;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class FeedPagingDtoResponse {
    private Long feedId;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
//    private List<Comment> commentList;

    public FeedPagingDtoResponse(Feed feed){
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.feedId=feed.getFeedId();
        this.createdAt=feed.getCreatedAt();
        this.modifiedAt=feed.getModifiedAt();
//        List<Comment> comments = new ArrayList<>();
//        comments=feed.getComment();
//        this.commentList=comments;
    }


}
