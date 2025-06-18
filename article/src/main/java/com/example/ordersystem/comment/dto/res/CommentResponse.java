package com.example.ordersystem.comment.dto.res;

import com.example.ordersystem.comment.entity.Comment;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponse {
  private Long commentId;
  private String content;
  private Long articleId;
  private Long parentCommentId;
  private Long writerId;
  private boolean deleted;

  public static CommentResponse from(Comment comment) {
    CommentResponse response = new CommentResponse();
    response.commentId = comment.getCommentId();
    response.content = comment.getContent();
    response.articleId = comment.getArticleId();
    response.parentCommentId = comment.getParentCommentId();
    response.writerId = comment.getWriterId();
    response.deleted = comment.isDeleted();
    return response;
  }
}
