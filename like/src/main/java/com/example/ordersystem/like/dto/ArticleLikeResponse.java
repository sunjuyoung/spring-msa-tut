package com.example.ordersystem.like.dto;

import com.example.ordersystem.like.entity.ArticleLike;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleLikeResponse {

    private Long articleLikeId;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static ArticleLikeResponse from(ArticleLike articleLike) {
        ArticleLikeResponse response = new ArticleLikeResponse();
        response.articleLikeId = articleLike.getArticleLikeId();
        response.articleId = articleLike.getArticleId();
        response.userId = articleLike.getUserId();
        response.createdTime = articleLike.getCreatedTime();
        response.updatedTime = articleLike.getUpdatedTime();
        return response;
    }





}
