package com.example.ordersystem.like.controller;

import com.example.ordersystem.like.dto.ArticleLikeResponse;
import com.example.ordersystem.like.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/article-like")
public class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @GetMapping("/article/{articleId}/user/{userId}")
    public ArticleLikeResponse read(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
     return articleLikeService.read(articleId, userId);
    }

//    @GetMapping("/article/{articleId}")
//    public ArticleLikeResponse read(@PathVariable Long articleId) {
//    }

    @PostMapping("/article/{articleId}/user/{userId}")
    public Long like(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        return articleLikeService.like(articleId, userId);
    }

    @DeleteMapping("/article/{articleId}/user/{userId}")
    public void unlike(@PathVariable("articleId") Long articleId, @PathVariable("userId") Long userId) {
        articleLikeService.unlike(articleId, userId);
    }

}
