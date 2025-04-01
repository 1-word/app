package com.numo.api.domain.wordbook.word.service.update;

import com.numo.domain.wordbook.type.UpdateType;

public class UpdateFactory {

    public static UpdateWord create(UpdateType type) {
        return switch (type) {
            case all ->  new UpdateAll();
            case memo -> new UpdateMemo();
            case memorization -> new UpdateMemorization();
        };
    }

}
