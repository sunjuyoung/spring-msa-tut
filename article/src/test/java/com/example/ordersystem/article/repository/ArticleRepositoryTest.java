package com.example.ordersystem.article.repository;

import com.example.ordersystem.article.entity.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ArticleRepositoryTest {
  @Autowired
  ArticleRepository articleRepository;

  @Test
  @DisplayName("Article 저장 및 조회 테스트")
  void saveAndFindById() {
    // given
    Article article = Article.create(1L, "title", "content", 100L, 200L);
    articleRepository.save(article);

    // when
    Article found = articleRepository.findById(1L).orElse(null);

    // then
    assertThat(found).isNotNull();
    assertThat(found.getTitle()).isEqualTo("title");
    assertThat(found.getProductId()).isEqualTo(100L);
  }

  @Test
  @DisplayName("findAll 커스텀 쿼리 테스트")
  void findAllCustomQuery() {
    // given

    // when
    List<Article> articles = articleRepository.findAll(1L, 0L, 3L);

    // then
    assertThat(articles).hasSize(3);

  }

  @Test
  @DisplayName("count 커스텀 쿼리 테스트")
  void countCustomQuery() {
    // given
    for (long i = 1; i <= 5; i++) {
      articleRepository.save(Article.create(i, "title" + i, "content" + i, 100L, 200L));
    }

    // when
    Long count = articleRepository.count(100L, 5L);

    // then
    assertThat(count).isEqualTo(5L);
  }

  @Test
  @DisplayName("findAllInfiniteScroll 첫 페이지 조회 테스트")
  void findAllInfiniteScrollFirstPage() {
    // given

    // when
    List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 3L);

    // then
    assertThat(articles).hasSize(3);

  }

  @Test
  @DisplayName("findAllInfiniteScroll 두 번째 페이지 조회 테스트")
  void findAllInfiniteScrollSecondPage() {
    // given

    // when
    List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 183102887002816560L, 3L);

    // then
    assertThat(articles).hasSize(3);
    assertThat(articles.get(0).getArticleId()).isLessThan(183102887002816560L);
  }
}