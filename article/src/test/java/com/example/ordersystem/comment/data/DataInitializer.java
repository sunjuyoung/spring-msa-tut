package com.example.ordersystem.comment.data;

import com.example.ordersystem.comment.entity.Comment;
import com.example.ordersystem.common.snowflake.Snowflake;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@SpringBootTest
public class DataInitializer {

  @PersistenceContext
  EntityManager entityManager;

  @Autowired
  TransactionTemplate transactionTemplate;

  private final Snowflake snowflake = new Snowflake();
  private final Random random = new Random();

  static final int BATCH_SIZE = 100;
  static final int TOTAL_RECORDS = 100_000;
  static final int THREAD_POOL_SIZE = 10;

  @Test
  void initialize() {
    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    CountDownLatch latch = new CountDownLatch(THREAD_POOL_SIZE);
    List<Future<?>> futures = new ArrayList<>();

    try {
      for (int i = 0; i < THREAD_POOL_SIZE; i++) {
        futures.add(executorService.submit(() -> {
          try {
            insertBatch();
          } catch (Exception e) {
            System.err.println("Error during batch insert: " + e.getMessage());
          } finally {
            latch.countDown();
          }
        }));
      }

      latch.await();

      // Check for any exceptions in the futures
      for (Future<?> future : futures) {
        try {
          future.get();
        } catch (Exception e) {
          System.err.println("Error in future: " + e.getMessage());
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Initialization interrupted: " + e.getMessage());
    } finally {
      executorService.shutdown();
      try {
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
          executorService.shutdownNow();
        }
      } catch (InterruptedException e) {
        executorService.shutdownNow();
        Thread.currentThread().interrupt();
      }
    }
  }

  private void insertBatch() {
    int recordsPerThread = TOTAL_RECORDS / THREAD_POOL_SIZE;
    int batchCount = recordsPerThread / BATCH_SIZE;

    for (int batch = 0; batch < batchCount; batch++) {
      transactionTemplate.executeWithoutResult(transactionStatus -> {
        try {
          Comment prev = null;
          for (int i = 0; i < BATCH_SIZE; i++) {

            Comment comment = Comment.createComment(
                    snowflake.nextId(),
                    generateRandomContent(),
                    random.nextLong(183102528188497920L, 183102528188497930L), // articleId 범위 수정
                    i % 2 ==0 ? null : prev.getCommentId(), // parentCommentId (50% chance of being root comment)
                    random.nextLong(1, 5));// writerId

      prev = comment;

            entityManager.persist(comment);

            if (i % 100 == 0) {
              entityManager.flush();
              entityManager.clear();
            }
          }
        } catch (Exception e) {
          transactionStatus.setRollbackOnly();
          throw e;
        }
      });
    }
  }

  private Comment createRandomComment() {
    Long commentId = snowflake.nextId();
    return Comment.createComment(
        commentId,
        generateRandomContent(),
        random.nextLong(183102528188497920L, 183102528188497930L), // articleId 범위 수정
        random.nextBoolean() ? commentId : random.nextLong(1, 1000), // parentCommentId (50% chance of being root
                                                                     // comment)
        random.nextLong(1, 5)); // writerId
  }

  private String generateRandomContent() {
    String[] templates = {
        "이 글에 대한 의견: %s",
        "좋은 글이네요! %s",
        "추가로 말씀드리자면, %s",
        "이런 관점도 있네요: %s"
    };
    String[] subjects = {
        "정말 유익한 정보였습니다.",
        "더 자세한 설명이 있었으면 좋겠습니다.",
        "이 부분은 조금 더 생각해볼 필요가 있을 것 같습니다.",
        "전반적으로 동의합니다.",
        "좋은 의견 감사합니다."
    };

    return String.format(templates[random.nextInt(templates.length)],
        subjects[random.nextInt(subjects.length)]);
  }
}
