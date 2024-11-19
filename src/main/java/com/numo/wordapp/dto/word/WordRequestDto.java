package com.numo.wordapp.dto.word;

import com.numo.wordapp.dto.word.detail.WordDetailRequestDto;
import com.numo.wordapp.entity.user.User;
import com.numo.wordapp.entity.word.Folder;
import com.numo.wordapp.entity.word.GttsCode;
import com.numo.wordapp.entity.word.Sound;
import com.numo.wordapp.entity.word.Word;
import com.numo.wordapp.entity.word.detail.WordDetail;
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
