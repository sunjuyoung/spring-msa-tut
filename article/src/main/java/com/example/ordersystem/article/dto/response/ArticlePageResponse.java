package com.example.ordersystem.article.dto.response;

import com.example.ordersystem.article.entity.Article;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class ArticlePageResponse {

    private List<ArticleResponse> articles;
    private Long articleCount;

    public static ArticlePageResponse from(List<Article> articles, Long articleCount) {
        ArticlePageResponse response = new ArticlePageResponse();
        response.articles = articles.stream()
                .map(ArticleResponse::from)
                .toList();
        response.articleCount = articleCount;
        return response;

    }
}