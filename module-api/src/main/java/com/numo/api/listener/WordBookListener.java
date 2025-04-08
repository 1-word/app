package com.numo.api.listener;

import com.numo.api.domain.wordbook.service.WordBookService;
import com.numo.api.listener.event.WordBookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class WordBookListener {
    private final WordBookService wordBookService;

    /**
     * 이전 단어장의 count를 동기화
     * @param event 단어장 정보
     */
    @EventListener
    @TransactionalEventListener
    public void decrementPreviousWordBookCount(WordBookEvent event) {
        wordBookService.decrementPreviousWordBookCount(event.id(), event.memorization());
    }
}
