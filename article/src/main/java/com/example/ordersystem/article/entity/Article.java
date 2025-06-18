package com.example.ordersystem.article.entity;

import com.example.ordersystem.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Table(name = "article")
@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article extends BaseTimeEntity {

    @Id
    private Long articleId;
    private String title;
    private String content;
    private Long productId;
    private Long writerId;

    public static Article create(Long articleId, String title, String content, Long productId, Long writerId) {
        Article article = new Article();
        article.articleId = articleId;
        article.title = title;
        article.content = content;
        article.productId = productId;
        article.writerId = writerId;
        return article;
    }

    public void update(String title, String content) {
        this.content = content;
        this.title = title;
    }
}
