package com.numo.domain.wordbook;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class WordCount {
    private int totalCount;
    private int memorizedCount;
    private int unMemorizedCount;

    public WordCount(int totalCount, int memorizedCount, int unMemorizedCount) {
        this.totalCount = totalCount;
        this.memorizedCount = memorizedCount;
        this.unMemorizedCount = unMemorizedCount;
    }

    /**
     * 단어 삭제 시 count 삭제
     */
    protected WordCount decrementCount(String memorization) {
        WordCountType type = getCountType(memorization);
        switch (type) {
            case memorized -> this.memorizedCount--;
            case unmemorized -> this.unMemorizedCount--;
        }
        this.totalCount--;

        return new WordCount(totalCount, memorizedCount, unMemorizedCount);
    }

    /**
     * 단어 암기 여부 수정 시 count 업데이트
     */
    protected WordCount updateCountByMemorizationStatus(String memorization) {
        WordCountType countType = getCountType(memorization);
        switch (countType) {
            case memorized -> {
                this.memorizedCount++;
                this.unMemorizedCount--;
            }
            case unmemorized -> {
                this.unMemorizedCount++;
                this.memorizedCount--;
            }
        }
        return new WordCount(totalCount, memorizedCount, unMemorizedCount);
    }

    /**
     * 단어 추가 시 count 추가
     */
    protected WordCount incrementCount(String memorization) {
        WordCountType type = getCountType(memorization);
        switch (type) {
            case memorized -> this.memorizedCount++;
            case unmemorized -> this.unMemorizedCount++;
        }
        this.totalCount++;
        return new WordCount(totalCount, memorizedCount, unMemorizedCount);
    }

    /**
     * 단어장에 단어가 있으면 삭제 불가
     */
    protected boolean isDeleteAllowed() {
        return totalCount <= 0;
    }

    private WordCountType getCountType(String memorization) {
        return switch (memorization) {
            case "Y" -> WordCountType.memorized;
            case "N" -> WordCountType.unmemorized;
            default -> throw new IllegalStateException("Unexpected value: " + memorization);
        };
    }
}
