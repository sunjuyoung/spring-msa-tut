package com.example.ordersystem.comment.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
  private String content;
  private Long articleId;
  private Long parentCommentId;
  private Long writerId;
}
