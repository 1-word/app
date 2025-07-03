package com.numo.batch.word;

import com.numo.domain.wordbook.word.Word;

import java.util.ArrayList;
import java.util.List;

public class WordCopyService {
    private long currentWordDetailId = 0L;
    private int wordDetailSize = 0;
    private final Long userId;
    private final Long wordBookId;

    public WordCopyService(Long userId, Long wordBookId) {
        this.userId = userId;
        this.wordBookId = wordBookId;
    }

    public List<Word> getCopyWords(long startWordId, List<Word> words) {
        checkData(startWordId);

        List<Word> copyWords = new ArrayList<>();

        for (Word word : words) {
            Word copyword = word.copyWithWordBook(startWordId++, currentWordDetailId, userId, wordBookId);
            copyWords.add(copyword);
            int size = copyword.getWordDetails().size();
            wordDetailSize += size;
            if (size > 0) {
                currentWordDetailId = copyword.getWordDetails().get(size - 1).getWordDetailId() + 1;
            }

        }

        return copyWords;
    }

    private void checkData(long wordId) {
        if (wordId <= 0L) {
            throw new IllegalArgumentException("wordId must be greater than 0");
        }
    }

    public void setCurrentWordDetailId(long currentWordDetailId) {
        this.currentWordDetailId = currentWordDetailId;
    }

    public int getWordDetailSize() {
        return wordDetailSize;
    }
}
