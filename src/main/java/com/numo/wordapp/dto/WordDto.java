package com.numo.wordapp.dto;

import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordDto {
    @Getter
    @Setter
    public static class Request{
        private int word_id;
        private String word;
        private String mean;
        private String wread;
        private String memo;
        private List<SynonymDto.Request> synonyms;
        //private SynonymDto.Request synonym;

        public Word toEntity(){
            Word words = Word.builder()
                    //.word_id(word_id)
                    .word(word)
                    .mean(mean)
                    .wread(wread)
                    .memo(memo)
                    //.synonyms(synonyms.stream().map(Synonym::new).collect(Collectors.toList()))
                    .build();
            return words;
        }
    }
    @Getter
    public static class Response{
        private int word_id;
        private String word;
        private String mean;
        private String wread;
        private String memo;
        private List<SynonymDto.Response> synonyms;

        public Response(Word words){
            this.word_id = words.getWord_id();
            this.word = words.getWord();
            this.mean = words.getMean();
            this.wread = words.getWread();
            this.memo = words.getMemo();
            //synonymsDto에 맞게 컬럼 생성
            //stream을 이용하여 List<Synonyms> => List<SynonymDto.Response>로 형 변환
            this.synonyms = words.getSynonyms().stream().map(SynonymDto.Response::new).collect(Collectors.toList());
        }
    }
}
