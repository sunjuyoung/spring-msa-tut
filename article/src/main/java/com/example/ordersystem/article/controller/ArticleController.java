package com.example.ordersystem.article.controller;

import com.example.ordersystem.article.dto.request.ArticleCreatRequest;
import com.example.ordersystem.article.dto.request.ArticleUpdateRequest;
import com.example.ordersystem.article.dto.response.ArticlePageResponse;
import com.example.ordersystem.article.dto.response.ArticleResponse;
import com.example.ordersystem.article.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;


    @GetMapping("/infinite-scroll")
    public List<ArticleResponse> readInfiniteScroll(@RequestParam Long productId,
                                                @RequestParam(value = "lastArticleId",required = false) Long lastArticleId,
                                                @RequestParam Long pageSize) {
        return articleService.readAllInfiniteScroll(productId, lastArticleId, pageSize);
    }

    /**
     * 게시글 페이지 조회
     * @param productId
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping
    public ArticlePageResponse readPage(@RequestParam Long productId,
                                        @RequestParam Long page, @RequestParam Long pageSize){
        return articleService.readAll(productId, page, pageSize);
    }

    /**
     * 게시글 상세 조회
     * @param articleId
     * @return
     */
    @GetMapping("/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId){
        return articleService.read(articleId);
    }

    /**
     * 게시글 생성
     * @param request
     * @return
     */
    @PostMapping
    public ArticleResponse create(@RequestBody ArticleCreatRequest request){
        return articleService.create(request);
    }

    /**
     * 게시글 수정
     * @param articleId
     * @param request
     * @return
     */
    @PutMapping("/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request){
        return articleService.update(articleId,request);
    }

    /**
     * 게시글 삭제
     * @param articleId
     */
    @DeleteMapping("/{articleId}")
    public void delete(@PathVariable Long articleId){
        articleService.delete(articleId);
    }
}
