package com.grizz.inventoryapp.inventory.repository.redis;

import com.grizz.inventoryapp.test.exception.NotImplementedTestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Import(InventoryRedisRepositoryImpl.class)
@Testcontainers
@DataRedisTest
public class InventoryRedisRepositoryTest {
    @Container
    private static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.2")
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @Autowired
    InventoryRedisRepository sut;

    @Autowired
    StringRedisTemplate redisTemplate;

    final String existingItemId = "1";
    final String nonExistingItemId = "2";
    final Long stock = 100L;

    @BeforeEach
    void setUp() {
        redisTemplate.opsForValue().set(sut.key(existingItemId), stock.toString());
    }

    @Nested
    class GetStock {
        @DisplayName("itemId를 갖는 inventory가 없다면, null을 반환한다")
        @Test
        void test1() {
            // when
            final Long result = sut.getStock(nonExistingItemId);

            // then
            assertNull(result);
        }

        @DisplayName("itemId를 갖는 inventory가 있다면, stock을 반환한다")
        @Test
        void test2() {
            // when
            final Long result = sut.getStock(existingItemId);

            // then
            assertEquals(stock, result);
        }
    }

    @Nested
    class DecreaseStock {
        final Long quantity = 10L;

        @DisplayName("itemId를 갖는 inventory가 없다면, inventory를 생성하고 stock을 차감하고 반환한다")
        @Test
        void test1() {
            // when
            final Long result = sut.decreaseStock(nonExistingItemId, quantity);

            // then
            assertEquals(-quantity, result);
        }

        @DisplayName("itemId를 갖는 inventory가 있다면, stock을 차감하고 반환한다")
        @Test
        void test2() {
            // when
            final Long result = sut.decreaseStock(existingItemId, quantity);

            // then
            assertEquals(stock - quantity, result);
        }
    }

    @Nested
    class DeleteStock {
        @DisplayName("itemId를 갖는 inventory가 없다면, false를 반환한다")
        @Test
        void test1() {
            throw new NotImplementedTestException();
        }

        @DisplayName("itemId를 갖는 inventory가 있다면, stock을 삭제하고 true를 반환한다")
        @Test
        void test2() {
            throw new NotImplementedTestException();
        }
    }

    @Nested
    class SetStock {
        @DisplayName("itemId를 갖는 inventory가 없다면, inventory를 생성하고 stock을 수정하고 반환한다")
        @Test
        void test1() {
            throw new NotImplementedTestException();
        }

        @DisplayName("itemId를 갖는 inventory가 있다면, stock을 수정하고 반환한다")
        @Test
        void test2() {
            throw new NotImplementedTestException();
        }
    }

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
