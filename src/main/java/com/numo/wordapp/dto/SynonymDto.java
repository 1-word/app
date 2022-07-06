package com.numo.wordapp.dto;

import com.numo.wordapp.model.Synonym;
import com.numo.wordapp.model.Word;
import lombok.*;

public class SynonymDto {
    @Getter
    @Setter
    public static class Request {
        private int synonym_id=0;
        private String synonym="";
        private String memo="";
        //private Word word;

        public Synonym toEntity(){
            Synonym synonyms = Synonym.builder()
                    .synonym_id(synonym_id)
                    .synonym(synonym)
                    .memo(memo)
                    .build();
            return synonyms;
        }

        public Synonym toEntity(Word word){
            Synonym synonyms = Synonym.builder()
                    //.synonym_id(synonym_id)
                    .synonym(synonym)
                    .memo(memo)
                    .word(word)
                    .build();
            return synonyms;
        }
    }
    @Getter
    public static class Response {
       private int synonym_id;
        private String synonym;
        private String memo;
        //private int word_id;

        public Response(Synonym synonyms){
            this.synonym_id = synonyms.getSynonym_id();
            this.synonym = synonyms.getSynonym();
            this.memo = synonyms.getMemo();
            //this.word_id = synonyms.getWord().getWord_id();
        }
    }
}
