package com.example.ordersystem.like.repository;

import com.example.ordersystem.like.entity.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike,Long> {

    Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);

    Optional<ArticleLike> findByArticleId(Long articleId);
}
