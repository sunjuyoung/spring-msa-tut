package com.example.ordersystem.article.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ArticleUpdateRequest {
    private String title;
    private String content;
}