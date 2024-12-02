package com.numo.wordapp.comm.init;

import com.numo.wordapp.entity.word.detail.WordGroup;
import com.numo.wordapp.entity.word.detail.WordGroupType;
import com.numo.wordapp.repository.word.group.WordGroupRepository;
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
