package com.numo.api.global.comm.init;

import com.numo.domain.word.detail.WordGroup;
import com.numo.domain.word.detail.type.WordGroupType;
import com.numo.api.domain.wordbook.group.repository.WordGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final WordGroupRepository wordGroupRepository;

    @Override
    public void run(String... args) throws Exception {
        if (!hasInitData()) {
            initData();
        }
    }

    private boolean hasInitData() {
        return wordGroupRepository.existsByDefaultGroup("Y");
    }

    private void initData() {
        Arrays.stream(WordGroupType.values())
                .forEach(wordGroupType -> {
                    WordGroup wordGroup = WordGroup.builder()
                            .name(wordGroupType.getName())
                            .description(wordGroupType.getName())
                            .defaultGroup("Y")
                            .build();
                    wordGroupRepository.save(wordGroup);
                });
    }
}
