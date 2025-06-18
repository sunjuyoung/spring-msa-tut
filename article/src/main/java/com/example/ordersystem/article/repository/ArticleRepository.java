package com.example.ordersystem.article.repository;

import com.example.ordersystem.article.entity.Article;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

        @Query(value = " select a.article_id, a.title, a.content,a.product_id,a.writer_id,a.created_time,a.updated_time " +
                        " from ( " +
                        " select article_id " +
                        " from article " +
                        " where product_id = :productId " +
                        " order by article_id desc " +
                        " limit :limit offset :offset) t " +
                        " left join article a on t.article_id = a.article_id", nativeQuery = true)
        List<Article> findAll(@Param("productId") Long productId,
                        @Param("offset") Long offset,
                        @Param("limit") Long limit);



        @Query(
                value="select count(*) from(" +
                        " select article_id " +
                        " from article " +
                        " where product_id = :productId " +
                        " order by article_id desc " +
                        " limit :limit) t",
                nativeQuery = true
        )
        Long count(@Param("productId") Long productId,
                        @Param("limit") Long limit);


        //무한스크롤 1번 페이지처리
        @Query(value = " select a.article_id, a.title, a.content,a.product_id,a.writer_id,a.created_time,a.updated_time " +
                        " from article a"+
                        " where a.product_id = :productId" +
                        " order by a.article_id desc  limit :limit"
                , nativeQuery = true)
        List<Article> findAllInfiniteScroll(@Param("productId") Long productId,
                        @Param("limit") Long limit);

        //무한스크롤 2번페이지부터 기준점 추가
        @Query(value = " select a.article_id, a.title, a.content,a.product_id,a.writer_id,a.created_time,a.updated_time " +
                " from article a"+
                " where a.product_id = :productId and a.article_id < :articleId" +
                " order by a.article_id desc  limit :limit"
                , nativeQuery = true)
        List<Article> findAllInfiniteScroll(@Param("productId") Long productId,
                        @Param("articleId") Long articleId,
                                            @Param("limit") Long limit);
}

