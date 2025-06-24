package com.example.ordersystem.like.service;

import com.example.common.util.TestUtil;
import com.example.ordersystem.common.snowflake.Snowflake;
import com.example.ordersystem.like.dto.ArticleLikeResponse;
import com.example.ordersystem.like.entity.ArticleLike;
import com.example.ordersystem.like.entity.ArticleLikeCount;
import com.example.ordersystem.like.repository.ArticleLikeCountRepository;
import com.example.ordersystem.like.repository.ArticleLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikeService {

    private final Snowflake snowflake = new Snowflake();
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;


    public ArticleLikeResponse read(Long articleId,Long userId){
        return articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .map(ArticleLikeResponse::from)
                .orElseThrow();
    }

    /**
     * //select 하지않고 바로 update
     *
     */
    @Transactional
    public Long likePessimisticLock1(Long articleId,Long userId){
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(snowflake.nextId(), articleId, userId)
        );




        //최초 요청 시에는
        int result = articleLikeCountRepository.increaseLikeCount(articleId);
        if(result == 0){
            articleLikeCountRepository.save(
                    ArticleLikeCount.create(articleId,1L)
            );
        }

        return articleLike.getArticleLikeId();

    }


    @Transactional
    public void unlikePessimisticLock1(Long articleId, Long userId){
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> articleLikeRepository.delete(articleLike))
        ;

    }

    /**
     * //select 하지않고 바로 update
     *
     */
    @Transactional
    public Long likeOpticLock1(Long articleId,Long userId){
        ArticleLike articleLike = articleLikeRepository.save(
                ArticleLike.create(snowflake.nextId(), articleId, userId)
        );
        return articleLike.getArticleLikeId();

    }


    @Transactional
    public void unlikeOpticLock1(Long articleId, Long userId){
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId)
                .ifPresent(articleLike -> articleLikeRepository.delete(articleLike))
        ;

    }
}
