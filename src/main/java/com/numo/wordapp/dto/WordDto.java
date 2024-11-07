package com.numo.wordapp.dto;

import com.numo.wordapp.model.word.Word;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class WordDto{
    @Builder
    @Getter
    public static class Read {
        private int page;
        private String user_id;
        private int folder_id;
        private int last_word_id;
        private String search_text;
        private String memorization;
        private String language;
    }
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
        private String read;
        private String memo;
        private String memorization;
        private Integer folder_id;
        //private String type;
        private List<WordDetailMainDto.Request> details;
        //private SynonymDto.Request synonym;

        public Word toEntity(){
            Word words = Word.builder()
                    //.word_id(word_id)
                    .userId(user_id)
                    .word(word)
                    .mean(mean)
                    .read(read)
                    .memo(memo)
                    .memorization(memorization)
                    .folderId(folder_id)
                    //.type(Gtts.valueOf(type))
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
        private String read;
        private String memo;
        private String soundPath;
        private List<WordDetailMainDto.Response> details;
        private String update_time;
        private String create_time;
        private String memorization;
        private String type;
        private FolderDto.Response folder;

        public Response(Word words){
            this.word_id = words.getWordId();
            this.word = words.getWord();
            this.mean = words.getMean();
            this.read = words.getRead();
            this.memo = words.getMemo();
            this.soundPath = words.getSoundPath();
            this.memorization = words.getMemorization();
            this.type = words.getLang().getValue();
            //synonymsDto에 맞게 컬럼 생성
            //stream을 이용하여 List<Synonyms> => List<SynonymDto.Response>로 형 변환
            this.details = words.getWordDetailMains().stream().map(WordDetailMainDto.Response::new).collect(Collectors.toList());
            this.update_time = words.getUpdate_time();
            this.create_time = words.getCreate_time();
//            this.folder = words.getFolder();
            if (words.getFolder() != null) {
                this.folder = new FolderDto.Response(words.getFolder());
            }
        }
    }
}
