package com.numo.wordapp.dto;

import com.numo.wordapp.model.word.Word;
import com.numo.wordapp.model.word.detail.WordDetailMain;
import com.numo.wordapp.model.word.detail.WordDetailSub;
import com.numo.wordapp.model.word.detail.WordDetailTitle;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WordDetailMainDto {
    @Getter
    @Setter
    public static class Request {
        private int detail_id;
        private int title_id;
        private String content="";
        private String memo="";
        private List<WordDetailSubDto.Request> detailSubs = new ArrayList<>();

        public WordDetailMain toEntity(){
            WordDetailMain detail = WordDetailMain.builder()
                    .detailMainId(detail_id)
//                    .wordDetailTitle(WordDetailTitleDto.Request.toEntity(title_id))
                    .content(content)
                    .memo(memo)
                    .build();
            return detail;
        }

        public WordDetailMain toEntity(WordDetailTitle wordDetailTitle){
            WordDetailMain detail = WordDetailMain.builder()
                    .detailMainId(detail_id)
                    .wordDetailTitle(wordDetailTitle)
                    .content(content)
                    .memo(memo)
                    .build();
            return detail;
        }

        public WordDetailMain toEntity(Word word, WordDetailTitle wordDetailTitle){
            System.out.println(detailSubs);
            WordDetailMain detail = WordDetailMain.builder()
                    .detailMainId(detail_id)
//                    .title(title)
                    .content(content)
                    .memo(memo)
                    .word(word)
                    .wordDetailSub(detailSubs.stream().map(WordDetailSubDto.Request::toEntity).collect(Collectors.toList()))
                    .wordDetailTitle(wordDetailTitle)
                    .build();
            return detail;
        }
    }
    @Getter
    public static class Response {
       private int detail_id;
       private int title_id;
        private String title_name;
//        private String title_type;
        private String content;
        private String memo;
        private List<WordDetailSubDto.Response> detailSubs = new ArrayList<>();

        public Response(WordDetailMain wordDetailMain){
            this.detail_id = wordDetailMain.getDetailMainId();
            this.title_id = wordDetailMain.getWordDetailTitle().getTitleId();
            this.title_name = wordDetailMain.getWordDetailTitle().getTitleName();
            this.content = wordDetailMain.getContent();
            this.memo = wordDetailMain.getMemo();
            this.detailSubs = wordDetailMain.getWordDetailSub().stream().map(WordDetailSubDto.Response::new).collect(Collectors.toList());
        }
    }
}
