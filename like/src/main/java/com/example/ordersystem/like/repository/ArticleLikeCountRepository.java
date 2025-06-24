package com.example.ordersystem.like.repository;

import com.example.ordersystem.like.entity.ArticleLikeCount;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long> {



    //select .. for update
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<ArticleLikeCount> findLockedByArticleId(Long articleId);


    @Query(value = "update article_like_count set like_count = like_count + 1 where article_id = :articleId",
            nativeQuery = true)
    @Modifying
    int increaseLikeCount(@Param("articleId") Long articleId);

    @Query(value = "update article_like_count set like_count = like_count - 1 where article_id = :articleId",
            nativeQuery = true)
    @Modifying
    int decreaseLikeCount(@Param("articleId") Long articleId);
}
