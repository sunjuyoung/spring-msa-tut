package com.example.ordersystem.article.data;

import com.example.ordersystem.article.entity.Article;
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

    static final int BATCH_SIZE = 1000;
    static final int TOTAL_RECORDS = 1_000_000;
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
                    for (int i = 0; i < BATCH_SIZE; i++) {
                        entityManager.persist(createRandomArticle());

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

    private Article createRandomArticle() {
        return Article.create(
                snowflake.nextId(),
                generateRandomTitle(),
                generateRandomContent(),
                1L,
                1L);
    }

    private String generateRandomTitle() {
        String[] prefixes = { "Important", "Notice", "Update", "News", "Info" };
        String[] topics = { "System", "Service", "Product", "Event", "Meeting" };
        return String.format("%s: %s %d",
                prefixes[random.nextInt(prefixes.length)],
                topics[random.nextInt(topics.length)],
                random.nextInt(1000));
    }

    private String generateRandomContent() {
        String[] templates = {
                "This is an important update regarding %s. Please review the following information carefully.",
                "We would like to inform you about recent changes in %s. Details are as follows:",
                "Important announcement about %s. Please take note of the following:"
        };
        String[] subjects = { "the system", "our services", "the product", "upcoming events", "maintenance" };

        return String.format(templates[random.nextInt(templates.length)],
                subjects[random.nextInt(subjects.length)]);
    }
}