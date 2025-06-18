package com.example.ordersystem.article.service;

import com.example.ordersystem.article.dto.request.ArticleCreatRequest;
import com.example.ordersystem.article.dto.request.ArticleUpdateRequest;
import com.example.ordersystem.article.dto.response.ArticlePageResponse;
import com.example.ordersystem.article.dto.response.ArticleResponse;
import com.example.ordersystem.article.entity.Article;
import com.example.ordersystem.article.repository.ArticleRepository;
import com.example.ordersystem.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final Snowflake snowflake = new Snowflake();
    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleResponse create(ArticleCreatRequest request) {

        Article article = Article.create(
                snowflake.nextId(),
                request.getTitle(),
                request.getContent(),
                request.getProductId(),
                request.getWriterId());
        Article saveArticle = articleRepository.save(article);

        return ArticleResponse.from(saveArticle);

    }

    @Transactional
    public ArticleResponse update(Long articleId, ArticleUpdateRequest request) {

        Article article = articleRepository.findById(articleId).orElseThrow();
        article.update(request.getTitle(), request.getContent());

        return ArticleResponse.from(article);
    }

    public ArticleResponse read(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        return ArticleResponse.from(article);
    }

    @Transactional
    public void delete(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();
        articleRepository.delete(article);

    }

    public ArticlePageResponse readAll(Long productId, Long page, Long pageSize) {
        return ArticlePageResponse.from(articleRepository.findAll(productId, (page-1)*pageSize, pageSize),
                articleRepository.count(productId, PageCalculator.calculatePage(page, pageSize, 10L)));
    }

    public List<ArticleResponse> readAllInfiniteScroll(Long productId,Long lastArticleId, Long pageSize) {
        if (lastArticleId == null) {
            return articleRepository.findAllInfiniteScroll(productId, pageSize).stream()
                    .map(ArticleResponse::from)
                    .toList();
        } else {
            return articleRepository.findAllInfiniteScroll(productId, lastArticleId, pageSize).stream()
                    .map(ArticleResponse::from)
                    .toList();
        }
    }
}
