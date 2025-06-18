package com.example.ordersystem.like.controller;

import com.example.ordersystem.like.dto.ArticleLikeResponse;
import com.example.ordersystem.like.service.ArticleLikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArticleLikeController.class)
@DisplayName("ArticleLikeController 테스트")
class ArticleLikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleLikeService articleLikeService;

    @Test
    @DisplayName("좋아요 조회 - 성공")
    void read_Success() throws Exception {
        // Given
        Long articleId = 1L;
        Long userId = 1L;
        Long articleLikeId = 12345L;
        LocalDateTime now = LocalDateTime.now();
        
        ArticleLikeResponse mockResponse = createMockArticleLikeResponse(articleLikeId, articleId, userId, now);
        
        given(articleLikeService.read(articleId, userId)).willReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/article-like/article/{articleId}/user/{userId}", articleId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articleLikeId").value(articleLikeId))
                .andExpect(jsonPath("$.articleId").value(articleId))
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.createdTime").exists())
                .andExpect(jsonPath("$.updatedTime").exists());
    }

    @Test
    @DisplayName("좋아요 생성 - 성공")
    void like_Success() throws Exception {
        // Given
        Long articleId = 1L;
        Long userId = 1L;
        Long expectedArticleLikeId = 12345L;
        
        given(articleLikeService.like(articleId, userId)).willReturn(expectedArticleLikeId);

        // When & Then
        mockMvc.perform(post("/article-like/article/{articleId}/user/{userId}", articleId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedArticleLikeId.toString()));
    }

    @Test
    @DisplayName("좋아요 취소 - 성공")
    void unlike_Success() throws Exception {
        // Given
        Long articleId = 1L;
        Long userId = 1L;
        
        willDoNothing().given(articleLikeService).unlike(articleId, userId);

        // When & Then
        mockMvc.perform(delete("/article-like/article/{articleId}/user/{userId}", articleId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("좋아요 조회 - 잘못된 articleId 형식")
    void read_InvalidArticleIdFormat() throws Exception {
        // Given
        String invalidArticleId = "invalid";
        Long userId = 1L;

        // When & Then
        mockMvc.perform(get("/article-like/article/{articleId}/user/{userId}", invalidArticleId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요 조회 - 잘못된 userId 형식")
    void read_InvalidUserIdFormat() throws Exception {
        // Given
        Long articleId = 1L;
        String invalidUserId = "invalid";

        // When & Then
        mockMvc.perform(get("/article-like/article/{articleId}/user/{userId}", articleId, invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요 생성 - 잘못된 articleId 형식")
    void like_InvalidArticleIdFormat() throws Exception {
        // Given
        String invalidArticleId = "invalid";
        Long userId = 1L;

        // When & Then
        mockMvc.perform(post("/article-like/article/{articleId}/user/{userId}", invalidArticleId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요 생성 - 잘못된 userId 형식")
    void like_InvalidUserIdFormat() throws Exception {
        // Given
        Long articleId = 1L;
        String invalidUserId = "invalid";

        // When & Then
        mockMvc.perform(post("/article-like/article/{articleId}/user/{userId}", articleId, invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요 취소 - 잘못된 articleId 형식")
    void unlike_InvalidArticleIdFormat() throws Exception {
        // Given
        String invalidArticleId = "invalid";
        Long userId = 1L;

        // When & Then
        mockMvc.perform(delete("/article-like/article/{articleId}/user/{userId}", invalidArticleId, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("좋아요 취소 - 잘못된 userId 형식")
    void unlike_InvalidUserIdFormat() throws Exception {
        // Given
        Long articleId = 1L;
        String invalidUserId = "invalid";

        // When & Then
        mockMvc.perform(delete("/article-like/article/{articleId}/user/{userId}", articleId, invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    /**
     * 테스트용 ArticleLikeResponse 객체 생성 헬퍼 메서드
     */
    private ArticleLikeResponse createMockArticleLikeResponse(Long articleLikeId, Long articleId, Long userId, LocalDateTime dateTime) {
        // ArticleLikeResponse가 정적 팩토리 메서드만 제공하므로, 
        // 실제 ArticleLike 엔티티를 만들어서 변환하거나, 리플렉션을 사용해야 합니다.
        // 여기서는 간단히 리플렉션으로 처리하겠습니다.
        ArticleLikeResponse response = new ArticleLikeResponse();
        try {
            setField(response, "articleLikeId", articleLikeId);
            setField(response, "articleId", articleId);
            setField(response, "userId", userId);
            setField(response, "createdTime", dateTime);
            setField(response, "updatedTime", dateTime);
        } catch (Exception e) {
            throw new RuntimeException("테스트 객체 생성 실패", e);
        }
        return response;
    }

    /**
     * 리플렉션을 사용해서 private 필드에 값을 설정하는 헬퍼 메서드
     */
    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
