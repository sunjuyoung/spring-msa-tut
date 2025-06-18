package com.example.ordersystem.comment.service;

import com.example.ordersystem.comment.entity.Comment;
import com.example.ordersystem.comment.repository.CommentRepository;
import com.example.ordersystem.common.snowflake.Snowflake;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

  @InjectMocks
  private CommentService commentService;

  @Mock
  private CommentRepository commentRepository;

  @Mock
  private Snowflake snowflake;

  private Comment comment;
  private Long commentId;
  private Long articleId;
  private Long writerId;

  @BeforeEach
  void setUp() {
    commentId = 1L;
    articleId = 1L;
    writerId = 1L;
    comment = Comment.createComment(commentId, "test content", articleId, commentId, writerId);
  }

  @Test
  @DisplayName("자식 댓글이 없는 경우 실제로 삭제되어야 한다")
  void deleteCommentWithoutChildren() {
    // given
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
    when(commentRepository.countBy(articleId, commentId, 2L)).thenReturn(1L);

    // when
    commentService.delete(commentId);

    // then
    verify(commentRepository).delete(comment);
  }

  @Test
  @DisplayName("자식 댓글이 있는 경우 soft delete가 되어야 한다")
  void deleteCommentWithChildren() {
    // given
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
    when(commentRepository.countBy(articleId, commentId, 2L)).thenReturn(2L);

    // when
    commentService.delete(commentId);

    // then
    verify(commentRepository, never()).delete(any());
    assertTrue(comment.isDeleted());
  }

  @Test
  @DisplayName("이미 삭제된 댓글은 아무 동작도 하지 않아야 한다")
  void deleteAlreadyDeletedComment() {
    // given
    comment.delete();
    when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

    // when
    commentService.delete(commentId);

    // then
    verify(commentRepository, never()).delete(any());
  }

  @Test
  @DisplayName("존재하지 않는 댓글은 아무 동작도 하지 않아야 한다")
  void deleteNonExistentComment() {
    // given
    when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

    // when
    commentService.delete(commentId);

    // then
    verify(commentRepository, never()).delete(any());
  }
}