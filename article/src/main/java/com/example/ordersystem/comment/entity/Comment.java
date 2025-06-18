package com.example.ordersystem.comment.entity;

import com.example.ordersystem.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

  @Id
  @Column(name = "comment_id")
  private Long commentId;

  @Column(name = "content", nullable = false, length = 3000)
  private String content;

  @Column(name = "article_id", nullable = false)
  private Long articleId;

  @Column(name = "parent_comment_id", nullable = false)
  private Long parentCommentId;

  @Column(name = "writer_id", nullable = false)
  private Long writerId;

  @Column(name = "deleted", nullable = false)
  private boolean deleted;

  public static Comment createComment(Long commentId, String content, Long articleId,
      Long parentCommentId, Long writerId) {
    Comment comment = new Comment();
    comment.commentId = commentId;
    comment.content = content;
    comment.articleId = articleId;
    comment.parentCommentId = parentCommentId == null ? commentId : parentCommentId;
    comment.writerId = writerId;
    comment.deleted = false;
    return comment;
  }

  public boolean isRoot() {
    return parentCommentId.longValue() == commentId.longValue();
  }

  public void delete() {
    this.deleted = true;
  }

}
