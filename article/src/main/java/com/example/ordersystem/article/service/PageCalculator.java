package com.example.ordersystem.article.service;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class PageCalculator {

    public static Long calculatePage(Long page, Long pageSize, Long moveablePageCount){
        return (((page -1)/ moveablePageCount) +1) * pageSize * moveablePageCount + 1;
    }
}
