package com.numo.api.domain.wordbook.word.dto;

import com.numo.domain.user.User;
import com.numo.domain.wordbook.word.Word;
import com.numo.domain.wordbook.detail.WordDetail;
import com.numo.domain.wordbook.folder.Folder;
import com.numo.domain.wordbook.sound.Sound;
import com.numo.domain.wordbook.sound.type.GttsCode;
import com.numo.api.domain.wordbook.detail.dto.WordDetailRequestDto;
import lombok.Builder;

import java.util.List;

@Builder
public record WordRequestDto(
        Long folderId,
        String word,
        String mean,
        String read,
        String memo,
        String memorization,
        List<WordDetailRequestDto> details
) {

    public Word toEntity(Long userId, String gttsType) {
        User user = User.builder().userId(userId).build();
        Folder folder = Folder.builder().folderId(folderId).build();
        List<WordDetail> wordDetails = details.stream().map(WordDetailRequestDto::toEntity).toList();
        return Word.builder()
                .user(user)
                .folder(folder)
                .wordDetails(wordDetails)
                .word(word)
                .mean(mean)
                .read(read)
                .memo(memo)
                .memorization(memorization)
                .lang(GttsCode.valueOf(gttsType))
                .build();
    }

    public Word toEntity(Long userId, String gttsType, Long soundId) {
        User user = User.builder().userId(userId).build();
        Folder folder = null;
        if (folderId != null) {
            folder = Folder.builder().folderId(folderId).build();
        }
        Sound sound = null;
        if (soundId != null) {
            sound = Sound.builder().soundId(soundId).build();
        }
        List<WordDetail> wordDetails = details.stream().map(WordDetailRequestDto::toEntity).toList();
        return Word.builder()
                .user(user)
                .folder(folder)
                .wordDetails(wordDetails)
                .word(word)
                .mean(mean)
                .read(read)
                .memo(memo)
                .memorization(memorization)
                .lang(GttsCode.valueOf(gttsType))
                .sound(sound)
                .build();
    }
}
