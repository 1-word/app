package com.numo.wordapp.dto;

import com.numo.wordapp.model.word.detail.WordDetailSub;
import com.numo.wordapp.model.word.detail.WordDetailTitle;
import lombok.Getter;

public class WordDetailSubDto {
    @Getter
    public static class Request{
        private int detail_sub_id;
        private int title_id;
        private String content;
        private String  memo;

        public WordDetailSub toEntity(){
            return WordDetailSub.builder()
                    .detailSubId(detail_sub_id)
                    .wordDetailTitle(new WordDetailTitle(title_id))
                    .content(content)
                    .memo(memo)
                    .build();
        }
    }

    // Getter가 없으면, Jackson에서 데이터 bind 처리할 때 에러
    @Getter
    public static class Response{
        private int detail_sub_id;
//        private int word_detail_main_id;
        private String title_name;
        private String content;
        private String memo;

        public Response(WordDetailSub wordDetailSub){
            this.detail_sub_id = wordDetailSub.getDetailSubId();
            this.title_name = wordDetailSub.getWordDetailTitle().getTitleName();
            this.content = wordDetailSub.getContent();
            this.memo = wordDetailSub.getMemo();
        }
    }
}
