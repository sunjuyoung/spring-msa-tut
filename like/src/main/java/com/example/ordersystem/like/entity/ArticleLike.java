package com.example.ordersystem.like.entity;

import com.example.ordersystem.common.domain.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "article_like")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike extends BaseTimeEntity {


    @Id
    private Long articleLikeId;

    private Long articleId;
    private Long userId;

    public static ArticleLike create(Long articleLikeId, Long articleId, Long userId) {
        ArticleLike articleLike = new ArticleLike();
        articleLike.articleLikeId = articleLikeId;
        articleLike.articleId = articleId;
        articleLike.userId = userId;
        return articleLike;
    }

}
