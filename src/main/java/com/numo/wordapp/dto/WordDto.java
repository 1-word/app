package com.numo.wordapp.dto;

import com.numo.wordapp.model.Word;
import com.numo.wordapp.security.model.WordType;
import lombok.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class WordDto {
    /**
     * 클라이언트 -> API
     */
    @Getter
    @Setter
    public static class Request{
        private int word_id;
        private String user_id;
        private String word;
        private String mean;
        private String wread;
        private String memo;
        private String memorization;
        private Integer folder_id;
        private String type;
        private List<SynonymDto.Request> synonyms;
        //private SynonymDto.Request synonym;

        public Word toEntity(){
            Word words = Word.builder()
                    //.word_id(word_id)
                    .userId(user_id)
                    .word(word)
                    .mean(mean)
                    .wread(wread)
                    .memo(memo)
                    .memorization(memorization)
                    .folderId(folder_id)
                    .type(WordType.valueOf(type))
                    //.synonyms(synonyms.stream().map(Synonym::new).collect(Collectors.toList()))
                    .build();
            return words;
        }
    }

    /**
     * API -> 클라이언트 응답
     */

    @Getter
    public static class Response{
        private int word_id;
        private String word;
        private String mean;
        private String wread;
        private String memo;
        private String soundPath;
        private List<SynonymDto.Response> synonyms;
        private String update_time;
        private String memorization;
        private String type;

        public Response(Word words){
            this.word_id = words.getWordId();
            this.word = words.getWord();
            this.mean = words.getMean();
            this.wread = words.getWread();
            this.memo = words.getMemo();
            this.soundPath = words.getSoundPath();
            this.memorization = words.getMemorization();
            this.type = words.getType().getValue();
            //synonymsDto에 맞게 컬럼 생성
            //stream을 이용하여 List<Synonyms> => List<SynonymDto.Response>로 형 변환
            this.synonyms = words.getSynonyms().stream().map(SynonymDto.Response::new).collect(Collectors.toList());
            try{
                this.update_time = words.getUpdate_time().format(DateTimeFormatter.ISO_DATE);
            }catch (Exception e){
                this.update_time = "";
            }
        }
    }
}
