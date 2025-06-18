package com.example.ordersystem.article.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArticleCreatRequest {
    private String title;
    private String content;
    private Long writerId;
    private Long productId;
}
