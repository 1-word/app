package com.numo.batch.listener;

import com.numo.batch.word.DataShare;
import com.numo.domain.wordbook.word.Word;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WordItemWriteListener implements ItemWriteListener<Word>, ChunkListener {
    private final DataShare<Long> dataMap;

    @Override
    public void afterWrite(Chunk<? extends Word> items) {
        if (!items.isEmpty()) {
            Word lastWord = items.getItems().get(items.size() - 1);
            Long lastWordId = lastWord.getWordId();
            System.out.println(">>>>>>>>>>>>>> WordItemWriteListener - lastWordId: " + lastWordId);
            dataMap.putData("lastWordId", lastWordId);
        }
    }


}