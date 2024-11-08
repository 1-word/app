package com.numo.wordapp.dto.word;

import com.numo.wordapp.entity.word.detail.WordDetailTitle;
import com.numo.wordapp.entity.user.User;
import lombok.Getter;

public class WordDetailTitleDto {
    public static class Request{
        private int title_id;
        private String user_id;
        private String title_type;
        private String title_name;
        private String memo;

        public WordDetailTitle toEntity(User user){
            return WordDetailTitle.builder()
                    .titleId(title_id)
                    .user(user)
                    .titleType(title_type)
                    .titleName(title_name)
                    .build();
        }
    }

    @Getter
    public static class Response{
        private int title_id;
        private String title_name;
        private String memo;

        public Response(WordDetailTitle wordDetailTitle){
            this.title_id = wordDetailTitle.getTitleId();
            this.title_name = wordDetailTitle.getTitleName();
            this.memo = wordDetailTitle.getMemo();
        }
    }
}
