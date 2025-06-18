package com.example.ordersystem.article.dto.response;

import com.example.ordersystem.article.entity.Article;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleResponse {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId; //shard key
    private Long writerId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public static ArticleResponse from(Article article){
        ArticleResponse response = new ArticleResponse();
        response.articleId = article.getArticleId();
        response.boardId = article.getProductId();
        response.title = article.getTitle();
        response.content = article.getContent();
        response.writerId = article.getWriterId();
        response.createdTime = article.getCreatedTime();
        response.updatedTime = article.getUpdatedTime();
        return response;
    }

    }