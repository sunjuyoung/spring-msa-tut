package com.example.ordersystem.comment.dto.res;

import com.example.ordersystem.comment.entity.Comment;
import lombok.Getter;

import java.util.List;

@Getter
public class CommentPageResponse {

    private List<CommentResponse> comments;
    private Long commentCount;

    public static CommentPageResponse fromComments(List<Comment> comments, Long commentCount) {
        CommentPageResponse response = new CommentPageResponse();
        response.comments = comments.stream()
                .map(CommentResponse::from)
                .toList();
        response.commentCount = commentCount;
        return response;

    }
}
