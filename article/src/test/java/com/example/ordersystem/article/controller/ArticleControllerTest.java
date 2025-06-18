package com.example.ordersystem.article.controller;

import com.example.ordersystem.article.dto.request.ArticleCreatRequest;
import com.example.ordersystem.article.dto.request.ArticleUpdateRequest;
import com.example.ordersystem.article.dto.response.ArticleResponse;
import com.example.ordersystem.article.service.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

  @InjectMocks
  private ArticleController articleController;

  @Mock
  private ArticleService articleService;

  private ArticleResponse mockArticleResponse;
  private ArticleCreatRequest createRequest;
  private ArticleUpdateRequest updateRequest;

  @BeforeEach
  void setUp() {
    mockArticleResponse = new ArticleResponse();
    createRequest = new ArticleCreatRequest();
    updateRequest = new ArticleUpdateRequest();
  }

  @Test
  @DisplayName("게시글 조회 테스트")
  void readArticleTest() {
    // given
    Long articleId = 1L;
    when(articleService.read(articleId)).thenReturn(mockArticleResponse);

    // when
    ArticleResponse response = articleController.read(articleId);

    // then
    assertNotNull(response);
    verify(articleService, times(1)).read(articleId);
  }

  @Test
  @DisplayName("게시글 생성 테스트")
  void createArticleTest() {
    // given
    when(articleService.create(any(ArticleCreatRequest.class))).thenReturn(mockArticleResponse);

    // when
    ArticleResponse response = articleController.create(createRequest);

    // then
    assertNotNull(response);
    verify(articleService, times(1)).create(createRequest);
  }

  @Test
  @DisplayName("게시글 수정 테스트")
  void updateArticleTest() {
    // given
    Long articleId = 1L;
    when(articleService.update(anyLong(), any(ArticleUpdateRequest.class))).thenReturn(mockArticleResponse);

    // when
    ArticleResponse response = articleController.update(articleId, updateRequest);

    // then
    assertNotNull(response);
    verify(articleService, times(1)).update(articleId, updateRequest);
  }

  @Test
  @DisplayName("게시글 삭제 테스트")
  void deleteArticleTest() {
    // given
    Long articleId = 1L;
    doNothing().when(articleService).delete(articleId);

    // when
    articleController.delete(articleId);

    // then
    verify(articleService, times(1)).delete(articleId);
  }
}