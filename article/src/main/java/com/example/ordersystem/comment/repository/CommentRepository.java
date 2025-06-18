package com.example.ordersystem.comment.repository;

import com.example.ordersystem.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query(value = "select count(*) from " +
            " (select comment_id from comment " +
            " where article_id = :articleId and parent_comment_id = :parentCommentId limit :limit) t",
            nativeQuery = true
            )
    Long countBy(Long articleId,Long parentCommentId, Long limit);



    @Query(value = "select c.comment_id, c.content, c.article_id, c.parent_comment_id, c.writer_id, c.deleted,c.created_time, c.updated_time  " +
            " from ( " +
            " select comment_id from comment " +
            " where article_id = :articleId " +
            " order by parent_comment_id asc , comment_id asc  limit :limit offset :offset ) t " +
            " left join comment c on t.comment_id = c.comment_id", nativeQuery = true)
    List<Comment> findAllPage(@Param("articleId") Long articleId,
                                   @Param("offset") Long offset,
                                   @Param("limit") Long limit);


    @Query(value = "select count(*) from " +
            " (select comment_id from comment " +
            " where article_id = :articleId  limit :limit) t",
            nativeQuery = true
    )
    Long countBy(@Param("articleId") Long articleId, @Param("limit") Long limit);



    @Query(value = "select c.comment_id, c.content, c.article_id, c.parent_comment_id, c.writer_id, c.deleted,c.created_time,c.updated_time " +
            " from  comment c  " +
            " where c.article_id = :articleId  " +
            " order by c.parent_comment_id asc,  c.comment_id asc limit :limit  " ,

            nativeQuery = true)
    List<Comment> findAllInfinite(@Param("articleId") Long articleId,
                                   @Param("limit") Long limit);


    @Query(value = "select c.comment_id, c.content, c.article_id, c.parent_comment_id, c.writer_id, c.deleted, c.created_time, c.updated_time " +
            " from comment c " +
            " where c.article_id = :articleId and c.parent_comment_id > :lastParentCommentId  or " +
            " ( c.parent_comment_id = :lastParentCommentId and c.comment_id > :lastCommentId )" +
            " order by c.parent_comment_id asc,  c.comment_id asc limit :limit  "

            , nativeQuery = true)
    List<Comment> findAllInfinite2(@Param("articleId") Long articleId,
                          @Param("lastParentCommentId") Long lastParentCommentId,
                          @Param("lastCommentId") Long lastCommentId,
                          @Param("limit") Long limit);

}
