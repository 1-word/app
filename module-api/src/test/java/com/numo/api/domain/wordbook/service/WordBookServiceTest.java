package com.numo.api.domain.wordbook.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WordBookServiceTest {

    @Autowired
    WordBookService wordBookService;

    Long userId = 37L;
    Long wordBookId = 66L;

    @BeforeEach
    void setUp() {
        // 캐시 등록
//        System.out.println("캐시 등록 start");
//        wordBookService.getWordBook(wordBookId);
//        System.out.println("캐시 등록 end");
    }

    @DisplayName("start")
    @Test
    @Order(1)
    void start() {

    }

    @DisplayName("반정규화한 카운트 조회")
    @Test
    @Order(2)
    void getWordBooks() {
        wordBookService.getWordBooks(userId);
    }

    @DisplayName("join 쿼리를 이용하여 조회")
    @Test
    @Order(3)
    void getWordBooksCount() {
        wordBookService.getWordBooksCount(userId);
    }

    @DisplayName("카운트와 단어장 따로 조회 후 조립")
    @Test
    @Order(4)
    void getWordBooksGrouping() {
        wordBookService.getWordBooksGrouping(userId);
    }
//
//    @DisplayName("단어장 조회 - 캐시 미 사용")
//    @Test
//    @Order(3)
//    void getWordBook_NoCache() {
//        long start = System.currentTimeMillis();
//        wordBookService.getWordBook_NoCache(wordBookId);
//        long end = System.currentTimeMillis();
//
//        System.out.println(end - start);
//    }
//
//    @DisplayName("단어장 조회 - 캐시 사용")
//    @Test
//    @Order(2)
//    void getWordBook_UseCache() {
//        long start = System.currentTimeMillis();
//        wordBookService.getWordBook(wordBookId);
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
//    }
}