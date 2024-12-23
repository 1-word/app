package com.numo.wordapp.service.word.update;

import com.numo.domain.word.type.UpdateType;

public class UpdateFactory {

    public static UpdateWord create(UpdateType type) {
        return switch (type) {
            case all ->  new UpdateAll();
            case memo -> new UpdateMemo();
            case folder -> new UpdateFolder();
            case memorization -> new UpdateMemorization();
        };
    }

}
