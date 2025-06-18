package com.example.ordersystem.like.service;

import com.example.ordersystem.common.snowflake.Snowflake;
import com.example.ordersystem.like.dto.ArticleLikeResponse;
import com.example.ordersystem.like.entity.ArticleLike;
import com.example.ordersystem.like.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final Snowflake snowflake = new Snowflake();
    private final ArticleLikeRepository articleLikeRepository;


    public ArticleLikeResponse read(Long articleId,Long userId){
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }

    @Transactional
    public Long like(Long articleId,Long userId){
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(snowflake.nextId(), articleId, userId)
        );
        return articleLike.getArticleLikeId();

    }


    @Transactional
    public void unlike(Long articleId, Long userId){
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> articleLikeRepository.delete(articleLike))
        ;

    }
}
