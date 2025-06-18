package com.example.ordersystem.comment.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentApiTest {

  RestClient restClient = RestClient.create("http://localhost:8080");

  @Test
  @DisplayName("댓글 생성 테스트")
  void testCreateComment() {
    // given

    CommentCreateRequest commentCreateRequest = new CommentCreateRequest();
    commentCreateRequest.setContent("테스트 댓글입니다.");
    commentCreateRequest.setArticleId(1L);
    commentCreateRequest.setParentCommentId(null); // 부모 댓글이 없는 경우
    commentCreateRequest.setWriterId(1L);

    CommentResponse response = restClient.post()
        .uri("/comments")
        .contentType(MediaType.APPLICATION_JSON)
        .body(commentCreateRequest)
        .retrieve()
        .body(CommentResponse.class);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getContent()).isEqualTo("테스트 댓글입니다.");
    assertThat(response.getArticleId()).isEqualTo(1L);
    assertThat(response.getWriterId()).isEqualTo(1L);
    assertThat(response.isDeleted()).isFalse();
  }

  @Test
  @DisplayName("댓글 조회 테스트")
  void testReadComment() {
    // given
    Long commentId = 191018540892160000L;

    // when
    CommentResponse response = restClient.get()
        .uri("/comments/{commentId}", commentId)
        .retrieve()
        .body(CommentResponse.class);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getCommentId()).isEqualTo(commentId);
  }

  @Test
  @DisplayName("댓글 삭제 테스트")
  void testDeleteComment() {
    // given
    Long commentId = 191018540892160000L;

    // when & then
    restClient.delete()
        .uri("/comments/{commentId}", commentId)
        .retrieve()
        .toBodilessEntity();
  }

  @Test
  @DisplayName("대댓글 생성 테스트")
  void testCreateReply() {
    // given
    CommentCreateRequest request = new CommentCreateRequest(
        "테스트 대댓글입니다.",
        1L, // articleId
        191018810866925568L, // parentCommentId (부모 댓글 ID)
        2L // writerId
    );

    // when
    CommentResponse response = restClient.post()
        .uri("/comments")
        .contentType(MediaType.APPLICATION_JSON)
        .body(request)
        .retrieve()
        .body(CommentResponse.class);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getContent()).isEqualTo("테스트 대댓글입니다.");
    assertThat(response.getArticleId()).isEqualTo(1L);
    assertThat(response.getParentCommentId()).isEqualTo(191018810866925568L);
    assertThat(response.getWriterId()).isEqualTo(2L);
    assertThat(response.isDeleted()).isFalse();
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  @Setter
  public static class CommentCreateRequest {
    private String content;
    private Long articleId;
    private Long parentCommentId;
    private Long writerId;
  }

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CommentResponse {
    private Long commentId;
    private String content;
    private Long articleId;
    private Long parentCommentId;
    private Long writerId;
    private boolean deleted;
  }
}
