package com.example.ordersystem.comment.service;

import com.example.ordersystem.comment.dto.req.CommentCreateRequest;
import com.example.ordersystem.comment.dto.res.CommentPageResponse;
import com.example.ordersystem.comment.dto.res.CommentResponse;
import com.example.ordersystem.comment.entity.Comment;
import com.example.ordersystem.comment.repository.CommentRepository;
import com.example.ordersystem.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final Snowflake snowflake = new Snowflake();

    public CommentResponse create(CommentCreateRequest request) {
        Comment parentComment = findParent(request);

        Comment save = commentRepository.save(Comment.createComment(
                snowflake.nextId(),
                request.getContent(),
                request.getArticleId(),
                parentComment == null ? null : parentComment.getCommentId(),
                request.getWriterId()));

        return CommentResponse.from(save);
    }

    private Comment findParent(CommentCreateRequest request) {
        if (request.getParentCommentId() == null) {
            return null;
        }
        return commentRepository.findById(request.getParentCommentId())
                .filter(not(Comment::isDeleted))
                .filter(Comment::isRoot)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
    }

    public CommentResponse read(Long commentId) {
        Comment comment = findById(commentId);
        return CommentResponse.from(comment);
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .filter(not(Comment::isDeleted))
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
    }

    //댓글 삭제
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(not(Comment::isDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)) {
                        comment.delete();
                    } else {
                        deleteComment(comment);

                    }
                });
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getCommentId(), 2L) == 2;

    }

    private void deleteComment(Comment comment) {
        commentRepository.delete(comment);
        if (!comment.isRoot()) {
            // 부모아이디로 찾고 필터로 삭제되지않고, 자식이 없는 댓글 찾아서 재귀적으로 삭제
            Comment parent = commentRepository.findById(comment.getParentCommentId())
                    .filter(not(Comment::isDeleted))
                    .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
            deleteComment(parent);
        }
    }

    public CommentPageResponse readPage(Long articleId, Long page, Long pageSize) {
        return CommentPageResponse.fromComments(
                commentRepository.findAllPage(articleId, page, pageSize),
                commentRepository.countBy(articleId, PageCalculator.calculatePage(page, pageSize, 10L))
        );
    }

    public List<CommentResponse> readAllInfiniteScroll(Long articleId,Long lastParentCommentId, Long lastCommentId, Long limit) {
        if (lastCommentId == null) {
            return commentRepository.findAllInfinite(articleId, limit).stream()
                    .map(CommentResponse::from)
                    .toList();
        } else {
            return commentRepository.findAllInfinite2(articleId, lastParentCommentId,lastCommentId, limit).stream()
                    .map(CommentResponse::from)
                    .toList();
        }
    }
}
