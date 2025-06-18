package com.example.ordersystem.comment.controller;

import com.example.ordersystem.comment.dto.req.CommentCreateRequest;
import com.example.ordersystem.comment.dto.res.CommentPageResponse;
import com.example.ordersystem.comment.dto.res.CommentResponse;
import com.example.ordersystem.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

  private final CommentService commentService;

  @PostMapping
  public CommentResponse create(@RequestBody CommentCreateRequest request) {
    return commentService.create(request);
  }

  @GetMapping("/{commentId}")
  public CommentResponse read(@PathVariable Long commentId) {
    return commentService.read(commentId);
  }

  @DeleteMapping("/{commentId}")
  public void delete(@PathVariable Long commentId) {
    commentService.delete(commentId);
  }


  //pageReadAll
  @GetMapping
    public CommentPageResponse readAll(@RequestParam Long articleId,
                                       @RequestParam Long page,
                                       @RequestParam Long pageSize) {
        return commentService.readPage(articleId, page, pageSize);
    }

    //infiniteScrollReadAll
    @GetMapping("/infinite-scroll")
    public List<CommentResponse> readInfiniteScroll(@RequestParam Long articleId,
                                                    @RequestParam(value = "lastParentCommentId", required = false) Long lastParentCommentId,
                                                    @RequestParam(value = "lastCommentId", required = false) Long lastCommentId,
                                                    @RequestParam Long pageSize) {
        return commentService.readAllInfiniteScroll(articleId, lastParentCommentId,lastCommentId, pageSize);
    }
}
